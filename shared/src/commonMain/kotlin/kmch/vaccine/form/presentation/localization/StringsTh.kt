package kmch.vaccine.form.presentation.localization

import kmch.vaccine.form.domain.model.DocumentType
import kmch.vaccine.form.domain.model.PatientType
import kmch.vaccine.form.domain.model.Prefix
import kmch.vaccine.form.domain.model.Sex

object StringsTh : Strings {

    override val ok = "ตกลง"
    override val cancel = "ยกเลิก"
    override val save = "บันทึก"
    override val notice = "แจ้งเตือน"

    override val notFoundTitle = "404 ไม่พบหน้าที่ต้องการ"
    override val notFoundMessage = "กรุณาระบุ URL Path ให้ถูกต้องเพื่อเข้าถึงระบบ"
    override val goToScreeningForm = "ไปยังหน้าฟอร์มคัดกรอง"

    override val screeningLoadError =
        "โหลดแบบคัดกรองไม่สำเร็จ กรุณาตรวจสอบการเชื่อมต่อแล้วลองใหม่อีกครั้ง"
    override val retry = "ลองใหม่อีกครั้ง"

    override val screeningFormTitle = "แบบสอบถามผู้มารับบริการ วัคซีนไข้หวัดใหญ่ตามฤดูกาล"
    override val screeningFormSubtitle =
        "กรุณาทำเครื่องหมายตามความเป็นจริงเพื่อเจ้าหน้าที่จะได้พิจารณาว่าท่านสามารถฉีดวัคซีนได้หรือไม่"

    override val patientInfoSectionTitle = "ข้อมูลผู้มารับบริการ"
    override val prefixLabel = "คำนำหน้า"
    override fun prefixOptionLabel(prefix: Prefix) = when (prefix) {
        Prefix.MR -> "นาย"
        Prefix.MRS -> "นาง"
        Prefix.MISS -> "นางสาว"
    }
    override val firstNameLabel = "ชื่อ"
    override val lastNameLabel = "นามสกุล"
    override val sexLabel = "เพศ"
    override fun sexOptionLabel(sex: Sex) = when (sex) {
        Sex.MALE -> "ชาย"
        Sex.FEMALE -> "หญิง"
    }
    override val patientTypeLabel = "ประเภทผู้มารับบริการ"
    override val patientTypeSelectLabel = "เลือกประเภทผู้รับบริการ"
    override fun patientTypeOptionLabel(type: PatientType) = when (type) {
        PatientType.STUDENT -> "นักศึกษา สจล."
        PatientType.EMPLOYEE -> "พนักงาน"
        PatientType.COMPANY -> "บริษัทคู่สัญญา"
        PatientType.EXTERNAL -> "บุคคลภายนอก"
    }
    override val documentTypeLabel = "เอกสารยืนยันตัวตน"
    override fun documentTypeOptionLabel(type: DocumentType) = when (type) {
        DocumentType.ID_CARD -> "บัตรประชาชน"
        DocumentType.PASSPORT -> "พาสปอร์ต"
    }
    override val idCardNumberLabel = "เลขบัตรประจำตัวประชาชน"
    override fun idCardDigitError(current: Int) = "เลขบัตรประชาชนต้องมี 13 หลัก (ตอนนี้ $current หลัก)"
    override val passportNumberLabel = "เลขที่พาสปอร์ต"
    override val ageLabel = "อายุ"
    override val phoneNumberLabel = "เบอร์โทรศัพท์"
    override fun phoneDigitError(current: Int) = "เบอร์โทรศัพท์ต้องมี 10 หลัก (ตอนนี้ $current หลัก)"
    override val vaccinationDateLabel = "วันที่รับวัคซีน (YYYY-MM-DD)"
    override val selectDateContentDescription = "เลือกวันที่"
    override val underlyingDiseaseLabel = "โรคประจำตัว (ถ้ามี)"
    override val addressLabel = "ที่อยู่"
    override val zipCodeLabel = "รหัสไปรษณีย์"

    override val healthScreeningSectionTitle = "แบบคัดกรองสุขภาพ"
    override val noScreeningQuestionsMessage = "ไม่พบข้อมูลแบบคัดกรองในขณะนี้ กรุณาติดต่อเจ้าหน้าที่"
    override val specifyYourAnswerLabel = "ระบุคำตอบของคุณ"
    override val answerYes = "มี / ใช่"
    override val answerNo = "ไม่มี / ไม่ใช่"

    override val pdpaConsentText =
        "โรงพยาบาลพระจอมเกล้าเจ้าคุณทหาร สถาบันเทคโนโลยีพระจอมเกล้าเจ้าคุณทหารลาดกระบัง (KMITL) มีการเก็บรวบรวม ใช้ และเปิดเผยข้อมูลส่วนบุคคลของท่าน ประกอบด้วย ชื่อ นามสกุล อีเมล เลขบัตรประจำตัวประชาชน อายุ หมายเลขโทรศัพท์ คณะ ข้อมูลสุขภาพ พฤติกรรม และสุขภาวะแวดล้อม เพื่อดำเนินการ ประสานงาน รวมถึงติดต่อกรณีที่มีอันจำเป็นภายหลังการตรวจสุขภาพ ในโครงการตรวจสุขภาพนักศึกษา ประจำปีงบประมาณ 2569 โดยใช้ “ฐานสัญญาและฐานความจำเป็นเพื่อประโยชน์โดยชอบด้วยกฎหมาย”\n\n" +
                "ทางโรงพยาบาลยืนยันว่าข้อมูลส่วนบุคคลของท่านจะถูกเก็บเป็นความลับ และไม่มีการเปิดเผยโดยปราศจากความยินยอมของท่าน เว้นแต่เป็นการเปิดเผยตามที่กฎหมายกำหนด หรือตามหน้าที่ หรือเมื่อมีข้อบ่งชี้และความจำเป็นในการวินิจฉัย รักษาโรคและฟื้นฟูสภาพของข้าพเจ้า ซึ่งกรณีที่ตรวจพบความผิดปกติ ทางโรงพยาบาลจะมีการแจ้งผลให้ท่านทราบ เพื่อเป็นการติดตามและดูแลต่อไป\n\n" +
                "หากมีข้อสงสัยเกี่ยวกับการคุ้มครองข้อมูลส่วนบุคคล สามารถสืบค้นข้อมูลเพิ่มเติมและช่องทางติดต่อได้ที่เว็บไซต์ "
    override val pdpaLinkLabel = "pdpa.kmitl.ac.th"
    override val vaccineInfoConsentText =
        "ท่านได้รับข้อมูลเกี่ยวกับวัคซีนไข้หวัดใหญ่และได้ทำความเข้าใจแล้ว"

    override val submitErrorInvalidOrDuplicate = "ข้อมูลไม่ถูกต้องหรือเอกสารซ้ำในระบบ กรุณาตรวจสอบอีกครั้ง"
    override val submitErrorVaccineOutOfStock = "วัคซีนหมดสต็อกในขณะนี้ กรุณาติดต่อเจ้าหน้าที่"
    override fun submitErrorGeneric(code: Int) = "เกิดข้อผิดพลาด ($code) กรุณาลองใหม่อีกครั้ง"
    override val submitErrorNetwork = "ไม่สามารถเชื่อมต่อเซิร์ฟเวอร์ได้ กรุณาลองใหม่อีกครั้ง"
    override val submitting = "กำลังบันทึก..."
    override val confirmAndSubmit = "ยืนยันบันทึกข้อมูล"

    override val submissionSuccessTitle = "บันทึกข้อมูลสำเร็จ!"
    override val patientIdLabel = "รหัสผู้ป่วย"
    override val vaccinationIdLabel = "รหัสการฉีดวัคซีน"
    override val submissionSuccessNote = "ท่านสามารถแคปหน้าจอนี้ไว้เป็นหลักฐาน\nและออกจากหน้านี้ได้เลย"
    override val returnToHome = "กลับสู่หน้าหลัก"

    override val adminDashboardTitle = "แดชบอร์ดจัดการคัดกรองวัคซีน"
    override val lotIdLabel = "Lot ID (ตัวเลข)"
    override val addVaccineOrLotContentDescription = "เพิ่มวัคซีนหรือล็อตใหม่"
    override val employeeRateIdLabel = "เลขอัตราเจ้าหน้าที่"
    override val searchPatientsLabel = "ค้นหาชื่อ/เลขบัตร/เบอร์โทร"
    override val createNewScreeningContentDescription = "สร้างแบบคัดกรองใหม่"
    override fun noPatientsFoundMessage(query: String) = "ไม่พบข้อมูลผู้รับบริการที่ตรงกับ '$query'"
    override fun patientListIdLine(id: Int) = "รหัส: $id"
    override fun patientListDateLine(date: String) = "วันที่: $date"
    override fun patientListNameLine(name: String) = "ชื่อ-นามสกุล: $name"
    override fun patientListPhoneLine(tel: String) = "เบอร์โทร: $tel"

    override val backButton = "< กลับ"
    override val newScreeningByStaffTitle = "กรอกแบบคัดกรองใหม่โดยเจ้าหน้าที่"
    override fun screeningDetailTitle(patientId: Int?) = "รายละเอียดแบบคัดกรอง (Patient ID: $patientId)"
    override val patientNotFoundMessage = "ไม่พบข้อมูลผู้ป่วย"
    override val answerHistoryTitle = "ประวัติการตอบคำถาม"
    override val printScreeningFormButton = "พิมพ์ใบคัดกรอง"
    override fun patientHeaderName(name: String) = "ชื่อ-นามสกุล: $name"
    override fun patientHeaderId(id: Int) = "รหัสผู้ป่วย: $id"
    override fun patientHeaderPhone(tel: String) = "เบอร์โทร: $tel"
    override fun patientHeaderUnderlyingDisease(disease: String) = "โรคประจำตัว: $disease"
    override fun vaccinationDateChip(date: String, vaccineName: String?, lotNumber: String?) =
        "วันที่รับวัคซีน: $date" +
                (vaccineName?.let { " • $it" } ?: "") +
                (lotNumber?.let { " • Lot $it" } ?: "")
    override fun answerRemark(remark: String) = " — ระบุ: $remark"

    override val manageVaccineDataTitle = "จัดการข้อมูลวัคซีน"
    override val addNewVaccineOption = "เพิ่มวัคซีนใหม่"
    override val addNewLotOption = "เพิ่มล็อตวัคซีน"
    override val vaccineNameLabel = "ชื่อวัคซีน (Vaccine Name)"
    override val vaccineIdRefLabel = "รหัสอ้างอิงวัคซีน (Vaccine ID)"
    override val lotNumberLabel = "หมายเลขล็อต (Lot Number)"
    override val initialQuantityLabel = "จำนวนเริ่มต้น (Initial Quantity)"
    override fun vaccineCreatedMessage(vaccineId: Int) = "เพิ่มวัคซีนสำเร็จ (Vaccine ID: $vaccineId)"
    override fun lotCreatedMessage(lotId: Int) = "เพิ่มล็อตสำเร็จ (Lot ID: $lotId)"
    override fun genericErrorMessage(message: String) = "เกิดข้อผิดพลาด: $message"
    override val vaccineIdMustBeNumberError = "ID วัคซีนต้องเป็นตัวเลข"

    override val adminLoginTitle = "ระบบจัดการแอดมิน"
    override val adminLoginSubtitle = "กรุณากรอกรหัสผ่านเพื่อเข้าใช้งาน"
    override val passwordLabel = "รหัสผ่าน"
    override val passwordIncorrectError = "รหัสผ่านไม่ถูกต้อง"
    override val passwordLockedOutError = "กรอกรหัสผ่านผิดหลายครั้งเกินไป กรุณารอสักครู่แล้วลองใหม่อีกครั้ง"
    override val backToHome = "กลับหน้าหลัก"
    override val logIn = "เข้าสู่ระบบ"
}
