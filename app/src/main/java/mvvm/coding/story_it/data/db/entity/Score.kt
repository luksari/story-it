package mvvm.coding.story_it.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score")
data class Score(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @Embedded(prefix = "player_")
    val player: Player,
    val points: Int
)

