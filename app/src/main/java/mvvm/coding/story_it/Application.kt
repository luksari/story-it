package mvvm.coding.story_it

import android.app.Application
import mvvm.coding.story_it.base.appModule
import org.koin.android.ext.android.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule))
    }
}