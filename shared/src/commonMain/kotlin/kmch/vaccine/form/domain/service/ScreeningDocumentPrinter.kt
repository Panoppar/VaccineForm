package kmch.vaccine.form.domain.service

import kmch.vaccine.form.domain.model.RegistrationDetail

interface ScreeningDocumentPrinter {
    fun print(detail: RegistrationDetail)
}
