package mvvm.coding.story_it.data.db.repository

import android.util.Log
import androidx.annotation.WorkerThread

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import mvvm.coding.story_it.data.db.PlayerDao
import mvvm.coding.story_it.data.db.ScoreDao
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.data.db.entity.Score
import mvvm.coding.story_it.data.model.Game
import java.lang.Exception




class GameRepositoryImpl(
    private val playerDao: PlayerDao,
    private val scoreDao: ScoreDao)
    : GameRepository {

    @WorkerThread
    override fun getPlayers(): List<Player> {
           return playerDao.getPlayers()
    }

    override fun getPlayerOf(id: Long): LiveData<Player> {
        val mutableLiveData = MutableLiveData<Player>()
        val player: Player?
        try {
            player = playerDao.getPlayers().single { player -> player.id == id  }
            mutableLiveData.value = player
        }catch (ex: Exception){
            Log.e("REPOSITORY", ex.localizedMessage)
        }
        return mutableLiveData
    }
    @WorkerThread
    override fun getScores(): LiveData<List<Score>> = scoreDao.getScores()

    override fun getScoreOf(id: Long): LiveData<Score> {
        val mutableLiveData = MutableLiveData<Score>()
        val score: Score?
        try {
            score = scoreDao.getScores().value?.single { score -> score.playerId == id }
            mutableLiveData.value = score
        }catch (ex: Exception){
            Log.e("REPOSITORY", ex.localizedMessage)
        }
        return mutableLiveData
    }
    @WorkerThread
    override fun addPlayer(player: Player) {
        playerDao.addPlayer(player)
    }
    @WorkerThread
    override fun addScore(score: Score) {
        scoreDao.addScore(score)
    }

}
