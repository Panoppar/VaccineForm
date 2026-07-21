package kmch.vaccine.form.data.print

import kmch.vaccine.form.domain.model.RegistrationDetail
import kmch.vaccine.form.domain.service.ScreeningDocumentPrinter
import kmch.vaccine.form.util.PrintAnswerItem
import kmch.vaccine.form.util.PrintPatientInfo
import kmch.vaccine.form.util.printVaccineDocument

class ScreeningDocumentPrinterImpl : ScreeningDocumentPrinter {

    override fun print(detail: RegistrationDetail) {
        val printItems = detail.answers.mapIndexed { index, answer ->
            PrintAnswerItem(
                order = index + 1,
                question = answer.questionText,
                isYes = answer.answer,
                remark = answer.remark
            )
        }
        printVaccineDocument(
            patient = PrintPatientInfo(
                prefix = detail.prefix,
                firstName = detail.firstName,
                lastName = detail.lastName,
                idCard = detail.idCard,
                passportId = detail.passportId,
                underlyingDisease = detail.underlyingDisease,
                address = detail.address,
                telNo = detail.telNo,
                shotDate = detail.shotDate.toString()
            ),
            answers = printItems
        )
    }
}
