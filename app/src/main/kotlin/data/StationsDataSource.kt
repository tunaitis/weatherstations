
package weatherstations.data

import com.google.gson.JsonParser
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import weatherstations.models.Station
import java.nio.channels.UnresolvedAddressException
import java.util.Date

object UnixTimestampAdapter : TypeAdapter<Date>() {
    override fun write(out: JsonWriter, value: Date?) {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.getTime() / 1000);
    }

    override fun read(input: JsonReader): Date? {
        if (input.peek() == JsonToken.NULL) {
            input.nextNull();
            return null;
        }
        return Date(input.nextLong() * 1000);
    }
}

class StationsDataSource {
    private val client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson {
                registerTypeAdapter(Date::class.java, UnixTimestampAdapter)
            }
        }
    }

    private inline fun <R> safeExecute(call: () -> R): Result<R> {
        return try {
            Result.Success(call())
        } catch (e: UnresolvedAddressException) {
            Result.Error(ErrorType.NetworkError)
        } catch (e: Exception) {
            Result.Error(ErrorType.UnknownError)
        }
    }

    suspend fun getStations(): Result<List<Station>> {
        return safeExecute {
            client
                .get("https://eismoinfo.lt/weather-conditions-service")
                .body<List<Station>>()
        }
    }

    suspend fun getHistoricalData(id: String): Result<List<Station>> {
        return safeExecute {
            var entries = client
                .get("https://eismoinfo.lt/weather-conditions-retrospective?id=$id&number=500")
                .body<List<Station>>()

            var modified = mutableListOf<Station>()


            for (i in entries.indices) {
                if (i == 0) {
                    modified.add(Station(id = "", name = "", updatedUnix = entries[i].updatedUnix))
                }

                if (i > 0 && entries[i-1].updatedUnix?.day != entries[i].updatedUnix?.day) {
                    modified.add(Station(id = "", name = "", updatedUnix = entries[i].updatedUnix))
                }
                modified.add(entries[i])
            }

            modified
        }
    }

    suspend fun getStationPhoto(id: String): Result<String> {
        return safeExecute {
            val resp = client
                .get("https://eismoinfo.lt/eismoinfo-backend/feature-info/OSI/WD:$id?returnIcon=true")
                .body<String>()

            val parser = JsonParser.parseString(resp)
            val photos = parser
                .asJsonObject
                .get("info")
                .asJsonArray
                .get(0)
                .asJsonObject
                .get("photos")
                .asJsonArray

            if (photos.size() == 0) {
                ""
            } else {
                photos.get(0).asString
            }
        }
    }
}