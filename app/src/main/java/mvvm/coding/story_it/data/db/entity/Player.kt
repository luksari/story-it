package mvvm.coding.story_it.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player")
data class Player(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val colorHex: String
)
