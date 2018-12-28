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

    private val _roundName : MutableLiveData<String> = MutableLiveData()
    val rondName : LiveData<String>
        get() = _roundName

    private val _turnStoryPart : MutableLiveData<String> = MutableLiveData()
    val storyPart : LiveData<String>
        get() = _turnStoryPart

    private val _currentPlayerName : MutableLiveData<String> = MutableLiveData()
    val currentPlayerName : LiveData<String>
        get() = _currentPlayerName

    private val _currentRound : MutableLiveData<Round> = MutableLiveData()
    val currentRound : LiveData<Round>
        get() = _currentRound

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

    private fun initializeDataToBeShown(){
        _currentRound.value = _gameModel.value!!.rounds.first()
        _roundName.value = "Round ${_currentRound.value!!.id}"

    }


}
