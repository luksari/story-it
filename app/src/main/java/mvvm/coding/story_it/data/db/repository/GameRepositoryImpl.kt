package mvvm.coding.story_it.data.db.repository

import android.util.Log
import androidx.annotation.WorkerThread
import mvvm.coding.story_it.data.db.GameDao
import mvvm.coding.story_it.data.db.PlayerDao
import mvvm.coding.story_it.data.db.ScoreDao
import mvvm.coding.story_it.data.db.entity.Game
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.data.db.entity.Score

class GameRepositoryImpl(
    private val playerDao: PlayerDao,
    private val scoreDao: ScoreDao,
    private val gameDao: GameDao)
    : GameRepository {


    /*
    * Every of those getters sets the value of mutable property from the _gameModel.value.*
    * And then returns LiveData<*> to make it Observable for ViewModels.
    * */



    //region Database Getters Methods

    override fun getPlayers(): List<Player> {
        return playerDao.getPlayers()
    }

    override fun getPlayerOf(id: Long): Player{
        var player: Player? = null
        try {
            player = playerDao.getPlayers().singleOrNull { player -> player.id == id }
        }catch (ex: Exception){
            Log.e("REPOSITORY", ex.localizedMessage)
        }
        return player!!
    }
    override fun getPlayerOf(name: String): Player{
        var player: Player? = null
        try {
            player = playerDao.getPlayers().singleOrNull { player -> player.name == name }
        }catch (ex: Exception){
            Log.e("REPOSITORY", ex.localizedMessage)
        }
        return player!!
    }
    @WorkerThread
    override fun getScores(): List<Score> = scoreDao.getScores()

    @WorkerThread
    override fun deletePlayers() {
        playerDao.deletePlayers()
    }

    override fun getScoreOf(id: Long): Score? {
        var score: Score? = null
        try {
            score = scoreDao.getScores().singleOrNull { score -> score.playerId == id }
        }catch (ex: Exception){
            Log.e("REPOSITORY", ex.localizedMessage)
        }
        return score
    }




    //endregion

    //region Database Insert Methods
    @WorkerThread
    override fun addPlayer(player: Player) {
        playerDao.addPlayer(player)
    }
    @WorkerThread
    override fun addScore(score: Score) {
        scoreDao.addScore(score)
    }
    @WorkerThread
    override fun upsertGame(game: Game) {
        gameDao.upsert(game)
    }
    @WorkerThread
    override fun getGameEntry(): Game {
        return gameDao.getCurrentGame()
    }



}
