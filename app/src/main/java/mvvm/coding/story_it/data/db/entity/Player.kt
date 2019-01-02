package mvvm.coding.story_it.data.db.entity

import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Entity(tableName = "players",indices = [Index("id_p")])
data class Player(
    @ColumnInfo(name = "id_p")
    @PrimaryKey(autoGenerate = true)
    var id: Long? = 0,
    @ColumnInfo(name = "name")
    val name: String
) {
    @Ignore
    var color: MutableLiveData<Int> = MutableLiveData()
    @Ignore
    var isChosen : MutableLiveData<Boolean> = MutableLiveData()

}
