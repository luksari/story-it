package mvvm.coding.story_it.ui.round

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    val MAX_CHARS = MutableLiveData<Int>()

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
            MAX_CHARS.value = _gameModel.value!!.preferences.maxChars
            initializeDataToBeShown()
        }
    }
    init {
        getGameDataFromDB()
        _hasRoundEnded.value = false

    }

    private fun initializeDataToBeShown(){
        turnsIterator.value = 1
        roundsIterator.value = _gameModel.value?.rounds?.findLast { round -> round.wasCurrent }?.id?.plus(1) ?: 1
        _currentRound.value = _gameModel.value!!.rounds.single { round -> round.id == roundsIterator.value }
        _roundName.value = "Round ${_currentRound.value!!.id}"
        _currentTurn.value = _currentRound.value!!.turns.single { turn -> turn.id == turnsIterator.value }
        _currentPlayerName.value = _currentTurn.value!!.player.name
        _previousTurnStoryPart.value = " "

        turnsIterator.observeForever {
            if(it > _currentRound.value!!.turns.size) {
                roundsIterator.value = roundsIterator.value!!.plus(1)
                turnsIterator.value = 1
                _hasRoundEnded.value = true
            }
            else{
                if(roundsIterator.value!! <= _gameModel.value!!.rounds.size){
                    _currentTurn.value = _currentRound.value!!.turns.single { turn -> turn.id == it }
                    _currentPlayerName.value = _currentTurn.value!!.player.name
                    _previousTurnStoryPart.value = currentWords.takeLast(_gameModel.value!!.preferences.numberOfWordsShown).joinToString(" ") { word ->  word.text }
                    currentTurnStoryPart.value = ""
                }
            }
        }
        roundsIterator.observeForever {
            if(it < _gameModel.value!!.rounds.size){
                _currentRound.value = _gameModel.value!!.rounds.single { round -> round.id == roundsIterator.value }
                _roundName.value = "Round ${_currentRound.value!!.id}"
            }
            else{

            }
        }
    }
    fun nextPlayer(){
        val tempStringArray = currentTurnStoryPart.value!!.split(" ")
        for (i in 0 until tempStringArray.size){
            currentWords.add(Word(i+1, tempStringArray[i], _currentTurn.value!!.player))
        }
        _currentTurn.value?.words = currentWords
        gameModel.value!!.rounds[roundsIterator.value!!.minus(1)].turns.toMutableList()[turnsIterator.value!!.minus(1)] = _currentTurn.value!!
        gameModel.value!!.rounds[roundsIterator.value!!.minus(1)].storyPart = currentWords
        Log.d("GAME", gameModel.value!!.rounds[roundsIterator.value!!.minus(1)].toString())

        upsertGame()

    }

    private fun upsertGame(){
        _gameModel.value!!.rounds[roundsIterator.value!!.minus(1)].wasCurrent = true
        val gameJson = Converters.gameModelToJson(_gameModel.value!!)
        val game = Game(gameJson)
        Coroutines.ioThenMain({
            gameRepository.upsertGame(game)
        }){
            turnsIterator.value = turnsIterator.value!!.plus(1)
        }
    }

}
