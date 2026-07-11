package kmch.vaccine.form

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform