package kmch.vaccine.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kmch.vaccine.form.di.appModule
import kmch.vaccine.form.presentation.component.LanguageToggle
import kmch.vaccine.form.presentation.localization.LocalStrings
import kmch.vaccine.form.presentation.localization.LocalizationController
import kmch.vaccine.form.presentation.localization.strings
import kmch.vaccine.form.presentation.navigation.AppRoute
import kmch.vaccine.form.presentation.navigation.parseRoute
import kmch.vaccine.form.presentation.screen.admin.ProtectedAdminScreen
import kmch.vaccine.form.presentation.screen.screening.ScreeningFormScreen
import kmch.vaccine.form.presentation.theme.AppTheme
import kotlinx.browser.window
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.w3c.dom.events.Event

@Composable
fun App() {
    KoinApplication(application = { modules(appModule) }) {
        AppContent()
    }
}

@Composable
private fun AppContent() {
    // ดึงค่า Hash จาก URL ปัจจุบัน (เช่น #/vaccine/form)
    var currentRoute by remember { mutableStateOf(window.location.hash) }

    // คอยดักจับว่าผู้ใช้มีการพิมพ์เปลี่ยน URL ใน Address Bar หรือไม่
    DisposableEffect(Unit) {
        val onHashChange: (Event) -> Unit = {
            currentRoute = window.location.hash
        }
        window.addEventListener("hashchange", onHashChange)
        onDispose {
            window.removeEventListener("hashchange", onHashChange)
        }
    }

    val localizationController = koinInject<LocalizationController>()
    val language by localizationController.language.collectAsState()

    CompositionLocalProvider(LocalStrings provides strings(language)) {
        AppTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        LanguageToggle(
                            current = language,
                            onLanguageChange = localizationController::setLanguage,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        when (parseRoute(currentRoute)) {
                            AppRoute.Admin -> {
                                ProtectedAdminScreen(
                                    onNavigateBack = { window.location.hash = "#/vaccine/form" }
                                )
                            }

                            AppRoute.ScreeningForm -> {
                                ScreeningFormScreen()
                            }

                            AppRoute.NotFound -> {
                                NotFoundScreen(onGoToForm = { window.location.hash = "#/vaccine/form" })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotFoundScreen(onGoToForm: () -> Unit) {
    val strings = LocalStrings.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = strings.notFoundTitle,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = strings.notFoundMessage,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onGoToForm) {
            Text(strings.goToScreeningForm)
        }
    }
}
