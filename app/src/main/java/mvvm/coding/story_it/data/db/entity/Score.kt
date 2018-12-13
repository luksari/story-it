package mvvm.coding.story_it.data.db.entity

import androidx.room.*

@Entity(tableName = "scores",
        foreignKeys = arrayOf(
            ForeignKey(entity = Player::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("player_id"),
        onDelete = ForeignKey.CASCADE)
        ))
data class Score(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "player_id")
    val playerId: Long,
    val points: Int
)

