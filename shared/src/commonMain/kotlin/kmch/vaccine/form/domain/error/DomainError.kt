package kmch.vaccine.form.domain.error

// Deliberately carries no human-readable message for the well-known cases — the
// presentation layer maps each one to a localized Strings field. Unknown still
// carries the raw backend body text since admin screens (e.g. add vaccine/lot)
// show it verbatim for diagnostics, same as before this refactor.
sealed class DomainError : Exception() {
    data object InvalidDataOrDuplicate : DomainError()
    data object VaccineOutOfStock : DomainError()
    data object NetworkError : DomainError()
    data class Unknown(val code: Int, val rawMessage: String? = null) : DomainError()
}
