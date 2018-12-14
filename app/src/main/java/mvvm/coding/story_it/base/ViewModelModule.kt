package mvvm.coding.story_it.base

import androidx.lifecycle.ViewModel
import mvvm.coding.story_it.ui.main.MainViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val viewModelModule = Kodein.Module(name = "viewModelModule") {
    bind<ViewModel>(tag = MainViewModel::class.java.simpleName) with provider {
        MainViewModel(instance())
    }
}