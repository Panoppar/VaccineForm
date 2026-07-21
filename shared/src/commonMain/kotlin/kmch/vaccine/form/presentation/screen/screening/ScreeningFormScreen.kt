package kmch.vaccine.form.presentation.screen.screening

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kmch.vaccine.form.presentation.localization.LocalStrings
import kmch.vaccine.form.presentation.localization.submitErrorMessage
import kmch.vaccine.form.presentation.viewmodel.ScreeningFormViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ScreeningFormScreen(viewModel: ScreeningFormViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val strings = LocalStrings.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    if (state.isSubmitted) {
        SubmissionSuccessScreen(
            successResponse = state.successResponse,
            onReturnToHome = {
                viewModel.reset()
                coroutineScope.launch { scrollState.scrollTo(0) }
            }
        )
        return
    }

    when {
        state.isLoadingQuestions -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.loadError != null -> {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = strings.screeningLoadError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = viewModel::loadQuestions) {
                    Text(strings.retry)
                }
            }
        }

        else -> {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(scrollState)
            ) {
                Text(
                    text = strings.screeningFormTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = strings.screeningFormSubtitle, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(24.dp))

                PatientInfoSection(state, viewModel)

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                ScreeningQuestionsSection(state, viewModel)

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                ConsentSection(state, viewModel)

                Spacer(modifier = Modifier.height(16.dp))

                state.submitError?.let { error ->
                    Text(
                        text = strings.submitErrorMessage(error),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = viewModel::submit,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = state.canSubmit
                ) {
                    Text(if (state.isSubmitting) strings.submitting else strings.confirmAndSubmit)
                }
            }
        }
    }
}
