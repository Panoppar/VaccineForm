package kmch.vaccine.form.presentation.screen.admin.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kmch.vaccine.form.domain.model.RegistrationDetail
import kmch.vaccine.form.presentation.localization.LocalStrings

@Composable
fun PatientInfoHeader(detail: RegistrationDetail) {
    val strings = LocalStrings.current

    Column {
        Text(
            text = strings.patientHeaderName("${detail.prefix}${detail.firstName} ${detail.lastName}"),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = strings.patientHeaderId(detail.patientId),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = strings.patientHeaderPhone(detail.telNo),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (!detail.underlyingDisease.isNullOrBlank()) {
            Text(
                text = strings.patientHeaderUnderlyingDisease(detail.underlyingDisease),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = strings.vaccinationDateChip(detail.shotDate.toString(), detail.vaccineName, detail.lotNumber),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}
