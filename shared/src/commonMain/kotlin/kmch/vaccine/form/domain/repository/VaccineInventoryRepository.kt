package kmch.vaccine.form.domain.repository

import kmch.vaccine.form.domain.model.LotInfo
import kmch.vaccine.form.domain.model.VaccineInfo

interface VaccineInventoryRepository {
    suspend fun createVaccine(name: String): Result<VaccineInfo>
    suspend fun createLot(vaccineId: Int, lotNumber: String, initialQuantity: Int): Result<LotInfo>
}
