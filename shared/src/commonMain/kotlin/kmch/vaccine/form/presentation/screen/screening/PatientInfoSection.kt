package kmch.vaccine.form.presentation.screen.screening

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import kmch.vaccine.form.domain.model.DocumentType
import kmch.vaccine.form.domain.model.PatientType
import kmch.vaccine.form.domain.model.Prefix
import kmch.vaccine.form.domain.model.Sex
import kmch.vaccine.form.presentation.localization.LocalStrings
import kmch.vaccine.form.presentation.viewmodel.ScreeningFormUiState
import kmch.vaccine.form.presentation.viewmodel.ScreeningFormViewModel
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientInfoSection(state: ScreeningFormUiState, viewModel: ScreeningFormViewModel) {
    val strings = LocalStrings.current

    var expandedPrefix by remember { mutableStateOf(false) }
    var expandedPatientType by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    Text(strings.patientInfoSectionTitle, style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(16.dp))

    // คำนำหน้า (Dropdown)
    ExposedDropdownMenuBox(
        expanded = expandedPrefix,
        onExpandedChange = { expandedPrefix = !expandedPrefix }
    ) {
        OutlinedTextField(
            value = state.prefix?.let { strings.prefixOptionLabel(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(strings.prefixLabel) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPrefix) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expandedPrefix,
            onDismissRequest = { expandedPrefix = false }
        ) {
            Prefix.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(strings.prefixOptionLabel(option)) },
                    onClick = {
                        viewModel.onPrefixChange(option)
                        expandedPrefix = false
                    }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = state.firstName,
        onValueChange = viewModel::onFirstNameChange,
        label = { Text(strings.firstNameLabel) },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = state.lastName,
        onValueChange = viewModel::onLastNameChange,
        label = { Text(strings.lastNameLabel) },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16.dp))

    // เพศ
    Text(strings.sexLabel, style = MaterialTheme.typography.bodyMedium)
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth().selectableGroup()
    ) {
        Sex.entries.forEach { sexOption ->
            val isSelected = state.sex == sexOption
            Button(
                onClick = { viewModel.onSexChange(sexOption) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.weight(1f).semantics {
                    role = Role.RadioButton
                    selected = isSelected
                }
            ) { Text(strings.sexOptionLabel(sexOption)) }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))

    // ประเภทผู้มารับบริการ
    Text(strings.patientTypeLabel, style = MaterialTheme.typography.bodyMedium)
    Spacer(modifier = Modifier.height(8.dp))
    ExposedDropdownMenuBox(
        expanded = expandedPatientType,
        onExpandedChange = { expandedPatientType = !expandedPatientType }
    ) {
        OutlinedTextField(
            value = state.patientType?.let { strings.patientTypeOptionLabel(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(strings.patientTypeSelectLabel) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPatientType) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expandedPatientType,
            onDismissRequest = { expandedPatientType = false }
        ) {
            PatientType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(strings.patientTypeOptionLabel(type)) },
                    onClick = {
                        viewModel.onPatientTypeChange(type)
                        expandedPatientType = false
                    }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))

    // เอกสารยืนยันตัวตน
    Text(strings.documentTypeLabel, style = MaterialTheme.typography.bodyMedium)
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth().selectableGroup()
    ) {
        DocumentType.entries.forEach { docType ->
            val isSelected = state.documentType == docType
            Button(
                onClick = { viewModel.onDocumentTypeChange(docType) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.weight(1f).semantics {
                    role = Role.RadioButton
                    selected = isSelected
                }
            ) { Text(strings.documentTypeOptionLabel(docType)) }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))

    if (state.documentType == DocumentType.ID_CARD) {
        OutlinedTextField(
            value = state.idCard,
            onValueChange = viewModel::onIdCardChange,
            label = { Text(strings.idCardNumberLabel) },
            isError = state.idCard.isNotEmpty() && state.idCard.length != 13,
            supportingText = {
                if (state.idCard.isNotEmpty() && state.idCard.length != 13) {
                    Text(
                        strings.idCardDigitError(state.idCard.length),
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        OutlinedTextField(
            value = state.passportId,
            onValueChange = viewModel::onPassportIdChange,
            label = { Text(strings.passportNumberLabel) },
            modifier = Modifier.fillMaxWidth()
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = state.age,
        onValueChange = viewModel::onAgeChange,
        label = { Text(strings.ageLabel) },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = state.telNo,
        onValueChange = viewModel::onTelNoChange,
        label = { Text(strings.phoneNumberLabel) },
        isError = state.telNo.isNotEmpty() && state.telNo.length != 10,
        supportingText = {
            if (state.telNo.isNotEmpty() && state.telNo.length != 10) {
                Text(
                    strings.phoneDigitError(state.telNo.length),
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(12.dp))

    // วันที่รับวัคซีน
    val datePickerState = rememberDatePickerState(
        selectableDates = remember {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val todayStartMillis = (kotlin.time.Clock.System.now().toEpochMilliseconds() / 86_400_000L) * 86_400_000L
                    return utcTimeMillis >= todayStartMillis
                }
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val days = (millis / 86400000).toInt()
                        viewModel.onShotDateChange(LocalDate.fromEpochDays(days).toString())
                    }
                    showDatePicker = false
                }) { Text(strings.ok) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text(strings.cancel) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Box(modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }) {
        OutlinedTextField(
            value = state.shotDate,
            onValueChange = {},
            label = { Text(strings.vaccinationDateLabel) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            enabled = false,
            trailingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = strings.selectDateContentDescription)
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = state.underlyingDisease,
        onValueChange = viewModel::onUnderlyingDiseaseChange,
        label = { Text(strings.underlyingDiseaseLabel) },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = state.address,
        onValueChange = viewModel::onAddressChange,
        label = { Text(strings.addressLabel) },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = state.zipCode,
        onValueChange = viewModel::onZipCodeChange,
        label = { Text(strings.zipCodeLabel) },
        modifier = Modifier.fillMaxWidth()
    )
}
