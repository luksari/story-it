package mvvm.coding.story_it.data.db.entity

import androidx.room.*

@Entity(tableName = "scores",
        foreignKeys = [ForeignKey(entity = Player::class,
            parentColumns = arrayOf("id_p"),
            childColumns = arrayOf("player_id"),
            onDelete = ForeignKey.CASCADE)],
        indices = [Index("player_id")]
)
data class Score(
    @PrimaryKey(autoGenerate = true)
    val id_s: Long,
    @ColumnInfo(name = "player_id") val playerId: Long,
    val points: Int
)

