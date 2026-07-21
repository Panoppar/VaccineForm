package kmch.vaccine.form.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class DocumentType {
    @SerialName("id_card") ID_CARD,
    @SerialName("passport") PASSPORT
}
