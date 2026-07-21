package kmch.vaccine.form.data.repository

import kmch.vaccine.form.data.mapper.toDomain
import kmch.vaccine.form.data.remote.VaccineApiService
import kmch.vaccine.form.data.remote.dto.LotCreateRequestDto
import kmch.vaccine.form.data.remote.dto.VaccineCreateRequestDto
import kmch.vaccine.form.data.remote.runCatchingDomain
import kmch.vaccine.form.domain.model.LotInfo
import kmch.vaccine.form.domain.model.VaccineInfo
import kmch.vaccine.form.domain.repository.VaccineInventoryRepository

class VaccineInventoryRepositoryImpl(
    private val api: VaccineApiService
) : VaccineInventoryRepository {

    override suspend fun createVaccine(name: String): Result<VaccineInfo> = runCatchingDomain {
        api.createVaccine(VaccineCreateRequestDto(vaccineName = name)).toDomain()
    }

    override suspend fun createLot(vaccineId: Int, lotNumber: String, initialQuantity: Int): Result<LotInfo> =
        runCatchingDomain {
            api.createLot(
                vaccineId = vaccineId,
                request = LotCreateRequestDto(lotNumber = lotNumber, initialQuantity = initialQuantity)
            ).toDomain()
        }
}
