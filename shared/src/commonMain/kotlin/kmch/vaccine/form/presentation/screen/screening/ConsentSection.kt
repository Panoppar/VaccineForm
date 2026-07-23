package kmch.vaccine.form.presentation.screen.screening

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import kmch.vaccine.form.presentation.localization.LocalStrings
import kmch.vaccine.form.presentation.viewmodel.ScreeningFormUiState
import kmch.vaccine.form.presentation.viewmodel.ScreeningFormViewModel

private const val PDPA_URL = "https://pdpa.kmitl.ac.th"

@Composable
fun ConsentSection(state: ScreeningFormUiState, viewModel: ScreeningFormViewModel) {
    val strings = LocalStrings.current

    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth().toggleable(
            value = state.acceptedTerms,
            onValueChange = viewModel::onAcceptedTermsChange,
            role = Role.Checkbox
        )
    ) {
        Checkbox(
            checked = state.acceptedTerms,
            onCheckedChange = null,
            modifier = Modifier.padding(end = 8.dp)
        )

        // LinkAnnotation.Url (replacing the deprecated ClickableText+offset-detection
        // approach) gives the "pdpa.kmitl.ac.th" span real keyboard focus and
        // screen-reader affordance instead of only working for mouse clicks.
        val pdpaAnnotatedString = buildAnnotatedString {
            append(strings.pdpaConsentText)
            withLink(
                LinkAnnotation.Url(
                    url = PDPA_URL,
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                )
            ) {
                append(strings.pdpaLinkLabel)
            }
        }

        Text(
            text = pdpaAnnotatedString,
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.padding(top = 12.dp)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth().toggleable(
            value = state.acceptedVaccineInfo,
            onValueChange = viewModel::onAcceptedVaccineInfoChange,
            role = Role.Checkbox
        )
    ) {
        Checkbox(
            checked = state.acceptedVaccineInfo,
            onCheckedChange = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = strings.vaccineInfoConsentText,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}
