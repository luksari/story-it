package mvvm.coding.story_it.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_GAME_ID = 0

@Entity(tableName = "games")
data class Game(val gameStringJson: String) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_GAME_ID
}
