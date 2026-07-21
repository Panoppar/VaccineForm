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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kmch.vaccine.form.presentation.localization.LocalStrings
import kmch.vaccine.form.presentation.screen.screening.ScreeningFormScreen

// Auth gate: shows the password screen first, then the dashboard once verified.
@Composable
fun ProtectedAdminScreen(onNavigateBack: () -> Unit) {
    var isAuthenticated by remember { mutableStateOf(false) }

    if (isAuthenticated) {
        AdminDashboardScreen(onNavigateBack = onNavigateBack)
    } else {
        AdminLoginScreen(
            onLoginSuccess = { isAuthenticated = true },
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
