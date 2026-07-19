package kmch.vaccine.form

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kmch.vaccine.form.theme.AppTheme
import kotlinx.browser.window
import org.w3c.dom.events.Event

@Composable
fun App() {
    // ดึงค่า Hash จาก URL ปัจจุบัน (เช่น #/vaccine/form)
    var currentRoute by remember { mutableStateOf(window.location.hash) }

    // คอยดักจับว่าผู้ใช้มีการพิมพ์เปลี่ยน URL ใน Address Bar หรือไม่
    DisposableEffect(Unit) {
        val onHashChange: (Event) -> Unit = {
            currentRoute = window.location.hash
        }
        window.addEventListener("hashchange", onHashChange)
        onDispose {
            window.removeEventListener("hashchange", onHashChange)
        }
    }

    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            when {
                // หาก URL คือ http://localhost:8080/#/vaccine/admin
                currentRoute.contains("#/vaccine/admin") -> {
                    // เรียกใช้ ProtectedAdminScreen แทน เพื่อให้ติดหน้า Login ก่อน
                    ProtectedAdminScreen(
                        onNavigateBack = {
                            // เมื่อกดปุ่มกลับ ให้เตะออกไปหน้าแบบฟอร์ม หรือหน้าแรก
                            window.location.hash = "#/vaccine/form"
                        }
                    )
                }

                // หาก URL คือ http://localhost:8080/#/vaccine/form
                currentRoute.contains("#/vaccine/form") -> {
                    VaccineScreeningForm()
                }

                // หากพิมพ์ URL ผิด หรือเข้ามาที่หน้าแรกตรงๆ โดยไม่มี Hash
                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "404 Not Found",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "กรุณาระบุ URL Path ให้ถูกต้องเพื่อเข้าถึงระบบ",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        // ปุ่มสำหรับกดนำทางไปยังหน้าแบบฟอร์ม
                        Button(onClick = { window.location.hash = "#/vaccine/form" }) {
                            Text("ไปยังหน้าฟอร์มคัดกรอง")
                        }
                    }
                }
            }
        }
    }
}