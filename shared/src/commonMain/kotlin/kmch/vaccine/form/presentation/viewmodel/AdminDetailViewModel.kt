package kmch.vaccine.form.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kmch.vaccine.form.domain.error.DomainError
import kmch.vaccine.form.domain.model.RegistrationDetail
import kmch.vaccine.form.domain.usecase.GetRegistrationDetailUseCase
import kmch.vaccine.form.domain.usecase.PrintScreeningDocumentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminDetailUiState(
    val detail: RegistrationDetail? = null,
    val isLoading: Boolean = true,
    val loadError: DomainError? = null
)

class AdminDetailViewModel(
    private val patientId: Int,
    private val getRegistrationDetailUseCase: GetRegistrationDetailUseCase,
    private val printScreeningDocumentUseCase: PrintScreeningDocumentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminDetailUiState())
    val uiState: StateFlow<AdminDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loadError = null) }
            getRegistrationDetailUseCase(patientId)
                .onSuccess { detail ->
                    _uiState.update {
                        if (detail == null) {
                            it.copy(isLoading = false, loadError = DomainError.Unknown(404))
                        } else {
                            it.copy(isLoading = false, detail = detail)
                        }
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, loadError = error as? DomainError ?: DomainError.NetworkError)
                    }
                }
        }
    }

    fun print() {
        _uiState.value.detail?.let { printScreeningDocumentUseCase(it) }
    }
}
