package mvvm.coding.story_it.data.db.repository

import androidx.annotation.WorkerThread
import mvvm.coding.story_it.data.db.entity.Game
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.data.db.entity.Score

interface GameRepository {

    @WorkerThread
    fun upsertGame(game: Game)
    @WorkerThread
    fun getGameEntry(): Game

    @WorkerThread
    fun getPlayers() : List<Player>

    fun getPlayerOf(id: Long) : Player
    fun getPlayerOf(name: String) : Player
    @WorkerThread
    fun getScores():List<Score>

    fun getScoreOf(id: Long): Score?
    @WorkerThread
    fun addPlayer(player: Player)
    @WorkerThread
    fun addScore(score: Score)
    @WorkerThread
    fun deletePlayers()


}