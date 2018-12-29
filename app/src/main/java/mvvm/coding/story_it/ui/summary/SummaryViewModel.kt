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
        votersIterator.value = 1

    }
    private fun observeIterator(){
        votersIterator.observeForever {
            if(it <= votersList.value!!.size){
                _currentVoter.value = votersList.value!![it-1]
                _voterName.value = "Voter: ${_currentVoter.value!!.name}"
            }

        }
    }

    private fun getGameDataFromDB(){
        Coroutines.ioThenMain({
            gameEntry = gameRepository.getGameEntry()
        }){
            _gameModel.value = Converters.jsonStringToGameModel(gameEntry.gameStringJson)
            initializeDataToBeShown()
            observeIterator()
        }
    }

    private fun initializeDataToBeShown() {
        _currentRound.value = _gameModel.value!!.rounds.last { round -> round.wasCurrent }
        _summaryName.value = "Summary of Round ${_currentRound.value!!.id}"
        _story.value = _currentRound.value!!.turns.last().words
        _storyString.value = story.value!!.joinToString(" ") { word -> word.text }
        votersList.value = getPlayers()
        _currentVoter.value = votersList.value!![votersIterator.value!!.minus(1)]
        _voterName.value = "Voter: ${_currentVoter.value!!.name}"
    }
    private fun getPlayers() : List<Player>{
        var players : List<Player>
        players = _gameModel.value!!.rounds[0].turns.map { turn -> turn.player }
        return players
    }
    fun vote(){
        votersIterator.value = votersIterator.value!!.plus(1)
    }

}
