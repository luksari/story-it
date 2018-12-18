package mvvm.coding.story_it.ui.preferences

import android.util.Log
import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import mvvm.coding.story_it.base.Converters
import mvvm.coding.story_it.base.Coroutines
import mvvm.coding.story_it.data.db.entity.Game
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.data.db.repository.GameRepository
import mvvm.coding.story_it.data.model.GameModel
import mvvm.coding.story_it.data.model.Preferences
import kotlin.coroutines.CoroutineContext

class PreferencesViewModel(private val gameRepository: GameRepository) : ViewModel() {

    // Define paren Job and Coroutine Context
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)


    val playerName : MutableLiveData<String> = MutableLiveData()
    val numberOfRounds : MutableLiveData<Int> = MutableLiveData()
    val numberOfWords : MutableLiveData<Int> = MutableLiveData()
    val numberOfCharacters : MutableLiveData<Int> = MutableLiveData()
    val isRandomOrder : MutableLiveData<Boolean> = MutableLiveData()

    private val _gameModel : MutableLiveData<GameModel> = MutableLiveData()
    val gameModel : LiveData<GameModel>
        get() = _gameModel

    private var playerList : MutableList<Player> = mutableListOf()
    private val _players : MutableLiveData<List<Player>> = MutableLiveData()
    val players : LiveData<List<Player>>
        get() = _players

    private val _preferences = MutableLiveData<Preferences>()
    val preferences : LiveData<Preferences>
        get() = _preferences

    init {
        loadPlayers()

    }

    fun initGameModel(){
        Coroutines.ioThenMain({
            return@ioThenMain gameRepository.getGameEntry()
        }) {
            _gameModel.value = Converters.jsonStringToGameModel(it?.gameStringJson ?: " ")

        }

    }

    fun addGame(){
        val preferences = Preferences(numberOfCharacters.value ?: 0, numberOfRounds.value ?: 0,isRandomOrder.value ?: false , listOf())
        _gameModel.value = GameModel(preferences)
        Log.i("PREFS", preferences.toString())

        if(gameModel.value != null) {
            val gameJson = Converters.gameModelToJson(gameModel.value!!)
            val game = Game(gameJson)
            Coroutines.io {
                gameRepository.upsertGame(game)
            }
        }

    }
     private fun loadPlayers() {
        Coroutines.ioThenMain({
            playerList = gameRepository.getPlayers().toMutableList()
        }) {
            _players.value = playerList
        }
    }




}
