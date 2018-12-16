package mvvm.coding.story_it.base

import androidx.room.Room
import androidx.room.RoomDatabase
import mvvm.coding.story_it.data.db.GameDatabase
import mvvm.coding.story_it.data.db.repository.GameRepository
import mvvm.coding.story_it.data.db.repository.GameRepositoryImpl
import mvvm.coding.story_it.ui.main.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel

import org.koin.dsl.module.module

val appModule = module {

    single { GameRepositoryImpl(get(), get()) as GameRepository }

    single {
        GameDatabase.buildDatabase(androidApplication())
    }
    single { get<GameDatabase>().scoreDao() }
    single { get<GameDatabase>().playerDao() }

    viewModel { MainViewModel(get()) }



}