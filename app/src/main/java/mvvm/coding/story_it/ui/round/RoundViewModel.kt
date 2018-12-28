package mvvm.coding.story_it.ui.round

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import mvvm.coding.story_it.base.Converters
import mvvm.coding.story_it.base.Coroutines
import mvvm.coding.story_it.data.db.entity.Game
import mvvm.coding.story_it.data.db.repository.GameRepository
import mvvm.coding.story_it.data.model.GameModel
import mvvm.coding.story_it.data.model.Round

class RoundViewModel(private val gameRepository: GameRepository) : ViewModel() {

    private lateinit var round: Round
    private val roundList : MutableList<Round> = mutableListOf()

    private lateinit var gameEntry : Game
    private val _gameModel : MutableLiveData<GameModel> = MutableLiveData()
    val gameModel : LiveData<GameModel>
        get() = _gameModel

    private fun getGameDataFromDB(){
        Coroutines.ioThenMain({
            gameEntry = gameRepository.getGameEntry()
        }){
             _gameModel.value = Converters.jsonStringToGameModel(gameEntry.gameStringJson)
            Log.d("GAME", _gameModel.value.toString())
        }
    }
    init {
        getGameDataFromDB()

    }


}
