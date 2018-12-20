package mvvm.coding.story_it.data.db.entity

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Entity(tableName = "players",indices = [Index("id_p")])
data class Player(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_p")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String
){

    @Ignore
    var color: MutableLiveData<Int> = MutableLiveData()
    @Ignore
    var isChosen : MutableLiveData<Boolean> = MutableLiveData()

}
