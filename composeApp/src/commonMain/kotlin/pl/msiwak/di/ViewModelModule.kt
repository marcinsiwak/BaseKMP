package pl.msiwak.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pl.msiwak.ui.createprediction.CreatePredictionViewModel

internal val viewModelModule = module {
    viewModel { CreatePredictionViewModel(get()) }
}
