package kmch.vaccine.form

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

// Backend Golang (vaccinework_app) ฟังอยู่ที่ port 8081 แต่ไม่เปิดสู่อินเทอร์เน็ตโดยตรง
// เว็บ mobile.kmch.kmitl.ac.th มี reverse proxy (nginx) ตัวกลางอยู่แล้วสำหรับ hospitalKMCH
// ทีมเซิร์ฟเวอร์เพิ่ม route "/vaccine-api/" -> http://vaccinework_app:8081/ ให้ในนั้น
// ใช้ relative path นี้เพื่อให้ทำงานได้เหมือนกันทั้งตอน deploy จริง (ผ่าน proxy กลาง)
// และตอนทดสอบ standalone ในเครื่อง (ผ่าน deploy/nginx.conf.template + BACKEND_HOST)
const val API_BASE_URL = "/vaccinework-api/api/v1"

// สร้าง Instance ของ HttpClient เป็น Singleton แบบง่ายๆ
@OptIn(ExperimentalSerializationApi::class)
val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            // ป้องกันแอปแครชเวลา Backend Golang ส่งฟิลด์ใหม่ที่ฝั่งเรายังไม่มีใน Data Class
            ignoreUnknownKeys = true
            // ให้ใช้ค่า Default หากข้อมูลบางตัวเป็น null
            encodeDefaults = true
            // Backend Golang ใช้ snake_case (เช่น first_name) แต่ฝั่ง Kotlin ใช้ camelCase (firstName)
            namingStrategy = JsonNamingStrategy.SnakeCase
        })
    }
}