package kmch.vaccine.form.presentation.screen.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kmch.vaccine.form.presentation.localization.LocalStrings
import kmch.vaccine.form.presentation.screen.screening.ScreeningFormScreen
import kmch.vaccine.form.presentation.viewmodel.AdminSessionViewModel
import kotlinx.browser.window
import org.koin.compose.viewmodel.koinViewModel
import org.w3c.dom.events.Event

// Auth gate: shows the password screen first, then the dashboard once verified.
// Also owns the idle auto-logout timer (AdminSessionViewModel) for the entire
// admin area, since this composable stays mounted for as long as the admin
// route is active.
@Composable
fun ProtectedAdminScreen(onNavigateBack: () -> Unit) {
    val sessionViewModel: AdminSessionViewModel = koinViewModel()
    val sessionState by sessionViewModel.uiState.collectAsState()

    DisposableEffect(Unit) {
        val onActivity: (Event) -> Unit = { sessionViewModel.markActivity() }
        window.addEventListener("mousemove", onActivity)
        window.addEventListener("keydown", onActivity)
        window.addEventListener("click", onActivity)
        onDispose {
            window.removeEventListener("mousemove", onActivity)
            window.removeEventListener("keydown", onActivity)
            window.removeEventListener("click", onActivity)
        }
    }

    if (sessionState.isAuthenticated) {
        AdminDashboardScreen(onNavigateBack = onNavigateBack)
    } else {
        AdminLoginScreen(
            onLoginSuccess = { sessionViewModel.login() },
            onNavigateBack = onNavigateBack
        )
    }
}

@Composable
fun AdminDashboardScreen(onNavigateBack: () -> Unit) {
    val strings = LocalStrings.current
    var selectedPatientId by remember { mutableStateOf<Int?>(null) }
    var isCreatingNew by remember { mutableStateOf(false) }

    when {
        selectedPatientId != null -> {
            AdminDetailScreen(
                patientId = selectedPatientId!!,
                onBack = { selectedPatientId = null }
            )
        }

        isCreatingNew -> {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Button(onClick = { isCreatingNew = false }) {
                    Text(strings.backButton)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(strings.newScreeningByStaffTitle, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                ScreeningFormScreen()
            }
        }

        else -> {
            AdminListScreen(
                onPatientSelected = { selectedPatientId = it },
                onCreateNew = { isCreatingNew = true }
            )
        }
    }
}
