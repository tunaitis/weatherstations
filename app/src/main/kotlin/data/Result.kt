package weatherstations.data

sealed class Result<T> {
    data class Success<T>(val data: T): Result<T>()
    data class Error<T>(val error: ErrorType): Result<T>()
}

sealed class ErrorType(val message: String) {

    data object UnknownError: ErrorType(message = "An unknown error has occurred")
    data object NetworkError: ErrorType(message = "A network error has occurred")
}