package mvvm.coding.story_it.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.data.db.entity.Score

@Dao
interface ScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addScore(score: Score)

    @Query("SELECT * FROM scores")
    fun getScores(): LiveData<List<Score>>
}