package kmch.vaccine.form.domain.usecase

import kmch.vaccine.form.domain.model.VaccineInfo
import kmch.vaccine.form.domain.repository.VaccineInventoryRepository

class CreateVaccineUseCase(private val repository: VaccineInventoryRepository) {
    suspend operator fun invoke(name: String): Result<VaccineInfo> = repository.createVaccine(name)
}
