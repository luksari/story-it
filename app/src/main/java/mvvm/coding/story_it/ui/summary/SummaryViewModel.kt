package mvvm.coding.story_it.ui.summary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import mvvm.coding.story_it.base.Converters
import mvvm.coding.story_it.base.Coroutines
import mvvm.coding.story_it.data.db.entity.Game
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.data.db.repository.GameRepository
import mvvm.coding.story_it.data.model.GameModel
import mvvm.coding.story_it.data.model.Round
import mvvm.coding.story_it.data.model.Word

class SummaryViewModel(private val gameRepository: GameRepository) : ViewModel() {

    private lateinit var gameEntry : Game
    private val _gameModel = MutableLiveData<GameModel>()
    val gameModel : LiveData<GameModel>
        get() = _gameModel

    private val _summaryName = MutableLiveData<String>()
    val summaryName : LiveData<String>
        get() = _summaryName

    private val _voterName = MutableLiveData<String>()
    val voterName : LiveData<String>
        get() = _voterName

    private val _currentVoter = MutableLiveData<Player>()
    val currentVoter : LiveData<Player>
        get() = _currentVoter

    private val votersList = MutableLiveData<List<Player>>()
    private val votersIterator : MutableLiveData<Int> = MutableLiveData()
    private val listOfPlayersToVoteFor = MutableLiveData<List<Player>>()

    private val _summaryHasEnded : MutableLiveData<Boolean> = MutableLiveData()
    val summaryHasEnded : LiveData<Boolean>
        get() = _summaryHasEnded
    private val _gameHasEnded : MutableLiveData<Boolean> = MutableLiveData()
    val gameHasEnded : LiveData<Boolean>
        get() = _gameHasEnded

    private val _currentRound = MutableLiveData<Round>()
    val currentRound : LiveData<Round>
        get() = _currentRound

    private val _story = MutableLiveData<List<Word>>()
    val story : LiveData<List<Word>>
        get() = _story
    private val _storyString = MutableLiveData<String>()
    val storyString : LiveData<String>
        get() = _storyString


    init {
        getGameDataFromDB()
        _summaryHasEnded.value = false
        _gameHasEnded.value = false
        votersIterator.value = 1

    }

    private fun getGameDataFromDB(){
        Coroutines.ioThenMain({
            gameEntry = gameRepository.getGameEntry()
        }){
            _gameModel.value = Converters.jsonStringToGameModel(gameEntry.gameStringJson)
            initializeDataToBeShown()
            initObservers()
        }
    }

    private fun initializeDataToBeShown() {
        _currentRound.value = _gameModel.value!!.rounds.last { round -> round.wasCurrent }
        _summaryName.value = "Summary of Round ${_currentRound.value!!.id}"
        _story.value = _currentRound.value!!.storyPart
        votersList.value = getPlayers()
    }
    private fun initObservers(){
        _currentRound.observeForever{
            round->
            if(round.id == _gameModel.value!!.rounds.last().id) _gameHasEnded.value = true
        }

        votersIterator.observeForever {
            if(it <= votersList.value!!.size){
                _currentVoter.value = votersList.value!![it-1]
                _voterName.value = "Voter: ${_currentVoter.value!!.name}"
                listOfPlayersToVoteFor.value = votersList.value!!.filter { voter -> voter != _currentVoter.value }
                Log.d("OPTIONS", listOfPlayersToVoteFor.value.toString())
            }
            else{
                _summaryHasEnded.value = true
            }
        }

        _story.observeForever{
            _storyString.value = it.joinToString(" ") { word -> word.text }
        }

    }
    private fun getPlayers() : List<Player>{
        return gameModel.value!!.rounds[0].turns.map { turn -> turn.player }
    }
    fun vote(){
        votersIterator.value = votersIterator.value!!.plus(1)
        // To implement voting functionality
    }
    fun showGameSummary(){
        _summaryName.value = "Summary of Game"
        _story.value = _gameModel.value!!.rounds.map { round -> round.storyPart }.flatten()
        votersIterator.value = 1
    }

}
