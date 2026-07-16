//package kmch.vaccine.form
//
//import kmch.vaccine.form.util.InstantSerializer
//import kotlinx.serialization.KSerializer
//import kotlinx.serialization.Serializable
//import kotlin.time.Instant
//import kotlinx.serialization.descriptors.PrimitiveKind
//import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
//import kotlinx.serialization.descriptors.SerialDescriptor
//import kotlinx.serialization.encoding.Decoder
//import kotlinx.serialization.encoding.Encoder
//
//// Object สำหรับจัดการแปลงค่า String วันเวลาจาก Golang เป็น Instant ของ Kotlin
//object InstantSerializer : KSerializer<Instant> {
//    override val descriptor: SerialDescriptor =
//        PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)
//
//    override fun serialize(encoder: Encoder, value: Instant) {
//        // เวลาส่งกลับไปให้ Golang จะแปลงเป็น String
//        encoder.encodeString(value.toString())
//    }
//
//    override fun deserialize(decoder: Decoder): Instant {
//        // เวลารับ JSON จาก Golang จะแปลง String เป็น Instant
//        return Instant.parse(decoder.decodeString())
//    }
//}
//
//@Serializable
//enum class Sex {
//    MALE, FEMALE
//}
//
//@Serializable
//enum class Prefix {
//    MR, MRS, MS
//}
//
//@Serializable
//enum class PatientType {
//    STUDENT, EMPLOYEE, COMPANY, EXTERNAL
//}
//
//@Serializable
//enum class DocumentType {
//    ID, PASSPORT
//}
//
//@Serializable
//data class Patient(
//    val patientId: Int? = null,
//    val documentType: DocumentType, // NN
//    val idCard: String? = null,
//    val passportId: String? = null,
//    val lineId: String? = null,
//    val patientType: PatientType, // NN
//    val prefix: Prefix? = null,
//    val firstName: String, // NN
//    val lastName: String, // NN
//    val telNo: String, // NN
//    val email: String? = null,
//    val underlyingDisease: String? = null,
//    val age: Int? = null,
//    val address: String? = null,
//    val zipCode: String? = null,
//
//    // Audit Fields
//    @Serializable(with = InstantSerializer::class)
//    val createdAt: Instant, // NN
//    val createdBy: String, // NN
//    @Serializable(with = InstantSerializer::class)
//    val updatedAt: Instant? = null,
//    val updatedBy: String? = null,
//    @Serializable(with = InstantSerializer::class)
//    val deletedAt: Instant? = null,
//    val deletedBy: String? = null
//)
//
//@Serializable
//data class Vaccine(
//    val vaccineId: Int? = null,
//    val vaccineName: String, // NN
//
//    @Serializable(with = InstantSerializer::class)
//    val createdAt: Instant, // NN
//    val createdBy: String, // NN
//    @Serializable(with = InstantSerializer::class)
//    val updatedAt: Instant? = null,
//    val updatedBy: String? = null,
//    @Serializable(with = InstantSerializer::class)
//    val deletedAt: Instant? = null,
//    val deletedBy: String? = null
//)
//
//@Serializable
//data class VaccineLot(
//    val lotId: Int? = null,
//    val vaccineId: Int, // NN
//    val lotNumber: String, // NN
//    val initialQuantity: Int, // NN
//    val remainingQuantity: Int, // NN
//
//    @Serializable(with = InstantSerializer::class)
//    val createdAt: Instant, // NN
//    val createdBy: String, // NN
//    @Serializable(with = InstantSerializer::class)
//    val updatedAt: Instant? = null,
//    val updatedBy: String? = null,
//    @Serializable(with = InstantSerializer::class)
//    val deletedAt: Instant? = null,
//    val deletedBy: String? = null
//)
//
//@Serializable
//data class VaccinationRecord(
//    val vaccinationId: Int? = null,
//    val patientId: Int, // NN
//    val lotId: Int, // NN
//    @Serializable(with = InstantSerializer::class)
//    val shotDate: Instant, // NN
//
//    @Serializable(with = InstantSerializer::class)
//    val createdAt: Instant, // NN
//    val createdBy: String, // NN
//    @Serializable(with = InstantSerializer::class)
//    val updatedAt: Instant? = null,
//    val updatedBy: String? = null,
//    @Serializable(with = InstantSerializer::class)
//    val deletedAt: Instant? = null,
//    val deletedBy: String? = null
//)
//
//@Serializable
//data class ScreeningQuestion(
//    val questionId: Int? = null,
//    val questionText: String, // NN
//    val active: Boolean, // NN
//    val displayOrder: Int, // NN
//
//    @Serializable(with = InstantSerializer::class)
//    val createdAt: Instant, // NN
//    val createdBy: String, // NN
//    @Serializable(with = InstantSerializer::class)
//    val updatedAt: Instant? = null,
//    val updatedBy: String? = null,
//    @Serializable(with = InstantSerializer::class)
//    val deletedAt: Instant? = null,
//    val deletedBy: String? = null
//)
//
//@Serializable
//data class ScreeningRecord(
//    val recordId: Int? = null,
//    val patientId: Int, // NN
//
//    @Serializable(with = InstantSerializer::class)
//    val createdAt: Instant, // NN
//    val createdBy: String, // NN
//    @Serializable(with = InstantSerializer::class)
//    val updatedAt: Instant? = null,
//    val updatedBy: String? = null,
//    @Serializable(with = InstantSerializer::class)
//    val deletedAt: Instant? = null,
//    val deletedBy: String? = null
//)
//
//@Serializable
//data class ScreeningAnswer(
//    val recordId: Int,
//    val questionId: Int,
//    val answer: Boolean, // NN
//    val remark: String? = null,
//
//    @Serializable(with = InstantSerializer::class)
//    val createdAt: Instant, // NN
//    val createdBy: String, // NN
//    @Serializable(with = InstantSerializer::class)
//    val updatedAt: Instant? = null,
//    val updatedBy: String? = null,
//    @Serializable(with = InstantSerializer::class)
//    val deletedAt: Instant? = null,
//    val deletedBy: String? = null
//)
//
//@Serializable
//data class Student(
//    val studentId: Int? = null,
//    val patientId: Int, // NN
//
//    @Serializable(with = InstantSerializer::class)
//    val createdAt: Instant, // NN
//    val createdBy: String, // NN
//    @Serializable(with = InstantSerializer::class)
//    val updatedAt: Instant? = null,
//    val updatedBy: String? = null,
//    @Serializable(with = InstantSerializer::class)
//    val deletedAt: Instant? = null,
//    val deletedBy: String? = null
//)
//
//@Serializable
//data class Employee(
//    val employeeId: String? = null,
//    val patientId: Int, // NN
//
//    @Serializable(with = InstantSerializer::class)
//    val createdAt: Instant, // NN
//    val createdBy: String, // NN
//    @Serializable(with = InstantSerializer::class)
//    val updatedAt: Instant? = null,
//    val updatedBy: String? = null,
//    @Serializable(with = InstantSerializer::class)
//    val deletedAt: Instant? = null,
//    val deletedBy: String? = null
//)
//
//@Serializable
//data class Company(
//    val companyId: Int? = null,
//    val companyName: String, // NN
//
//    @Serializable(with = InstantSerializer::class)
//    val createdAt: Instant, // NN
//    val createdBy: String, // NN
//    @Serializable(with = InstantSerializer::class)
//    val updatedAt: Instant? = null,
//    val updatedBy: String? = null,
//    @Serializable(with = InstantSerializer::class)
//    val deletedAt: Instant? = null,
//    val deletedBy: String? = null
//)
//
//@Serializable
//data class CompanyEmployee(
//    val employeeId: String? = null,
//    val companyId: Int, // NN
//    val patientId: Int, // NN
//
//    @Serializable(with = InstantSerializer::class)
//    val createdAt: Instant, // NN
//    val createdBy: String, // NN
//    @Serializable(with = InstantSerializer::class)
//    val updatedAt: Instant? = null,
//    val updatedBy: String? = null,
//    @Serializable(with = InstantSerializer::class)
//    val deletedAt: Instant? = null,
//    val deletedBy: String? = null
//)
//
//@Serializable
//data class ExternalPerson(
//    val externalId: Int? = null,
//    val patientId: Int, // NN
//
//    @Serializable(with = InstantSerializer::class)
//    val createdAt: Instant, // NN
//    val createdBy: String, // NN
//    @Serializable(with = InstantSerializer::class)
//    val updatedAt: Instant? = null,
//    val updatedBy: String? = null,
//    @Serializable(with = InstantSerializer::class)
//    val deletedAt: Instant? = null,
//    val deletedBy: String? = null
//)
//
//@Serializable
//data class SystemAuditLog(
//    val auditId: Int? = null,
//    val tableName: String, // NN
//    val recordId: Int, // NN
//    val actionType: String, // NN
//    val oldValues: String? = null, // JSON String
//    val newValues: String? = null, // JSON String
//    val changedBy: String, // NN
//    @Serializable(with = InstantSerializer::class)
//    val changedAt: Instant // NN
//)
//
