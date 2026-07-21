package kmch.vaccine.form.presentation.localization

import kmch.vaccine.form.domain.error.DomainError
import kmch.vaccine.form.presentation.viewmodel.AdminDialogAlert

fun Strings.submitErrorMessage(error: DomainError): String = when (error) {
    DomainError.InvalidDataOrDuplicate -> submitErrorInvalidOrDuplicate
    DomainError.VaccineOutOfStock -> submitErrorVaccineOutOfStock
    is DomainError.Unknown -> submitErrorGeneric(error.code)
    DomainError.NetworkError -> submitErrorNetwork
}

fun Strings.dialogAlertMessage(alert: AdminDialogAlert): String = when (alert) {
    is AdminDialogAlert.VaccineCreated -> vaccineCreatedMessage(alert.vaccineId)
    is AdminDialogAlert.LotCreated -> lotCreatedMessage(alert.lotId)
    is AdminDialogAlert.InvalidVaccineId -> vaccineIdMustBeNumberError
    is AdminDialogAlert.Failure -> {
        val error = alert.error
        if (error is DomainError.Unknown && !error.rawMessage.isNullOrBlank()) {
            genericErrorMessage(error.rawMessage)
        } else {
            submitErrorMessage(error)
        }
    }
}
