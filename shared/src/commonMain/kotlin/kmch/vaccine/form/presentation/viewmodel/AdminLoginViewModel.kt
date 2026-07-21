package kmch.vaccine.form.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kmch.vaccine.form.domain.usecase.AuthenticateAdminUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AdminLoginUiState(
    val passwordInput: String = "",
    val isError: Boolean = false,
    val isAuthenticated: Boolean = false
)

class AdminLoginViewModel(
    private val authenticateAdminUseCase: AuthenticateAdminUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminLoginUiState())
    val uiState: StateFlow<AdminLoginUiState> = _uiState.asStateFlow()

    fun onPasswordChange(value: String) = _uiState.update { it.copy(passwordInput = value, isError = false) }

    fun submit() {
        val authenticated = authenticateAdminUseCase(_uiState.value.passwordInput)
        _uiState.update { it.copy(isAuthenticated = authenticated, isError = !authenticated) }
    }
}
