package mvvm.coding.story_it.data.db.entity

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Entity(tableName = "players",indices = [Index("id_p")])
data class Player(
    @ColumnInfo(name = "name")
    val name: String
){
    @ColumnInfo(name = "id_p")
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    @Ignore
    var color: MutableLiveData<Int> = MutableLiveData()
    @Ignore
    var isChosen : MutableLiveData<Boolean> = MutableLiveData()

}
