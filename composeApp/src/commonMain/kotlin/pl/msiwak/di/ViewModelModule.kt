package pl.msiwak.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pl.msiwak.ui.createprediction.CreateTypeViewModel

internal val viewModelModule = module {
    viewModel { CreateTypeViewModel(get()) }
}