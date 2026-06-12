package br.com.lucolimac.xuxubank.di

import androidx.room.Room
import br.com.lucolimac.xuxubank.data.local.XuxuDatabase
import br.com.lucolimac.xuxubank.data.repository.DebtRepositoryImpl
import br.com.lucolimac.xuxubank.data.repository.ClientRepositoryImpl
import br.com.lucolimac.xuxubank.data.repository.UserRepositoryImpl
import br.com.lucolimac.xuxubank.domain.repository.DebtRepository
import br.com.lucolimac.xuxubank.domain.repository.ClientRepository
import br.com.lucolimac.xuxubank.domain.repository.UserRepository
import br.com.lucolimac.xuxubank.domain.usecase.ManageDebtUseCase
import br.com.lucolimac.xuxubank.domain.usecase.ManageClientUseCase
import br.com.lucolimac.xuxubank.domain.usecase.ManageUserUseCase
import br.com.lucolimac.xuxubank.ui.viewmodel.DebtViewModel
import br.com.lucolimac.xuxubank.ui.viewmodel.ClientViewModel
import br.com.lucolimac.xuxubank.ui.viewmodel.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Dependency injection module using Koin.
 * Groups database, repository, use case, and viewmodel instances.
 */
val appModule = module {
    // Database singleton
    single {
        Room.databaseBuilder(
                androidContext(),
                XuxuDatabase::class.java,
                XuxuDatabase.DATABASE_NAME
            ).fallbackToDestructiveMigration(false)
            .build()
    }

    // DAO instances
    single { get<XuxuDatabase>().userDao() }
    single { get<XuxuDatabase>().clientDao() }
    single { get<XuxuDatabase>().debtDao() }

    // Repository implementations
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<ClientRepository> { ClientRepositoryImpl(get()) }
    single<DebtRepository> { DebtRepositoryImpl(get()) }

    // Use Case factories
    factory { ManageUserUseCase(get()) }
    factory { ManageClientUseCase(get()) }
    factory { ManageDebtUseCase(get()) }

    // UI ViewModels
    viewModel { UserViewModel(get()) }
    viewModel { ClientViewModel(get()) }
    viewModel { DebtViewModel(get()) }
}
