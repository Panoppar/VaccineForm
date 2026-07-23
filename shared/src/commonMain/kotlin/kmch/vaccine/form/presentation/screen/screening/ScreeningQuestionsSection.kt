package kmch.vaccine.form.presentation.screen.screening

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kmch.vaccine.form.presentation.localization.LocalStrings
import kmch.vaccine.form.presentation.viewmodel.ScreeningFormUiState
import kmch.vaccine.form.presentation.viewmodel.ScreeningFormViewModel

@Composable
fun ScreeningQuestionsSection(state: ScreeningFormUiState, viewModel: ScreeningFormViewModel) {
    val strings = LocalStrings.current

    Text(strings.healthScreeningSectionTitle, style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))

    if (state.questionList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = strings.noScreeningQuestionsMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center
            )
        }
    } else {
        state.questionList.forEach { question ->
            val qId = question.questionId

            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(question.displayText, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (question.isTextQuestion) {
                        OutlinedTextField(
                            value = state.remarks[qId] ?: "",
                            onValueChange = { newValue -> viewModel.onRemarkChange(qId, newValue) },
                            label = { Text(strings.specifyYourAnswerLabel) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = false,
                            maxLines = 3
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.selectableGroup()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.selectable(
                                    selected = state.answers[qId] == true,
                                    onClick = { viewModel.onAnswerChange(qId, true) },
                                    role = Role.RadioButton
                                )
                            ) {
                                RadioButton(selected = state.answers[qId] == true, onClick = null)
                                Text(strings.answerYes)
                            }
                            Spacer(modifier = Modifier.width(24.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.selectable(
                                    selected = state.answers[qId] == false,
                                    onClick = { viewModel.onAnswerChange(qId, false) },
                                    role = Role.RadioButton
                                )
                            ) {
                                RadioButton(selected = state.answers[qId] == false, onClick = null)
                                Text(strings.answerNo)
                            }
                        }
                    }
                }
            }
        }
    }
}
