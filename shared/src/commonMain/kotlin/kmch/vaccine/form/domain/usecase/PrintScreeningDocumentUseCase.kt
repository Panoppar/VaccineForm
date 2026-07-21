package kmch.vaccine.form.domain.usecase

import kmch.vaccine.form.domain.model.RegistrationDetail
import kmch.vaccine.form.domain.service.ScreeningDocumentPrinter

class PrintScreeningDocumentUseCase(private val printer: ScreeningDocumentPrinter) {
    operator fun invoke(detail: RegistrationDetail) = printer.print(detail)
}
