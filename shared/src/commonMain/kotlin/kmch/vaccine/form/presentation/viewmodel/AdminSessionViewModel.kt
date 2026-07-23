package kmch.vaccine.form.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

data class AdminSessionUiState(
    val isAuthenticated: Boolean = false
)

// HIPAA §164.312(a)(2)(iii) "Automatic Logoff" / PDPA §37 "appropriate
// security measures" — a technical safeguard, not a compliance guarantee.
// Bump IDLE_TIMEOUT if 15 minutes is too aggressive for your admin workflow.
class AdminSessionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AdminSessionUiState())
    val uiState: StateFlow<AdminSessionUiState> = _uiState.asStateFlow()

    private var lastActivityAt: Instant = Clock.System.now()

    init {
        viewModelScope.launch {
            while (true) {
                delay(IDLE_CHECK_INTERVAL)
                if (_uiState.value.isAuthenticated && Clock.System.now() - lastActivityAt >= IDLE_TIMEOUT) {
                    logout()
                }
            }
        }
    }

    fun login() {
        lastActivityAt = Clock.System.now()
        _uiState.update { it.copy(isAuthenticated = true) }
    }

    fun logout() {
        _uiState.update { it.copy(isAuthenticated = false) }
    }

    // Called on any real page activity (mouse move, keydown, click) so the
    // idle clock only advances while the admin is genuinely away.
    fun markActivity() {
        lastActivityAt = Clock.System.now()
    }

    companion object {
        val IDLE_TIMEOUT = 15.minutes
        private val IDLE_CHECK_INTERVAL = 30.seconds
    }
}
