package pl.msiwak.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pl.msiwak.ui.createtype.CreateTypeViewModel

internal val viewModelModule = module {
    viewModel { CreateTypeViewModel() }
}