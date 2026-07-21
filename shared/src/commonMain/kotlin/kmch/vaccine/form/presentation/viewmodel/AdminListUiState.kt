package kmch.vaccine.form.presentation.viewmodel

import kmch.vaccine.form.domain.error.DomainError
import kmch.vaccine.form.domain.model.RegistrationSummary

data class AdminListUiState(
    val lotInput: String = "",
    val employeeRateId: String = "",
    val searchQuery: String = "",
    val registrations: List<RegistrationSummary> = emptyList(),
    val isLoading: Boolean = true,
    val showAddVaccineDialog: Boolean = false,
    val dialogAlert: AdminDialogAlert? = null
)

sealed class AdminDialogAlert {
    data class VaccineCreated(val vaccineId: Int) : AdminDialogAlert()
    data class LotCreated(val lotId: Int) : AdminDialogAlert()
    data object InvalidVaccineId : AdminDialogAlert()
    data class Failure(val error: DomainError) : AdminDialogAlert()
}
