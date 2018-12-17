package mvvm.coding.story_it.ui.main

import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import mvvm.coding.story_it.base.Coroutines
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.data.db.repository.GameRepository
import kotlin.coroutines.CoroutineContext

class MainViewModel(private val gameRepository: GameRepository) : ViewModel() {

    // Define paren Job and Coroutine Context
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    private var _players: MutableLiveData<List<Player>> = MutableLiveData<List<Player>>()
    val players : LiveData<List<Player>> get() = _players

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    @UiThread
    fun addPlayer(player: Player) { Coroutines.ioThenMain({
        gameRepository.addPlayer(player)
    }){ } }

    @UiThread
    fun loadPlayers() : LiveData<List<Player>> {
        Coroutines.ioThenMain({
            gameRepository.getPlayers()
        }) {
            _players.value = it
        }
        return players
    }

}