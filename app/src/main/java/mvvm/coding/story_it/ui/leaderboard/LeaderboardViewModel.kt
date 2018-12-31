package mvvm.coding.story_it.ui.leaderboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import mvvm.coding.story_it.R
import mvvm.coding.story_it.base.Coroutines
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.data.db.entity.Score
import mvvm.coding.story_it.data.db.repository.GameRepository

class LeaderboardViewModel(private val gameRepository: GameRepository) : ViewModel() {

    private var playerList : MutableList<Player> = mutableListOf()
    private val _players : MutableLiveData<List<Player>> = MutableLiveData()
    val players : LiveData<List<Player>>
        get() = _players


    private var scoreList : MutableList<Score> = mutableListOf()
    private val _scores : MutableLiveData<List<Score>> = MutableLiveData()
    val scores : LiveData<List<Score>>
        get() = _scores

    private var adapter : LeaderboardListAdapter? = null
    fun getAdapter() = adapter
    fun setAdapter(scores: List<Score>){
        this.adapter?.setScores(scores)
        this.adapter?.notifyDataSetChanged()
    }
    fun getScore(position: Int) = scores.value!![position]

    init {
       println("LEADERBOARD LEAD")
       loadPlayers()
        loadScores()
        adapter = LeaderboardListAdapter( R.layout.leaderboard_list_item, this)


    }

    private fun loadPlayers() {

        Coroutines.ioThenMain({
            playerList = gameRepository.getPlayers().toMutableList()

        })
        {
            playerList.forEach { player-> println(player.name) }
            _players.value = playerList
        }
    }

    private fun loadScores(){

        Coroutines.ioThenMain({
            scoreList = gameRepository.getScores().toMutableList()

        })
        {
            var sortedList = scoreList.sortedWith(compareByDescending { it.points })

            _scores.value = sortedList
//            scoreList.forEach { score-> Log.d("POINT", score.points.toString()) }
            //_scores.value = scoreList
        }

    }


    fun getPlayerOfId(id: Int) = _players.value?.get(id)
    fun getScoreOfId(id: Int) = _scores.value?.get(id)


}
