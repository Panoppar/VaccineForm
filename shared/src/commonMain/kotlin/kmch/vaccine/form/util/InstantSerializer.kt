package kmch.vaccine.form.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.Instant // หากใช้ java.time.Instant ให้เปลี่ยน import ตรงนี้

object InstantSerializer : KSerializer<Instant> {
    // กำหนดให้แปลง Instant ออกมาเป็นชนิด String
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    // ขาแปลงเป็น JSON (เขียนข้อมูลลง JSON String)
    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toString())
    }

    // ขาอ่านจาก JSON (อ่าน String กลับมาเป็นคลาส Instant)
    override fun deserialize(decoder: Decoder): Instant {
        return Instant.parse(decoder.decodeString())
    }
}