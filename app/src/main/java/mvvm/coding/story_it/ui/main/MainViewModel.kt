package mvvm.coding.story_it.ui.main

import androidx.lifecycle.ViewModel
import mvvm.coding.story_it.data.db.repository.GameRepository

class MainViewModel(private val gameRepositoryImpl: GameRepository) : ViewModel() {
    val text: String = "HELLO WORLD FROM VIEW MODEL"

}