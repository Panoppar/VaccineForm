package kmch.vaccine.form.data.mapper

import kmch.vaccine.form.data.remote.dto.RegistrationAnswerDetailDto
import kmch.vaccine.form.data.remote.dto.RegistrationAnswerRequestDto
import kmch.vaccine.form.data.remote.dto.RegistrationDetailDto
import kmch.vaccine.form.data.remote.dto.RegistrationListItemDto
import kmch.vaccine.form.data.remote.dto.RegistrationListResponseDto
import kmch.vaccine.form.data.remote.dto.RegistrationRequestDto
import kmch.vaccine.form.data.remote.dto.RegistrationResponseDto
import kmch.vaccine.form.data.remote.dto.ScreeningQuestionDto
import kmch.vaccine.form.domain.model.RegistrationAnswerDetail
import kmch.vaccine.form.domain.model.RegistrationConfirmation
import kmch.vaccine.form.domain.model.RegistrationDetail
import kmch.vaccine.form.domain.model.RegistrationPage
import kmch.vaccine.form.domain.model.RegistrationSubmission
import kmch.vaccine.form.domain.model.RegistrationSummary
import kmch.vaccine.form.domain.model.ScreeningAnswer
import kmch.vaccine.form.domain.model.ScreeningQuestion

fun ScreeningQuestionDto.toDomain() = ScreeningQuestion(
    questionId = questionId,
    questionText = questionText,
    displayOrder = displayOrder
)

fun ScreeningAnswer.toRequestDto() = RegistrationAnswerRequestDto(
    questionId = questionId,
    answer = answer,
    remark = remark
)

fun RegistrationSubmission.toRequestDto() = RegistrationRequestDto(
    prefix = prefix,
    firstName = firstName,
    lastName = lastName,
    sex = sex,
    patientType = patientType,
    documentType = documentType,
    idCard = idCard,
    passportId = passportId,
    age = age,
    telNo = telNo,
    underlyingDisease = underlyingDisease,
    address = address,
    zipCode = zipCode,
    shotDate = shotDate,
    answers = answers.map { it.toRequestDto() }
)

fun RegistrationResponseDto.toDomain() = RegistrationConfirmation(
    patientId = patientId,
    screeningRecordId = screeningRecordId,
    vaccinationId = vaccinationId
)

fun RegistrationListItemDto.toDomain() = RegistrationSummary(
    patientId = patientId,
    prefix = prefix,
    firstName = firstName,
    lastName = lastName,
    documentType = documentType,
    idCard = idCard,
    passportId = passportId,
    telNo = telNo,
    patientType = patientType,
    shotDate = shotDate
)

fun RegistrationListResponseDto.toDomain() = RegistrationPage(
    total = total,
    page = page,
    limit = limit,
    items = items.map { it.toDomain() }
)

fun RegistrationAnswerDetailDto.toDomain() = RegistrationAnswerDetail(
    questionId = questionId,
    questionText = questionText,
    answer = answer,
    remark = remark
)

fun RegistrationDetailDto.toDomain() = RegistrationDetail(
    patientId = patientId,
    prefix = prefix,
    firstName = firstName,
    lastName = lastName,
    sex = sex,
    patientType = patientType,
    documentType = documentType,
    idCard = idCard,
    passportId = passportId,
    age = age,
    telNo = telNo,
    underlyingDisease = underlyingDisease,
    address = address,
    zipCode = zipCode,
    shotDate = shotDate,
    vaccineId = vaccineId,
    vaccineName = vaccineName,
    lotId = lotId,
    lotNumber = lotNumber,
    answers = answers.map { it.toDomain() }
)
