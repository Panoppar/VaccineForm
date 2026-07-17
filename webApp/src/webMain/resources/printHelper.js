// Fills the official screening PDF template with a patient's data and answers,
// then opens the result in a new tab so staff can review/print/save it.
//
// Coordinates below were measured directly off a 2x render of
// screening-template.pdf (page size 595.28 x 841.89 pt) — see
// deploy/pdf-template/ for the measurement images this was derived from.
// pdf-lib uses a bottom-left origin (Y increases upward), so each Y here is
// (841.89 - measuredTopDownY).

const PAGE_HEIGHT = 841.89;

const PATIENT_FIELDS = {
    name: { x: 117.5, y: 674.4 },
    lastName: { x: 75, y: 659.4 },
    idCard: { x: 145, y: 634.4 },
    disease: { x: 97.5, y: 609.4 },
    address1: { x: 57.5, y: 584.4 },
    address2: { x: 37.5, y: 559.4 },
    tel: { x: 97.5, y: 497.4 },
    shotDate: { x: 97.5, y: 472.4 }
};

// The template only has 7 printed checklist rows (one older question about
// early pregnancy duration exists in the seeded DB but has no row on this
// template — matching answers for it are simply skipped).
const CHECKLIST_ROWS = [
    { keyword: "แพ้ไก่", y: 599.4 },
    { keyword: "แพ้วัคซีนไข้หวัดใหญ่", y: 574.4 },
    { keyword: "กำลังมีไข้", y: 499.4 },
    { keyword: "เพิ่งหายจากการเจ็บป่วย", y: 449.4 },
    { keyword: "เพิ่งมานอนรักษาตัว", y: 399.4 },
    { keyword: "ยังมีโรคประจำตัว", y: 349.4 },
    { keyword: "ภาวะครรภ์เสี่ยงสูง", y: 249.4 }
];
const YES_X = 471;
const NO_X = 519;

const CONSENT_MARK = { x: 42, y: 124.4 };

// Drawn as vector lines rather than a "✓" text glyph, since the embedded
// Thai font (Noto Sans Thai) only covers Thai + basic Latin and may not
// contain a check mark glyph — this renders identically regardless of font.
function drawCheckmark(page, x, y, opts) {
    const size = opts.size || 11;
    const color = opts.color;
    const thickness = opts.thickness || 1.6;
    page.drawLine({ start: { x, y: y + size * 0.32 }, end: { x: x + size * 0.38, y }, thickness, color });
    page.drawLine({ start: { x: x + size * 0.38, y }, end: { x: x + size, y: y + size * 0.85 }, thickness, color });
}

let cachedTemplateBytes = null;
let cachedFontBytes = null;

async function loadAssets() {
    if (!cachedTemplateBytes) {
        const res = await fetch("screening-template.pdf");
        cachedTemplateBytes = await res.arrayBuffer();
    }
    if (!cachedFontBytes) {
        const res = await fetch("notosansthai_variablefont_wdth.ttf");
        cachedFontBytes = await res.arrayBuffer();
    }
    return { templateBytes: cachedTemplateBytes, fontBytes: cachedFontBytes };
}

function wrapAddress(address, maxChars) {
    if (!address) return ["", ""];
    if (address.length <= maxChars) return [address, ""];
    // Simple char-count wrap (Thai has no spaces to break on reliably)
    return [address.slice(0, maxChars), address.slice(maxChars, maxChars * 2)];
}

async function generateAndPrintVaccineForm(patientInfoJson, answersJson) {
    try {
        const patient = JSON.parse(patientInfoJson);
        const answers = JSON.parse(answersJson);
        const { PDFDocument, rgb } = PDFLib;

        const { templateBytes, fontBytes } = await loadAssets();

        const pdfDoc = await PDFDocument.load(templateBytes);
        pdfDoc.registerFontkit(fontkit);
        const thaiFont = await pdfDoc.embedFont(fontBytes, { subset: true });

        const page = pdfDoc.getPages()[0];
        const textOpts = { size: 11, font: thaiFont, color: rgb(0, 0, 0.55) };
        const markOpts = { size: 12, font: thaiFont, color: rgb(0.8, 0, 0) };

        const idNumber = patient.idCard || patient.passportId || "";
        const [addr1, addr2] = wrapAddress(patient.address, 45);

        page.drawText(patient.firstName || "", { x: PATIENT_FIELDS.name.x, y: PATIENT_FIELDS.name.y, ...textOpts });
        page.drawText(patient.lastName || "", { x: PATIENT_FIELDS.lastName.x, y: PATIENT_FIELDS.lastName.y, ...textOpts });
        page.drawText(idNumber, { x: PATIENT_FIELDS.idCard.x, y: PATIENT_FIELDS.idCard.y, ...textOpts });
        page.drawText(patient.underlyingDisease || "-", { x: PATIENT_FIELDS.disease.x, y: PATIENT_FIELDS.disease.y, ...textOpts });
        page.drawText(addr1, { x: PATIENT_FIELDS.address1.x, y: PATIENT_FIELDS.address1.y, ...textOpts });
        if (addr2) page.drawText(addr2, { x: PATIENT_FIELDS.address2.x, y: PATIENT_FIELDS.address2.y, ...textOpts });
        page.drawText(patient.telNo || "", { x: PATIENT_FIELDS.tel.x, y: PATIENT_FIELDS.tel.y, ...textOpts });
        page.drawText(patient.shotDate || "", { x: PATIENT_FIELDS.shotDate.x, y: PATIENT_FIELDS.shotDate.y, ...textOpts });

        answers.forEach((answer) => {
            const row = CHECKLIST_ROWS.find((r) => (answer.question || "").includes(r.keyword));
            if (!row) return;
            const x = answer.isYes ? YES_X : NO_X;
            drawCheckmark(page, x, row.y, { size: markOpts.size, color: markOpts.color });
        });

        // Patient already digitally consented in the app before submitting,
        // so the informed-consent checkbox is marked automatically. The
        // signature line is intentionally left blank for an in-person
        // wet-ink signature at the time of vaccination.
        drawCheckmark(page, CONSENT_MARK.x, CONSENT_MARK.y, { size: markOpts.size, color: markOpts.color });

        const pdfBytes = await pdfDoc.save();
        const blob = new Blob([pdfBytes], { type: "application/pdf" });
        const url = URL.createObjectURL(blob);
        window.open(url, "_blank");
    } catch (e) {
        console.error("พิมพ์ใบคัดกรองไม่สำเร็จ:", e);
        alert("พิมพ์ใบคัดกรองไม่สำเร็จ: " + e.message);
    }
}

window.generateAndPrintVaccineForm = generateAndPrintVaccineForm;
