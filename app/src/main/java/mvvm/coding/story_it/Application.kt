package mvvm.coding.story_it

import android.app.Application
import mvvm.coding.story_it.base.appModule
import org.koin.android.ext.android.startKoin

class Application : Application() {
//    override val kodein = Kodein.lazy {
//        import(androidXModule(this@Application))
//        bind() from singleton { GameDatabase(instance()) }
//        bind() from singleton { instance<GameDatabase>().playerDao() }
//        bind() from singleton { instance<GameDatabase>().scoreDao() }
//        bind<GameRepository>() with singleton { GameRepositoryImpl(instance(), instance()) }
//        bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(instance()) }
//    }


    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule))
    }
}