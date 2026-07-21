package kmch.vaccine.form.domain.usecase

import kmch.vaccine.form.domain.model.ScreeningQuestion
import kmch.vaccine.form.domain.repository.RegistrationRepository

class GetScreeningQuestionsUseCase(private val repository: RegistrationRepository) {
    suspend operator fun invoke(): Result<List<ScreeningQuestion>> =
        repository.getScreeningQuestions().map { it.sortedBy(ScreeningQuestion::displayOrder) }
}
