package kmch.vaccine.form.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Sex {
    @SerialName("male") MALE,
    @SerialName("female") FEMALE
}
