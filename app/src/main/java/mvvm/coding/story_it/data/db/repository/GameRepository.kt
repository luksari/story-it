package mvvm.coding.story_it.data.db.repository

import androidx.lifecycle.LiveData
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.data.db.entity.Score

interface GameRepository {
    suspend fun getPlayers() : LiveData<List<Player>>
    suspend fun getPlayerOf(id: Long) : LiveData<Player>
    suspend fun getScores() :LiveData<List<Score>>
    suspend fun getScoreOf(id: Long) : LiveData<Score>
    suspend fun addPlayer(player: Player)
    suspend fun addScore(score: Score)
}