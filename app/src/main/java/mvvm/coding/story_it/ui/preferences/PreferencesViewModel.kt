package mvvm.coding.story_it.ui.preferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import mvvm.coding.story_it.base.Coroutines
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.data.db.repository.GameRepository
import java.util.prefs.Preferences
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
    private fun setPreferences(){
        val preferences = gameRepository.getGamePreferences()

    }

     private fun loadPlayers() {
        Coroutines.ioThenMain({
            playerList = gameRepository.getPlayers().toMutableList()
        }) {
            _players.value = playerList
        }
    }




}
