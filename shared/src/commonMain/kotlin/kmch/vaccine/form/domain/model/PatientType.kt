package kmch.vaccine.form.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PatientType {
    @SerialName("student") STUDENT,
    @SerialName("employee") EMPLOYEE,
    @SerialName("company") COMPANY,
    @SerialName("external") EXTERNAL
}
