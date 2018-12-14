package mvvm.coding.story_it.data.db.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import mvvm.coding.story_it.data.db.PlayerDao
import mvvm.coding.story_it.data.db.ScoreDao
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.data.db.entity.Score
import java.lang.Exception

class GameRepositoryImpl(
    private val playerDao: PlayerDao,
    private val scoreDao: ScoreDao)
    : GameRepository {


    override suspend fun getPlayers(): LiveData<List<Player>> = playerDao.getPlayers()

    override suspend fun getPlayerOf(id: Long): LiveData<Player> {
        val mutableLiveData = MutableLiveData<Player>()
        val player: Player?
        try {
            player = playerDao.getPlayers().value?.single { player -> player.id == id }
            mutableLiveData.value = player
        }catch (ex: Exception){
            Log.e("REPOSITORY", ex.localizedMessage)
        }
        return mutableLiveData
    }



    override suspend fun getScores(): LiveData<List<Score>> = scoreDao.getScores()

    override suspend fun getScoreOf(id: Long): LiveData<Score> {
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

    override suspend fun addPlayer(player: Player) = playerDao.addPlayer(player)

    override suspend fun addScore(score: Score) = scoreDao.addScore(score)

}