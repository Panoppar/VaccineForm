package kmch.vaccine.form

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

// สร้าง Instance ของ HttpClient เป็น Singleton แบบง่ายๆ
val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            // ป้องกันแอปแครชเวลา Backend Golang ส่งฟิลด์ใหม่ที่ฝั่งเรายังไม่มีใน Data Class
            ignoreUnknownKeys = true
            // ให้ใช้ค่า Default หากข้อมูลบางตัวเป็น null
            encodeDefaults = true
        })
    }
}