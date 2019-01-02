package mvvm.coding.story_it.ui.summary

import android.text.SpannableString
import android.text.SpannableStringBuilder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mvvm.coding.story_it.R
import mvvm.coding.story_it.base.Converters
import mvvm.coding.story_it.base.Coroutines
import mvvm.coding.story_it.data.db.entity.Game
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.data.db.entity.Score
import mvvm.coding.story_it.data.db.repository.GameRepository
import mvvm.coding.story_it.data.model.GameModel
import mvvm.coding.story_it.data.model.Round
import mvvm.coding.story_it.data.model.Word

class SummaryViewModel(private val gameRepository: GameRepository) : ViewModel() {

    private var adapter : OptionsListAdapter? = null

    private val _stringBuilder = MutableLiveData<SpannableStringBuilder>()
    val stringBuilder: LiveData<SpannableStringBuilder>
        get() = _stringBuilder

    val isBuilderLoaded = MutableLiveData<Boolean>()

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

    private val _listOfPlayersToVoteFor = MutableLiveData<List<Player>>()
    val listOfPlayersToVoteFor : LiveData<List<Player>>
        get() = _listOfPlayersToVoteFor

    private val _summaryHasEnded : MutableLiveData<Boolean> = MutableLiveData()
    val summaryHasEnded : LiveData<Boolean>
        get() = _summaryHasEnded
    private val _gameHasEnded : MutableLiveData<Boolean> = MutableLiveData()
    val gameHasEnded : LiveData<Boolean>
        get() = _gameHasEnded
    private val _lastSummaryHasEnded: MutableLiveData<Boolean> = MutableLiveData()
    val lastSummaryHasEnded: LiveData<Boolean>
        get() = _lastSummaryHasEnded


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
        adapter = OptionsListAdapter(R.layout.options_list_item, this)

        getGameDataFromDB()
        _summaryHasEnded.value = false
        _gameHasEnded.value = false
        _lastSummaryHasEnded.value = false
        isBuilderLoaded.value = false
        _stringBuilder.value = SpannableStringBuilder()
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
        var scores: List<Score>? = null
        Coroutines.ioThenMain({
            scores = gameRepository.getScores()
        }) {

        }
    }

    //region Adapter setter getter
    fun setAdapter(options: List<Player>){
        this.adapter?.setOptions(options)
        this.adapter?.notifyDataSetChanged()
    }
    fun getAdapter() = adapter
    //endregion
    fun getOption(position: Int) = listOfPlayersToVoteFor.value!![position]

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
                _listOfPlayersToVoteFor.value = votersList.value!!.filter { voter -> voter != _currentVoter.value }
                listOfPlayersToVoteFor.value!!.forEach { it.isChosen.value=false } //initally, need to check it in entitity
            } else if (it > votersList.value!!.size && currentRound.value == gameModel.value!!.rounds.last()) {
                _lastSummaryHasEnded.value = true
            } else{
                _summaryHasEnded.value = true
            }
        }

        _story.observeForever{
            updateStringBuilder()
        }

    }

    private fun updateStringBuilder() {
        isBuilderLoaded.value = false
        val listOfWords: List<Word> = _story.value!!
        var coloredText: SpannableString
        var color: Int
        for (w in listOfWords) {
            coloredText = SpannableString(w.text + " ")
            color = w.owner.color.value as Int
            coloredText.setSpan(ForegroundColorSpan(color), 0, w.text.length + 1, 0)
            _stringBuilder.value?.append(coloredText)
        }
        isBuilderLoaded.value = true
    }
    private fun getPlayers() : List<Player>{
        return gameModel.value!!.rounds[0].turns.map { turn -> turn.player }
    }
    fun vote(){
        var score: Score
        var prevScore: Score? = null

        val chosenPlayers =  listOfPlayersToVoteFor.value!!.filter { player -> player.isChosen.value!! }
        val points = if (chosenPlayers.isEmpty()) {
            0
        } else {
            (10/chosenPlayers.size).toInt()
        }

            Coroutines.ioThenMain({
                chosenPlayers.forEach {
                    prevScore = gameRepository.getScoreOf(it.id!!)
                    score = if (prevScore != null)
                        Score(prevScore!!.id_s, prevScore!!.playerId, prevScore!!.points + points)
                    else
                        Score(it.id!!, it.id!!, points)

                            gameRepository.addScore(score)
                }
            }) {}
        votersIterator.value = votersIterator.value!!.plus(1)
    }

    fun showGameSummary(){
        _summaryName.value = "Summary of Game"
        _story.value = _gameModel.value!!.rounds.map { round -> round.storyPart }.flatten()
        votersIterator.value = 1

    }

}
