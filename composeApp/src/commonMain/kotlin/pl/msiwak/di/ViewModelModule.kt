package pl.msiwak.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pl.msiwak.ui.example.ExampleViewModel
import pl.msiwak.ui.aiGenerated.AiGeneratedViewModel
import pl.msiwak.ui.game.GameViewModel

internal val viewModelModule = module {
    viewModel { ExampleViewModel(get()) }
    viewModel { AiGeneratedViewModel() }
    viewModel { GameViewModel(get(), get()) }
}
