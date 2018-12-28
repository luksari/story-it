package mvvm.coding.story_it.ui.round

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import mvvm.coding.story_it.base.Converters
import mvvm.coding.story_it.base.Coroutines
import mvvm.coding.story_it.data.db.entity.Game
import mvvm.coding.story_it.data.db.repository.GameRepository
import mvvm.coding.story_it.data.model.GameModel
import mvvm.coding.story_it.data.model.Round
import mvvm.coding.story_it.data.model.Turn
import mvvm.coding.story_it.data.model.Word

class RoundViewModel(private val gameRepository: GameRepository) : ViewModel() {

    private val _roundName : MutableLiveData<String> = MutableLiveData()
    val roundName : LiveData<String>
        get() = _roundName

    private val _previousTurnStoryPart : MutableLiveData<String> = MutableLiveData()
    val previousTurnStoryPart : LiveData<String>
        get() = _previousTurnStoryPart

    private val _currentTurn : MutableLiveData<Turn> = MutableLiveData()
    val currentTurn : LiveData<Turn>
        get() = _currentTurn

    private val prevWords : MutableList<Word> = mutableListOf()
    private val currentWords : MutableList<Word> = mutableListOf()

    val currentTurnStoryPart : MutableLiveData<String> = MutableLiveData()

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

    private val  turnsIterator : MutableLiveData<Int> = MutableLiveData()
    private val  roundsIterator : MutableLiveData<Int> = MutableLiveData()

    private val _hasRoundEnded : MutableLiveData<Boolean> = MutableLiveData()
    val hasRoundEnded : LiveData<Boolean>
        get() = _hasRoundEnded

    private fun getGameDataFromDB(){
        Coroutines.ioThenMain({
            gameEntry = gameRepository.getGameEntry()
        }){
             _gameModel.value = Converters.jsonStringToGameModel(gameEntry.gameStringJson)
            initializeDataToBeShown()
        }
    }
    init {
        getGameDataFromDB()
        turnsIterator.value = 1
        roundsIterator.value = 1
    }

    private fun initializeDataToBeShown(){
        _currentRound.value = _gameModel.value!!.rounds.single { round -> round.id == roundsIterator.value }
        _roundName.value = "Round ${_currentRound.value!!.id}"
        _currentTurn.value = _currentRound.value!!.turns.single { turn -> turn.id == turnsIterator.value }
        _currentPlayerName.value = _currentTurn.value!!.player.name
        _previousTurnStoryPart.value = " "

        turnsIterator.observeForever {
            if(it > _currentRound.value!!.turns.size) {
                roundsIterator.value = roundsIterator.value!!.plus(1)
                turnsIterator.value = 1
                // @TODO Go to round sunmary

            }
            else{
                _currentTurn.value = _currentRound.value!!.turns.single { turn -> turn.id == it }
                _currentPlayerName.value = _currentTurn.value!!.player.name
                _previousTurnStoryPart.value = currentWords.takeLast(_gameModel.value!!.preferences.numberOfWordsShown).joinToString(" ") { word ->  word.text }
                currentTurnStoryPart.value = ""
            }
        }
        roundsIterator.observeForever {
            _currentRound.value = _gameModel.value!!.rounds.single { round -> round.id == roundsIterator.value }
            _roundName.value = "Round ${_currentRound.value!!.id}"
        }
    }
    fun nextPlayer(){
        val tempStringArray = currentTurnStoryPart.value!!.split(" ")
        for (i in 0 until tempStringArray.size){
            currentWords.add(Word(i+1, tempStringArray[i], _currentTurn.value!!.player))
        }
        Log.d("GAME", "ROUND: ${roundsIterator.value}, TURN: ${turnsIterator.value}")
        Log.d("GAME", _gameModel.value!!.rounds.toString())

        turnsIterator.value = turnsIterator.value!!.plus(1)
    }

}
