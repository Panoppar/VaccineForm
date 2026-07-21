package kmch.vaccine.form.data.remote

import kmch.vaccine.form.domain.error.DomainError

fun Throwable.toDomainError(): DomainError = when (this) {
    is ApiException -> when (statusCode) {
        400 -> DomainError.InvalidDataOrDuplicate
        409 -> DomainError.VaccineOutOfStock
        else -> DomainError.Unknown(statusCode, message)
    }
    else -> DomainError.NetworkError
}

suspend inline fun <T> runCatchingDomain(block: suspend () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: Exception) {
        Result.failure(e.toDomainError())
    }
