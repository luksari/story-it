package mvvm.coding.story_it.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.data.db.entity.Score

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPlayer(player: Player)

    @Query("SELECT * FROM players")
    fun getPlayers(): List<Player>

    @Query("DELETE FROM players")
    fun deletePlayers()
}