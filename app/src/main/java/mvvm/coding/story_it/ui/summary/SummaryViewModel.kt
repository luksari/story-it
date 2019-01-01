package mvvm.coding.story_it.ui.summary

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
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

    val builder = SpannableStringBuilder()
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
        isBuilderLoaded.value = false
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
            Log.d("SCORES", scores.toString())
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
        //---------------------------------------------------------------------------------------------------------------
        //                              HERE PUT PLAYER COLORS
        val listOfWords: List<Word> = _currentRound.value!!.storyPart
        var coloredText: SpannableString
        var color: Int
        for(w in listOfWords){
            coloredText = SpannableString(w.text)
            color = w.owner.color.value as Int
            coloredText.setSpan(ForegroundColorSpan(color), 0, w.text.length, 0)
            builder.append(coloredText)
        }
        //mTextView.setText(builder, BufferType.SPANNABLE);
        //_storyString.value = builder.toString()



        //---------------------------------------------------------------------------------------------------------------
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
            }
            else{
                _summaryHasEnded.value = true
            }
        }

        _story.observeForever{
            _storyString.value = it.joinToString(" ") { word -> word.text }
            isBuilderLoaded.value = true
        }

    }
    private fun getPlayers() : List<Player>{
        return gameModel.value!!.rounds[0].turns.map { turn -> turn.player }
    }
    fun vote(){
        var score: Score
        var prevScore: Score? = null
        val chosenPlayer = listOfPlayersToVoteFor.value!!.single { player -> player.isChosen.value!! }

        Coroutines.ioThenMain({
            prevScore = gameRepository.getScoreOf(chosenPlayer.id!!)
        }) {
            score = if (prevScore != null)
                Score(prevScore!!.id_s, prevScore!!.playerId, prevScore!!.points + 10)
            else
                Score(chosenPlayer.id, chosenPlayer.id!!, 10)

            Coroutines.ioThenMain({
                gameRepository.addScore(score)
            }) {
                votersIterator.value = votersIterator.value!!.plus(1)
            }
        }
    }
    fun showGameSummary(){
        _summaryName.value = "Summary of Game"
        _story.value = _gameModel.value!!.rounds.map { round -> round.storyPart }.flatten()
        votersIterator.value = 1
        isBuilderLoaded.value = true
    }

}
