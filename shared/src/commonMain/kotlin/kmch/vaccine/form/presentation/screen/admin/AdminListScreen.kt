package kmch.vaccine.form.presentation.screen.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kmch.vaccine.form.presentation.localization.LocalStrings
import kmch.vaccine.form.presentation.localization.dialogAlertMessage
import kmch.vaccine.form.presentation.screen.admin.component.AddVaccineOrLotDialog
import kmch.vaccine.form.presentation.viewmodel.AdminListViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminListScreen(
    onPatientSelected: (Int) -> Unit,
    onCreateNew: () -> Unit,
    viewModel: AdminListViewModel = koinViewModel()
) {
    val strings = LocalStrings.current
    val state by viewModel.uiState.collectAsState()

    state.dialogAlert?.let { alert ->
        AlertDialog(
            onDismissRequest = viewModel::dismissDialogAlert,
            confirmButton = { TextButton(onClick = viewModel::dismissDialogAlert) { Text(strings.ok) } },
            title = { Text(strings.notice) },
            text = { Text(strings.dialogAlertMessage(alert)) }
        )
    }

    if (state.showAddVaccineDialog) {
        AddVaccineOrLotDialog(
            onDismiss = { viewModel.onShowAddVaccineDialogChange(false) },
            onSaveVaccine = viewModel::createVaccine,
            onSaveLot = { vaccineId, lotNumber, qty -> viewModel.createLot(vaccineId, lotNumber, qty) }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(strings.adminDashboardTitle, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = state.lotInput,
                    onValueChange = viewModel::onLotInputChange,
                    label = { Text(strings.lotIdLabel) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(140.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                FloatingActionButton(
                    onClick = { viewModel.onShowAddVaccineDialogChange(true) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = strings.addVaccineOrLotContentDescription)
                }
            }

            OutlinedTextField(
                value = state.employeeRateId,
                onValueChange = viewModel::onEmployeeRateIdChange,
                label = { Text(strings.employeeRateIdLabel) },
                modifier = Modifier.width(180.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    label = { Text(strings.searchPatientsLabel) },
                    modifier = Modifier.width(180.dp).weight(1f)
                )

                FloatingActionButton(
                    onClick = onCreateNew,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = strings.createNewScreeningContentDescription)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.registrations.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = strings.noPatientsFoundMessage(state.searchQuery),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.registrations) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onPatientSelected(item.patientId) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = strings.patientListIdLine(item.patientId),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = strings.patientListDateLine(item.shotDate.toString()),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                strings.patientListNameLine("${item.prefix}${item.firstName} ${item.lastName}"),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                strings.patientListPhoneLine(item.telNo),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
