package weatherstations.models

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import weatherstations.data.UnixTimestampAdapter
import java.util.Date

fun emptyStation(): Station = Station(id = "", name = "")

data class Station(
    var id: String,

    @SerializedName("irenginys")
    val name: String,

    @SerializedName("pavadinimas")
    val road: String = "",

    @SerializedName("surinkimo_data")
    val updated: String = "",

    @JsonAdapter(UnixTimestampAdapter::class)
    @SerializedName("surinkimo_data_unix")
    val updatedUnix: Date? = null,

    @SerializedName("rasos_taskas")
    val dewPoint: Double = 0.0,

    @SerializedName("oro_temperatura")
    val temperature: Double = 0.0,

    @SerializedName("vejo_greitis_vidut")
    val windAverage: Double = 0.0,

    @SerializedName("vejo_greitis_maks")
    val windMax: Double = 0.0,

    @SerializedName("vejo_kryptis")
    val windDirection: String? = "",

    @SerializedName("krituliu_kiekis")
    val precipitation: Double = 0.0,

    @SerializedName("lat")
    val latitude: Double = 0.0,

    @SerializedName("lng")
    val longitude: Double = 0.0,

    @SerializedName("kelio_danga")
    val roadSurface: String = "",

    @SerializedName("matomumas")
    val visibility: Int = 0,

    val isStarred: Boolean = false,
    var distance: Float = 0.0f,
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val combinations = listOf(
            name,
            road,
        )
        return combinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}