package kmch.vaccine.form

import kmch.vaccine.form.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
enum class Sex {
    MALE, FEMALE
}

@Serializable
enum class Prefix {
    MR, MRS, MS
}

@Serializable
enum class PatientType {
    STUDENT, EMPLOYEE, COMPANY, EXTERNAL
}

@Serializable
enum class DocumentType {
    ID, PASSPORT
}

@Serializable
data class Patient(
    val patientId: Int? = null,
    val documentType: DocumentType, // NN
    val idCard: String? = null,
    val passportId: String? = null,
    val lineId: String? = null,
    val patientType: PatientType, // NN
    val prefix: Prefix? = null,
    val firstName: String, // NN
    val lastName: String, // NN
    val telNo: String, // NN
    val email: String? = null,
    val underlyingDisease: String? = null,
    val age: Int? = null,
    val address: String? = null,
    val zipCode: String? = null,

    // Audit Fields
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant, // NN
    val createdBy: String, // NN
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
    val updatedBy: String? = null,
    @Serializable(with = InstantSerializer::class)
    val deletedAt: Instant? = null,
    val deletedBy: String? = null
)

@Serializable
data class Vaccine(
    val vaccineId: Int? = null,
    val vaccineName: String, // NN

    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant, // NN
    val createdBy: String, // NN
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
    val updatedBy: String? = null,
    @Serializable(with = InstantSerializer::class)
    val deletedAt: Instant? = null,
    val deletedBy: String? = null
)

@Serializable
data class VaccineLot(
    val lotId: Int? = null,
    val vaccineId: Int, // NN
    val lotNumber: String, // NN
    val initialQuantity: Int, // NN
    val remainingQuantity: Int, // NN

    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant, // NN
    val createdBy: String, // NN
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
    val updatedBy: String? = null,
    @Serializable(with = InstantSerializer::class)
    val deletedAt: Instant? = null,
    val deletedBy: String? = null
)

@Serializable
data class VaccinationRecord(
    val vaccinationId: Int? = null,
    val patientId: Int, // NN
    val lotId: Int, // NN
    @Serializable(with = InstantSerializer::class)
    val shotDate: Instant, // NN

    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant, // NN
    val createdBy: String, // NN
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
    val updatedBy: String? = null,
    @Serializable(with = InstantSerializer::class)
    val deletedAt: Instant? = null,
    val deletedBy: String? = null
)

@Serializable
data class ScreeningQuestion(
    val questionId: Int? = null,
    val questionText: String, // NN
    val active: Boolean, // NN
    val displayOrder: Int, // NN

    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant, // NN
    val createdBy: String, // NN
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
    val updatedBy: String? = null,
    @Serializable(with = InstantSerializer::class)
    val deletedAt: Instant? = null,
    val deletedBy: String? = null
)

@Serializable
data class ScreeningRecord(
    val recordId: Int? = null,
    val patientId: Int, // NN

    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant, // NN
    val createdBy: String, // NN
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
    val updatedBy: String? = null,
    @Serializable(with = InstantSerializer::class)
    val deletedAt: Instant? = null,
    val deletedBy: String? = null
)

@Serializable
data class ScreeningAnswer(
    val recordId: Int,
    val questionId: Int,
    val answer: Boolean, // NN
    val remark: String? = null,

    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant, // NN
    val createdBy: String, // NN
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
    val updatedBy: String? = null,
    @Serializable(with = InstantSerializer::class)
    val deletedAt: Instant? = null,
    val deletedBy: String? = null
)

@Serializable
data class Student(
    val studentId: Int? = null,
    val patientId: Int, // NN

    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant, // NN
    val createdBy: String, // NN
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
    val updatedBy: String? = null,
    @Serializable(with = InstantSerializer::class)
    val deletedAt: Instant? = null,
    val deletedBy: String? = null
)

@Serializable
data class Employee(
    val employeeId: String? = null,
    val patientId: Int, // NN

    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant, // NN
    val createdBy: String, // NN
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
    val updatedBy: String? = null,
    @Serializable(with = InstantSerializer::class)
    val deletedAt: Instant? = null,
    val deletedBy: String? = null
)

@Serializable
data class Company(
    val companyId: Int? = null,
    val companyName: String, // NN

    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant, // NN
    val createdBy: String, // NN
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
    val updatedBy: String? = null,
    @Serializable(with = InstantSerializer::class)
    val deletedAt: Instant? = null,
    val deletedBy: String? = null
)

@Serializable
data class CompanyEmployee(
    val employeeId: String? = null,
    val companyId: Int, // NN
    val patientId: Int, // NN

    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant, // NN
    val createdBy: String, // NN
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
    val updatedBy: String? = null,
    @Serializable(with = InstantSerializer::class)
    val deletedAt: Instant? = null,
    val deletedBy: String? = null
)

@Serializable
data class ExternalPerson(
    val externalId: Int? = null,
    val patientId: Int, // NN

    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant, // NN
    val createdBy: String, // NN
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
    val updatedBy: String? = null,
    @Serializable(with = InstantSerializer::class)
    val deletedAt: Instant? = null,
    val deletedBy: String? = null
)

@Serializable
data class SystemAuditLog(
    val auditId: Int? = null,
    val tableName: String, // NN
    val recordId: Int, // NN
    val actionType: String, // NN
    val oldValues: String? = null, // JSON String
    val newValues: String? = null, // JSON String
    val changedBy: String, // NN
    @Serializable(with = InstantSerializer::class)
    val changedAt: Instant // NN
)

@Serializable
data class ScreeningSubmissionRequest(
    // 1. ข้อมูลผู้ป่วย:
    // - ถ้าเป็นคนไข้เก่า ส่งมาแค่ patientId
    // - ถ้าเป็นคนไข้ใหม่ ส่งข้อมูล Patient มาทั้งก้อน (patientId เป็น null)
    val patientId: Int? = null,
    val patient: Patient? = null,

    // 2. ข้อมูลผู้บันทึก (เลขอัตราเจ้าหน้าที่ที่กรอกให้ หรือถ้าผู้ป่วยกรอกเองอาจจะเป็น "SELF")
    val createdBy: String,

    // 3. รายการคำตอบทั้งหมด
    val answers: List<ScreeningAnswerRequest>
)

@Serializable
data class ScreeningAnswerRequest(
    val questionId: Int,
    val answer: Boolean, // true = มี/ใช่, false = ไม่มี/ไม่ใช่
    val remark: String? = null // สำหรับเก็บข้อความจากคำถามประเภท [Text] หรือหมายเหตุเพิ่มเติม
)

//val mockPatients = listOf(
//    // 1. นักศึกษา (STUDENT) ข้อมูลครบถ้วนพื้นฐาน
//    Patient(
//        patientId = 1001,
//        documentType = DocumentType.ID,
//        idCard = "1100012345678",
//        lineId = "somchai_std",
//        patientType = PatientType.STUDENT,
//        prefix = Prefix.MR,
//        firstName = "Somchai",
//        lastName = "Jaidee",
//        telNo = "0812345678",
//        email = "somchai.j@university.edu",
//        age = 20,
//        address = "123 หอพักนักศึกษา ถ.พหลโยธิน กรุงเทพมหานคร",
//        zipCode = "10900",
//        createdAt = Instant.parse("2026-07-01T08:30:00Z"),
//        createdBy = "Admin_A"
//    ),
//
//    // 2. พนักงาน (EMPLOYEE) มีโรคประจำตัว และเคยถูกอัปเดตข้อมูล
//    Patient(
//        patientId = 1002,
//        documentType = DocumentType.ID,
//        idCard = "3100087654321",
//        patientType = PatientType.EMPLOYEE,
//        prefix = Prefix.MS,
//        firstName = "Suda",
//        lastName = "Rakngan",
//        telNo = "0898765432",
//        email = "suda.r@company.com",
//        underlyingDisease = "โรคหอบหืด",
//        age = 32,
//        zipCode = "10250",
//        createdAt = Instant.parse("2026-06-15T10:00:00Z"),
//        createdBy = "Admin_B",
//        updatedAt = Instant.parse("2026-07-10T14:20:00Z"),
//        updatedBy = "Admin_A"
//    ),
//
//    // 3. บุคคลภายนอก (EXTERNAL) ใช้ Passport
//    Patient(
//        patientId = 1003,
//        documentType = DocumentType.PASSPORT,
//        passportId = "A12345678",
//        patientType = PatientType.EXTERNAL,
//        prefix = Prefix.MR,
//        firstName = "John",
//        lastName = "Smith",
//        telNo = "0911112222",
//        email = "john.smith@gmail.com",
//        age = 45,
//        address = "456 Sukhumvit Rd, Bangkok",
//        createdAt = Instant.parse("2026-07-12T09:15:00Z"),
//        createdBy = "Admin_C"
//    ),
//
//    // 4. พนักงานบริษัทคู่สัญญา (COMPANY) ข้อมูลน้อย (ไม่บังคับหลายฟิลด์)
//    Patient(
//        patientId = 1004,
//        documentType = DocumentType.ID,
//        idCard = "1200055555555",
//        patientType = PatientType.COMPANY,
//        prefix = Prefix.MRS,
//        firstName = "Manee",
//        lastName = "Suksabai",
//        telNo = "0855556666",
//        createdAt = Instant.parse("2026-07-13T11:45:00Z"),
//        createdBy = "Admin_A"
//    ),
//
//    // 5. นักศึกษา (STUDENT) ที่ถูกลบออกจากระบบ (มี deletedAt)
//    Patient(
//        patientId = 1005,
//        documentType = DocumentType.ID,
//        idCard = "1100099999999",
//        patientType = PatientType.STUDENT,
//        prefix = Prefix.MR,
//        firstName = "Wicha",
//        lastName = "Riankueng",
//        telNo = "0844443333",
//        age = 19,
//        createdAt = Instant.parse("2026-05-01T08:00:00Z"),
//        createdBy = "System",
//        deletedAt = Instant.parse("2026-07-14T16:00:00Z"),
//        deletedBy = "Admin_B"
//    ),
//
//    // 6. พนักงาน (EMPLOYEE) ที่ไม่ระบุคำนำหน้า (Prefix เป็น Null)
//    Patient(
//        patientId = 1006,
//        documentType = DocumentType.ID,
//        idCard = "2100011122233",
//        patientType = PatientType.EMPLOYEE,
//        firstName = "Napat",
//        lastName = "Boonmak",
//        telNo = "0822221111",
//        email = "napat.b@company.com",
//        address = "789 หมู่ 1 ต.คลองหลวง อ.คลองหลวง จ.ปทุมธานี",
//        zipCode = "12120",
//        createdAt = Instant.parse("2026-07-14T10:30:00Z"),
//        createdBy = "Admin_C"
//    ),
//
//    // 7. บุคคลภายนอก (EXTERNAL) ผู้หญิง (MS) มี Line ID
//    Patient(
//        patientId = 1007,
//        documentType = DocumentType.ID,
//        idCard = "5100077788899",
//        lineId = "kanlayanee_ka",
//        patientType = PatientType.EXTERNAL,
//        prefix = Prefix.MS,
//        firstName = "Kanlayanee",
//        lastName = "Suayngam",
//        telNo = "0877778888",
//        age = 28,
//        createdAt = Instant.parse("2026-07-15T08:00:00Z"),
//        createdBy = "Admin_A"
//    ),
//
//    // 8. พนักงานบริษัทคู่สัญญา (COMPANY) เป็นชาวต่างชาติใช้ Passport
//    Patient(
//        patientId = 1008,
//        documentType = DocumentType.PASSPORT,
//        passportId = "B98765432",
//        patientType = PatientType.COMPANY,
//        prefix = Prefix.MS,
//        firstName = "Alice",
//        lastName = "Johnson",
//        telNo = "0899990000",
//        email = "alice.j@corp.com",
//        underlyingDisease = "Hypertension",
//        age = 50,
//        createdAt = Instant.parse("2026-07-10T13:00:00Z"),
//        createdBy = "Admin_B"
//    ),
//
//    // 9. นักศึกษา (STUDENT) เพิ่งสร้างข้อมูลสดๆ ร้อนๆ (ใช้ Instant.now())
//    Patient(
//        patientId = 1009,
//        documentType = DocumentType.ID,
//        idCard = "1100033344455",
//        patientType = PatientType.STUDENT,
//        prefix = Prefix.MS,
//        firstName = "Pim",
//        lastName = "Sodsai",
//        telNo = "0833334444",
//        age = 21,
//        zipCode = "10310",
//        createdAt = Instant.parse("2026-01-20T09:00:00Z"), // ใช้เวลาปัจจุบันขณะรันโค้ด
//        createdBy = "AutoRegisterSystem"
//    ),
//
//    // 10. บุคคลภายนอก (EXTERNAL) ไม่มีประวัติในหลายฟิลด์ แต่เคยอัปเดตเบอร์โทรศัพท์
//    Patient(
//        patientId = 1010,
//        documentType = DocumentType.ID,
//        idCard = "4100055544433",
//        patientType = PatientType.EXTERNAL,
//        prefix = Prefix.MRS,
//        firstName = "Somkid",
//        lastName = "Phonpanya",
//        telNo = "0866667777",
//        createdAt = Instant.parse("2026-01-20T09:00:00Z"),
//        createdBy = "Admin_A",
//        updatedAt = Instant.parse("2026-07-15T09:30:00Z"),
//        updatedBy = "Admin_A"
//    )
//)
//
//// 1. ข้อมูลคำถาม (Master Data)
//val mockQuestions = listOf(
//    ScreeningQuestion(
//        questionId = 1,
//        questionText = "ท่านมีอาการป่วย มีไข้ ภายใน 14 วันที่ผ่านมาหรือไม่?",
//        active = true,
//        displayOrder = 1,
//        createdAt = Instant.parse("2026-07-01T08:00:00Z"),
//        createdBy = "system_admin"
//    ),
//    ScreeningQuestion(
//        questionId = 2,
//        questionText = "ท่านมีประวัติแพ้ยา แพ้อาหาร หรือแพ้วัคซีนมาก่อนหรือไม่?",
//        active = true,
//        displayOrder = 2,
//        createdAt = Instant.parse("2026-07-01T08:00:00Z"),
//        createdBy = "system_admin"
//    ),
//    ScreeningQuestion(
//        questionId = 3,
//        questionText = "[Text]ท่านมีโรคประจำตัวที่กำลังรักษาอยู่หรือไม่",
//        active = true,
//        displayOrder = 3,
//        createdAt = Instant.parse("2026-07-01T08:00:00Z"),
//        createdBy = "system_admin"
//    )
//)
//
//// 2. ข้อมูลรอบการทำแบบประเมิน (Transaction Header)
//val mockRecords = listOf(
//    ScreeningRecord(
//        recordId = 1,
//        patientId = 1001, // ของคุณสมชาย (ทำเมื่อ 15 ก.ค.)
//        createdAt = Instant.parse("2026-07-15T08:30:00Z"),
//        createdBy = "Somchai"
//    ),
//    ScreeningRecord(
//        recordId = 2,
//        patientId = 1002, // ของคุณสุดา (ทำเมื่อ 15 ก.ค.)
//        createdAt = Instant.parse("2026-07-15T09:15:00Z"),
//        createdBy = "Suda"
//    )
//)
//
//// 3. ข้อมูลคำตอบแบบประเมิน (Transaction Detail)
//val mockAnswers = listOf(
//    // คำตอบของ Record ID 1 (คุณสมชาย - ไม่มีอาการใดๆ)
//    ScreeningAnswer(recordId = 1, questionId = 1, answer = false, remark = null, createdAt = Instant.parse("2026-07-15T08:31:00Z"), createdBy = "Somchai"),
//    ScreeningAnswer(recordId = 1, questionId = 2, answer = false, remark = null, createdAt = Instant.parse("2026-07-15T08:31:20Z"), createdBy = "Somchai"),
//    ScreeningAnswer(recordId = 1, questionId = 3, answer = false, remark = null, createdAt = Instant.parse("2026-07-15T08:31:45Z"), createdBy = "Somchai"),
//
//    // คำตอบของ Record ID 2 (คุณสุดา - มีอาการแพ้และโรคประจำตัว)
//    ScreeningAnswer(recordId = 2, questionId = 1, answer = false, remark = null, createdAt = Instant.parse("2026-07-15T09:16:00Z"), createdBy = "Suda"),
//    ScreeningAnswer(recordId = 2, questionId = 2, answer = true, remark = "แพ้อาหารทะเลรุนแรง", createdAt = Instant.parse("2026-07-15T09:16:30Z"), createdBy = "Suda"),
//    ScreeningAnswer(recordId = 2, questionId = 3, answer = true, remark = "โรคหอบหืด ใช้ยาพ่น", createdAt = Instant.parse("2026-07-15T09:17:00Z"), createdBy = "Suda")
//)
//data class ScreeningQuestion(
//    val questionId: Int,
//    val questionText: String
//)

// ข้อมูลคำถามจากแบบฟอร์มคัดกรองวัคซีน (PDF)
//val mockQuestions = listOf(
//    ScreeningQuestion(1, "มีประวัติแพ้ไข่ไก่อย่างรุนแรง"),
//    ScreeningQuestion(2, "เคยแพ้วัคซีนไข้หวัดใหญ่ หรือแพ้สารประกอบอื่นๆ ในวัคซีนอย่างรุนแรง"),
//    ScreeningQuestion(3, "กำลังมีไข้ หรือกำลังเจ็บป่วยเฉียบพลัน"),
//    ScreeningQuestion(4, "เพิ่งหายจากการเจ็บป่วยเฉียบพลันมาไม่เกิน 7 วัน"),
//    ScreeningQuestion(5, "เพิ่งมานอนรักษาตัวและออกจากโรงพยาบาล ไม่เกิน 14 วัน"),
//    ScreeningQuestion(6, "ยังมีโรคประจำตัวที่อาการกำเริบ หรือยังควบคุมอาการไม่ได้"),
//    ScreeningQuestion(7, "ขณะตั้งครรภ์นี้มีภาวะครรภ์เสี่ยงสูง"),
//    ScreeningQuestion(8, "ชื่อผู้มารับวัคซีน"),
//    ScreeningQuestion(9, "นามสกุล"),
//    ScreeningQuestion(10, "เลขประจำตัวประชาชน"),
//    ScreeningQuestion(11, "โรคประจำตัว"),
//    ScreeningQuestion(12, "ที่อยู่"),
//    ScreeningQuestion(13, "เบอร์โทรศัพท์"),
//    ScreeningQuestion(14, "วันที่รับวัคซีน")
//)

// ข้อมูลผู้ป่วยจำลองสำหรับการทดสอบ
//val mockPatients = listOf(
//    Patient(1, "1234567890123", "สมชาย", "ใจดี", "0812345678", "-", "123 ถ.สุขุมวิท"),
//    Patient(2, "9876543210987", "สมหญิง", "รักดี", "0898765432", "ความดัน", "456 ถ.พญาไท")
//)