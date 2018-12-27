package mvvm.coding.story_it.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import mvvm.coding.story_it.data.db.entity.Game
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.data.db.entity.Score

@Database(
    entities = [Player::class, Score::class, Game::class],
    version = 1
)
abstract class GameDatabase : RoomDatabase(){
    abstract fun playerDao(): PlayerDao
    abstract fun scoreDao(): ScoreDao
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile private var instance : GameDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it }
        }
        fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                GameDatabase::class.java, "game-story-it.db").build()

    }
}