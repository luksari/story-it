package mvvm.coding.story_it

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import mvvm.coding.story_it.base.ViewModelFactory
import mvvm.coding.story_it.data.db.GameDatabase
import mvvm.coding.story_it.data.db.repository.GameRepository
import mvvm.coding.story_it.data.db.repository.GameRepositoryImpl
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class Application : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@Application))
        bind() from singleton { GameDatabase(instance()) }
        bind() from singleton { instance<GameDatabase>().playerDao() }
        bind() from singleton { instance<GameDatabase>().scoreDao() }
        bind<GameRepository>() with singleton { GameRepositoryImpl(instance(), instance()) }
        bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(instance()) }
    }

}