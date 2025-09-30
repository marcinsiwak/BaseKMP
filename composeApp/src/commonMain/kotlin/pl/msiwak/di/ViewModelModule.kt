package pl.msiwak.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pl.msiwak.MainViewModel
import pl.msiwak.ui.aiGenerated.AiGeneratedViewModel
import pl.msiwak.ui.example.ExampleViewModel
import pl.msiwak.ui.game.LobbyViewModel
import pl.msiwak.ui.game.gameplay.CardsPreparationViewModel
import pl.msiwak.ui.game.round.RoundViewModel
import pl.msiwak.ui.game.roundinfo.RoundInfoViewModel
import pl.msiwak.ui.game.start.StartViewModel

internal val viewModelModule = module {
    viewModel { ExampleViewModel() }
    viewModel { AiGeneratedViewModel() }
    viewModel { MainViewModel(get(), get()) }
    viewModel { StartViewModel(get(), get(), get(), get()) }
    viewModel { LobbyViewModel(get(), get(), get(), get()) }
    viewModel { CardsPreparationViewModel(get(), get(), get(), get()) }
    viewModel { RoundInfoViewModel(get(), get()) }
    viewModel { RoundViewModel(get(), get(), get()) }
}
