package kmch.vaccine.form.presentation.screen.screening

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kmch.vaccine.form.presentation.localization.LocalStrings
import kmch.vaccine.form.presentation.viewmodel.ScreeningFormUiState
import kmch.vaccine.form.presentation.viewmodel.ScreeningFormViewModel

private const val PDPA_URL = "https://pdpa.kmitl.ac.th"

@Composable
fun ConsentSection(state: ScreeningFormUiState, viewModel: ScreeningFormViewModel) {
    val strings = LocalStrings.current
    val uriHandler = LocalUriHandler.current

    Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
        Checkbox(
            checked = state.acceptedTerms,
            onCheckedChange = viewModel::onAcceptedTermsChange,
            modifier = Modifier.padding(end = 8.dp)
        )

        val pdpaAnnotatedString = buildAnnotatedString {
            append(strings.pdpaConsentText)
            pushStringAnnotation(tag = "URL", annotation = PDPA_URL)
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )
            ) { append(strings.pdpaLinkLabel) }
            pop()
        }

        ClickableText(
            text = pdpaAnnotatedString,
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.padding(top = 12.dp),
            onClick = { offset ->
                pdpaAnnotatedString.getStringAnnotations("URL", offset, offset)
                    .firstOrNull()?.let { annotation -> uriHandler.openUri(annotation.item) }
            }
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
        Checkbox(
            checked = state.acceptedVaccineInfo,
            onCheckedChange = viewModel::onAcceptedVaccineInfoChange,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = strings.vaccineInfoConsentText,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}
