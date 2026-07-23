package kmch.vaccine.form.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kmch.vaccine.form.domain.usecase.AuthenticateAdminUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

data class AdminLoginUiState(
    val passwordInput: String = "",
    val isError: Boolean = false,
    val isAuthenticated: Boolean = false,
    val isLockedOut: Boolean = false
)

class AdminLoginViewModel(
    private val authenticateAdminUseCase: AuthenticateAdminUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminLoginUiState())
    val uiState: StateFlow<AdminLoginUiState> = _uiState.asStateFlow()

    // Brute-force throttling against the single shared admin password —
    // a PDPA/HIPAA-aligned technical safeguard, not a substitute for
    // per-user credentials.
    private var failedAttempts = 0
    private var lockedUntil: Instant? = null

    fun onPasswordChange(value: String) = _uiState.update { it.copy(passwordInput = value, isError = false) }

    fun submit() {
        val now = Clock.System.now()
        val until = lockedUntil
        if (until != null && now < until) {
            _uiState.update { it.copy(isLockedOut = true, isError = false) }
            return
        }
        lockedUntil = null

        val authenticated = authenticateAdminUseCase(_uiState.value.passwordInput)
        if (authenticated) {
            failedAttempts = 0
            _uiState.update { it.copy(isAuthenticated = true, isError = false, isLockedOut = false) }
        } else {
            failedAttempts += 1
            if (failedAttempts >= MAX_ATTEMPTS) {
                lockedUntil = now + LOCKOUT_DURATION
                _uiState.update { it.copy(isError = false, isLockedOut = true) }
            } else {
                _uiState.update { it.copy(isError = true, isLockedOut = false) }
            }
        }
    }

    companion object {
        private const val MAX_ATTEMPTS = 5
        private val LOCKOUT_DURATION = 60.seconds
    }
}
