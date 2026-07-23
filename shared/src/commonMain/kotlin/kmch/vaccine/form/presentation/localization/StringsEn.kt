package kmch.vaccine.form.presentation.localization

import kmch.vaccine.form.domain.model.DocumentType
import kmch.vaccine.form.domain.model.PatientType
import kmch.vaccine.form.domain.model.Prefix
import kmch.vaccine.form.domain.model.Sex

object StringsEn : Strings {

    override val ok = "OK"
    override val cancel = "Cancel"
    override val save = "Save"
    override val notice = "Notice"

    override val notFoundTitle = "404 Not Found"
    override val notFoundMessage = "Please enter a valid URL path to access the system."
    override val goToScreeningForm = "Go to the screening form"

    override val screeningLoadError =
        "Failed to load the screening form. Please check your connection and try again."
    override val retry = "Try Again"

    override val screeningFormTitle = "Seasonal Influenza Vaccine Screening Form"
    override val screeningFormSubtitle =
        "Please mark truthfully so the staff can determine if you are eligible to receive the vaccine."

    override val patientInfoSectionTitle = "Patient Information"
    override val prefixLabel = "Prefix"
    override fun prefixOptionLabel(prefix: Prefix) = when (prefix) {
        Prefix.MR -> "Mr."
        Prefix.MRS -> "Ms."
        Prefix.MISS -> "Mrs."
    }
    override val firstNameLabel = "First Name"
    override val lastNameLabel = "Last Name"
    override val sexLabel = "Sex"
    override fun sexOptionLabel(sex: Sex) = when (sex) {
        Sex.MALE -> "Male"
        Sex.FEMALE -> "Female"
    }
    override val patientTypeLabel = "Patient Type"
    override val patientTypeSelectLabel = "Select Patient Type"
    override fun patientTypeOptionLabel(type: PatientType) = when (type) {
        PatientType.STUDENT -> "KMITL Student"
        PatientType.EMPLOYEE -> "KMITL Employee"
        PatientType.COMPANY -> "Contracted Company"
        PatientType.EXTERNAL -> "External"
    }
    override val documentTypeLabel = "Identification Document"
    override fun documentTypeOptionLabel(type: DocumentType) = when (type) {
        DocumentType.ID_CARD -> "ID Card"
        DocumentType.PASSPORT -> "Passport"
    }
    override val idCardNumberLabel = "ID Card Number"
    override fun idCardDigitError(current: Int) = "Must be 13 digits (Current: $current)"
    override val passportNumberLabel = "Passport Number"
    override val ageLabel = "Age"
    override val phoneNumberLabel = "Phone Number"
    override fun phoneDigitError(current: Int) = "Must be 10 digits (Current: $current)"
    override val vaccinationDateLabel = "Vaccination Date (YYYY-MM-DD)"
    override val selectDateContentDescription = "Select Date"
    override val underlyingDiseaseLabel = "Underlying Disease (If any)"
    override val addressLabel = "Address"
    override val zipCodeLabel = "Zip Code"

    override val healthScreeningSectionTitle = "Health Screening"
    override val noScreeningQuestionsMessage = "No screening questions found at this time. Please contact staff."
    override val specifyYourAnswerLabel = "Specify your answer"
    override val answerYes = "Yes"
    override val answerNo = "No"

    override val pdpaConsentText =
        "KMITL Hospital (King Mongkut's Institute of Technology Ladkrabang) collects, uses, and discloses your personal data — including name, email, national ID number, age, phone number, faculty, health information, behavior, and environmental health data — to coordinate and contact you if necessary following the health check-up, under the Student Health Check-up Project, fiscal year 2026, relying on the 'contractual basis and legitimate interest basis'.\n\n" +
                "The hospital confirms your personal data will be kept confidential and will not be disclosed without your consent, except as required by law, by duty, or when necessary for diagnosis, treatment, and rehabilitation. Should any abnormality be found, the hospital will notify you for further follow-up and care.\n\n" +
                "For further inquiries on personal data protection, please visit "
    override val pdpaLinkLabel = "pdpa.kmitl.ac.th"
    override val vaccineInfoConsentText =
        "I have received and understood the information regarding the influenza vaccine."

    override val submitErrorInvalidOrDuplicate = "Invalid data or duplicate document in the system. Please check again."
    override val submitErrorVaccineOutOfStock = "Vaccine is out of stock. Please contact staff."
    override fun submitErrorGeneric(code: Int) = "An error occurred ($code). Please try again."
    override val submitErrorNetwork = "Cannot connect to the server. Please try again."
    override val submitting = "Submitting..."
    override val confirmAndSubmit = "Confirm and Submit"

    override val submissionSuccessTitle = "Submission Successful!"
    override val patientIdLabel = "Patient ID"
    override val vaccinationIdLabel = "Vaccination ID"
    override val submissionSuccessNote = "You may capture this screen as proof and exit this page."
    override val returnToHome = "Return to Home"

    override val adminDashboardTitle = "Vaccine Screening Management Dashboard"
    override val lotIdLabel = "Lot ID (numeric)"
    override val addVaccineOrLotContentDescription = "Add new vaccine or lot"
    override val employeeRateIdLabel = "Staff Rate No."
    override val searchPatientsLabel = "Search name/ID/phone"
    override val createNewScreeningContentDescription = "Create new screening form"
    override fun noPatientsFoundMessage(query: String) = "No patients found matching '$query'"
    override fun patientListIdLine(id: Int) = "ID: $id"
    override fun patientListDateLine(date: String) = "Date: $date"
    override fun patientListNameLine(name: String) = "Name: $name"
    override fun patientListPhoneLine(tel: String) = "Phone: $tel"

    override val backButton = "< Back"
    override val newScreeningByStaffTitle = "New screening form (staff entry)"
    override fun screeningDetailTitle(patientId: Int?) = "Screening Details (Patient ID: $patientId)"
    override val patientNotFoundMessage = "Patient not found"
    override val answerHistoryTitle = "Answer History"
    override val printScreeningFormButton = "Print Screening Form"
    override fun patientHeaderName(name: String) = "Name: $name"
    override fun patientHeaderId(id: Int) = "Patient ID: $id"
    override fun patientHeaderPhone(tel: String) = "Phone: $tel"
    override fun patientHeaderUnderlyingDisease(disease: String) = "Underlying Disease: $disease"
    override fun vaccinationDateChip(date: String, vaccineName: String?, lotNumber: String?) =
        "Vaccination Date: $date" +
                (vaccineName?.let { " • $it" } ?: "") +
                (lotNumber?.let { " • Lot $it" } ?: "")
    override fun answerRemark(remark: String) = " — Note: $remark"

    override val manageVaccineDataTitle = "Manage Vaccine Data"
    override val addNewVaccineOption = "Add New Vaccine"
    override val addNewLotOption = "Add Vaccine Lot"
    override val vaccineNameLabel = "Vaccine Name"
    override val vaccineIdRefLabel = "Vaccine ID"
    override val lotNumberLabel = "Lot Number"
    override val initialQuantityLabel = "Initial Quantity"
    override fun vaccineCreatedMessage(vaccineId: Int) = "Vaccine added successfully (Vaccine ID: $vaccineId)"
    override fun lotCreatedMessage(lotId: Int) = "Lot added successfully (Lot ID: $lotId)"
    override fun genericErrorMessage(message: String) = "An error occurred: $message"
    override val vaccineIdMustBeNumberError = "Vaccine ID must be a number"

    override val adminLoginTitle = "Admin Management System"
    override val adminLoginSubtitle = "Please enter the password to continue"
    override val passwordLabel = "Password"
    override val passwordIncorrectError = "Incorrect password"
    override val passwordLockedOutError = "Too many incorrect attempts. Please wait a moment and try again."
    override val backToHome = "Back to Home"
    override val logIn = "Log In"
}
