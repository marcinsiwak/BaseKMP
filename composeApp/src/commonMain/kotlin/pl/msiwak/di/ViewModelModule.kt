package pl.msiwak.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pl.msiwak.ui.aiGenerated.AiGeneratedViewModel
import pl.msiwak.ui.example.ExampleViewModel
import pl.msiwak.ui.game.GameViewModel
import pl.msiwak.ui.game.start.StartViewModel

internal val viewModelModule = module {
    viewModel { ExampleViewModel() }
    viewModel { AiGeneratedViewModel() }
    viewModel { StartViewModel(get(), get(), get()) }
    viewModel { GameViewModel(get(), get(), get(), get()) }
}
