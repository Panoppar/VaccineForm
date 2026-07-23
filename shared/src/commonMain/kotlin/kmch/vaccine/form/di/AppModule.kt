package kmch.vaccine.form.di

import kmch.vaccine.form.data.print.ScreeningDocumentPrinterImpl
import kmch.vaccine.form.data.remote.HttpClientFactory
import kmch.vaccine.form.data.remote.VaccineApiService
import kmch.vaccine.form.data.repository.AccessibilityPreferencesRepositoryImpl
import kmch.vaccine.form.data.repository.AdminAuthRepositoryImpl
import kmch.vaccine.form.data.repository.LanguagePreferenceRepositoryImpl
import kmch.vaccine.form.data.repository.RegistrationRepositoryImpl
import kmch.vaccine.form.data.repository.VaccineInventoryRepositoryImpl
import kmch.vaccine.form.domain.repository.AccessibilityPreferencesRepository
import kmch.vaccine.form.domain.repository.AdminAuthRepository
import kmch.vaccine.form.domain.repository.LanguagePreferenceRepository
import kmch.vaccine.form.domain.repository.RegistrationRepository
import kmch.vaccine.form.domain.repository.VaccineInventoryRepository
import kmch.vaccine.form.domain.service.ScreeningDocumentPrinter
import kmch.vaccine.form.domain.usecase.AuthenticateAdminUseCase
import kmch.vaccine.form.domain.usecase.CreateLotUseCase
import kmch.vaccine.form.domain.usecase.CreateVaccineUseCase
import kmch.vaccine.form.domain.usecase.GetRegistrationDetailUseCase
import kmch.vaccine.form.domain.usecase.GetScreeningQuestionsUseCase
import kmch.vaccine.form.domain.usecase.ListRegistrationsUseCase
import kmch.vaccine.form.domain.usecase.PrintScreeningDocumentUseCase
import kmch.vaccine.form.domain.usecase.SubmitRegistrationUseCase
import kmch.vaccine.form.presentation.accessibility.AccessibilityController
import kmch.vaccine.form.presentation.localization.LocalizationController
import kmch.vaccine.form.presentation.viewmodel.AdminDetailViewModel
import kmch.vaccine.form.presentation.viewmodel.AdminListViewModel
import kmch.vaccine.form.presentation.viewmodel.AdminLoginViewModel
import kmch.vaccine.form.presentation.viewmodel.AdminSessionViewModel
import kmch.vaccine.form.presentation.viewmodel.ScreeningFormViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // ---- Data layer ----
    single { HttpClientFactory.create() }
    single { VaccineApiService(get()) }
    single<RegistrationRepository> { RegistrationRepositoryImpl(get()) }
    single<VaccineInventoryRepository> { VaccineInventoryRepositoryImpl(get()) }
    single<AdminAuthRepository> { AdminAuthRepositoryImpl() }
    single<LanguagePreferenceRepository> { LanguagePreferenceRepositoryImpl() }
    single<AccessibilityPreferencesRepository> { AccessibilityPreferencesRepositoryImpl() }
    single<ScreeningDocumentPrinter> { ScreeningDocumentPrinterImpl() }

    // ---- Localization / Accessibility ----
    single { LocalizationController(get()) }
    single { AccessibilityController(get()) }

    // ---- Use cases ----
    factory { GetScreeningQuestionsUseCase(get()) }
    factory { SubmitRegistrationUseCase(get()) }
    factory { ListRegistrationsUseCase(get()) }
    factory { GetRegistrationDetailUseCase(get()) }
    factory { CreateVaccineUseCase(get()) }
    factory { CreateLotUseCase(get()) }
    factory { AuthenticateAdminUseCase(get()) }
    factory { PrintScreeningDocumentUseCase(get()) }

    // ---- ViewModels ----
    viewModel { ScreeningFormViewModel(get(), get()) }
    viewModel { AdminListViewModel(get(), get(), get()) }
    viewModel { (patientId: Int) -> AdminDetailViewModel(patientId, get(), get()) }
    viewModel { AdminLoginViewModel(get()) }
    viewModel { AdminSessionViewModel() }
}
