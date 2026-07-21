package kmch.vaccine.form.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kmch.vaccine.form.domain.error.DomainError
import kmch.vaccine.form.domain.usecase.CreateLotUseCase
import kmch.vaccine.form.domain.usecase.CreateVaccineUseCase
import kmch.vaccine.form.domain.usecase.ListRegistrationsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminListViewModel(
    private val listRegistrationsUseCase: ListRegistrationsUseCase,
    private val createVaccineUseCase: CreateVaccineUseCase,
    private val createLotUseCase: CreateLotUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminListUiState())
    val uiState: StateFlow<AdminListUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        search()
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            search()
        }
    }

    private fun search() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            listRegistrationsUseCase(query = _uiState.value.searchQuery.ifBlank { null })
                .onSuccess { page -> _uiState.update { it.copy(registrations = page.items, isLoading = false) } }
                .onFailure { _uiState.update { it.copy(isLoading = false) } }
        }
    }

    fun onLotInputChange(value: String) = _uiState.update { it.copy(lotInput = value.filter(Char::isDigit)) }
    fun onEmployeeRateIdChange(value: String) = _uiState.update { it.copy(employeeRateId = value) }
    fun onShowAddVaccineDialogChange(show: Boolean) = _uiState.update { it.copy(showAddVaccineDialog = show) }
    fun dismissDialogAlert() = _uiState.update { it.copy(dialogAlert = null) }

    fun createVaccine(name: String) {
        viewModelScope.launch {
            createVaccineUseCase(name)
                .onSuccess { info ->
                    _uiState.update {
                        it.copy(dialogAlert = AdminDialogAlert.VaccineCreated(info.vaccineId), showAddVaccineDialog = false)
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(dialogAlert = AdminDialogAlert.Failure(error as? DomainError ?: DomainError.NetworkError)) }
                }
        }
    }

    fun createLot(vaccineIdRaw: String, lotNumber: String, initialQuantity: Int) {
        val vaccineId = vaccineIdRaw.toIntOrNull()
        if (vaccineId == null) {
            _uiState.update { it.copy(dialogAlert = AdminDialogAlert.InvalidVaccineId) }
            return
        }
        viewModelScope.launch {
            createLotUseCase(vaccineId, lotNumber, initialQuantity)
                .onSuccess { info ->
                    _uiState.update {
                        it.copy(dialogAlert = AdminDialogAlert.LotCreated(info.lotId), showAddVaccineDialog = false)
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(dialogAlert = AdminDialogAlert.Failure(error as? DomainError ?: DomainError.NetworkError)) }
                }
        }
    }
}
