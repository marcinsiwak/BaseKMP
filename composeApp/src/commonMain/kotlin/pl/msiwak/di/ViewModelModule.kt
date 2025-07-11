package pl.msiwak.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pl.msiwak.ui.example.ExampleViewModel

internal val viewModelModule = module {
    viewModel { ExampleViewModel(get()) }
}
