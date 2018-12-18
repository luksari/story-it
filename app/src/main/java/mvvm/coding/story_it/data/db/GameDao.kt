package mvvm.coding.story_it.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mvvm.coding.story_it.data.db.entity.CURRENT_GAME_ID
import mvvm.coding.story_it.data.db.entity.Game

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(game: Game)

    @Query("SELECT * FROM games WHERE id = $CURRENT_GAME_ID")
    fun getCurrentGame() : Game

}