package kmch.vaccine.form.domain.model

// Not backend-serialized directly — RegistrationSubmission carries the exact
// legacy combined string (e.g. "นาย/MR.") so the API contract/stored data
// format is unchanged by adding a language toggle to the UI.
enum class Prefix { MR, MRS, MISS }

fun Prefix.toSubmissionValue(): String = when (this) {
    Prefix.MR -> "นาย/MR."
    Prefix.MRS -> "นาง/MS."
    Prefix.MISS -> "นางสาว/MRS."
}
