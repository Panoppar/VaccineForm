package kmch.vaccine.form.presentation.navigation

sealed class AppRoute {
    data object ScreeningForm : AppRoute()
    data object Admin : AppRoute()
    data object NotFound : AppRoute()
}

fun parseRoute(hash: String): AppRoute = when {
    hash.contains("#/vaccine/admin") -> AppRoute.Admin
    hash.contains("#/vaccine/form") -> AppRoute.ScreeningForm
    else -> AppRoute.NotFound
}
