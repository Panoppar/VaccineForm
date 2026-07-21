package kmch.vaccine.form.presentation.screen.admin.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kmch.vaccine.form.presentation.localization.LocalStrings

private enum class AddMode { VACCINE, LOT }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVaccineOrLotDialog(
    onDismiss: () -> Unit,
    onSaveVaccine: (vaccineName: String) -> Unit,
    onSaveLot: (vaccineId: String, lotNumber: String, initialQty: Int) -> Unit
) {
    val strings = LocalStrings.current
    var selectedMode by remember { mutableStateOf(AddMode.VACCINE) }

    var vaccineName by remember { mutableStateOf("") }
    var vaccineId by remember { mutableStateOf("") }
    var lotNumber by remember { mutableStateOf("") }
    var initialQuantity by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = strings.manageVaccineDataTitle, style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedMode == AddMode.VACCINE,
                            onClick = { selectedMode = AddMode.VACCINE }
                        )
                        Text(strings.addNewVaccineOption, style = MaterialTheme.typography.bodyMedium)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedMode == AddMode.LOT,
                            onClick = { selectedMode = AddMode.LOT }
                        )
                        Text(strings.addNewLotOption, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                if (selectedMode == AddMode.VACCINE) {
                    OutlinedTextField(
                        value = vaccineName,
                        onValueChange = { vaccineName = it },
                        label = { Text(strings.vaccineNameLabel) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                } else {
                    OutlinedTextField(
                        value = vaccineId,
                        onValueChange = { newId -> vaccineId = newId.filter { it.isDigit() } },
                        label = { Text(strings.vaccineIdRefLabel) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = lotNumber,
                        onValueChange = { lotNumber = it },
                        label = { Text(strings.lotNumberLabel) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = initialQuantity,
                        onValueChange = { newQty -> initialQuantity = newQty.filter { it.isDigit() } },
                        label = { Text(strings.initialQuantityLabel) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedMode == AddMode.VACCINE) {
                        onSaveVaccine(vaccineName)
                    } else {
                        val qty = initialQuantity.toIntOrNull() ?: 0
                        onSaveLot(vaccineId, lotNumber, qty)
                    }
                },
                enabled = if (selectedMode == AddMode.VACCINE) {
                    vaccineName.isNotBlank()
                } else {
                    vaccineId.isNotBlank() && lotNumber.isNotBlank() && initialQuantity.isNotBlank()
                }
            ) {
                Text(strings.save)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(strings.cancel)
            }
        }
    )
}
