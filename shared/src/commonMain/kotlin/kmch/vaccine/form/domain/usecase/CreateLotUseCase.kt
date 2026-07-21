package kmch.vaccine.form.domain.usecase

import kmch.vaccine.form.domain.model.LotInfo
import kmch.vaccine.form.domain.repository.VaccineInventoryRepository

class CreateLotUseCase(private val repository: VaccineInventoryRepository) {
    suspend operator fun invoke(vaccineId: Int, lotNumber: String, initialQuantity: Int): Result<LotInfo> =
        repository.createLot(vaccineId, lotNumber, initialQuantity)
}
