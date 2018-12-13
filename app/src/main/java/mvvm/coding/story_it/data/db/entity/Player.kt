package mvvm.coding.story_it.data.db.entity

import android.graphics.Color
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String = "",
    @Ignore
    val color: Color
)
