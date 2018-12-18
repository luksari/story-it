package mvvm.coding.story_it.data.db.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.data.db.entity.Score
import mvvm.coding.story_it.data.model.Game
import mvvm.coding.story_it.data.model.Preferences
import mvvm.coding.story_it.data.model.Round

interface GameRepository {

    @WorkerThread
    fun getPlayers() : List<Player>

    fun getPlayerOf(id: Long) : LiveData<Player>
    @WorkerThread
    fun getScores() :LiveData<List<Score>>

    fun getScoreOf(id: Long) : LiveData<Score>
    @WorkerThread
    fun addPlayer(player: Player)
    @WorkerThread
    fun addScore(score: Score)

    // Functions for Game object not stored in DB
    fun getGame() : LiveData<Game>
    fun getGamePreferences() : LiveData<Preferences>
    fun getGameRounds() : LiveData<List<Round>>
    fun getCurrentGameRound() : LiveData<Round>
    fun getGamePlayers() : LiveData<List<Player>>
    fun getGameCurrentPlayer() : LiveData<Player>

}