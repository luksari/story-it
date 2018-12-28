package mvvm.coding.story_it.ui.round

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mvvm.coding.story_it.data.model.GameModel

class RoundViewModel : ViewModel() {

    private val _gameModel : MutableLiveData<GameModel> = MutableLiveData()
    val gameModel : LiveData<GameModel>
        get() = _gameModel


}
