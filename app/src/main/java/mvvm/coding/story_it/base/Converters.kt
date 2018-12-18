package mvvm.coding.story_it.base

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mvvm.coding.story_it.data.model.GameModel
import java.lang.Exception
import java.lang.reflect.Type
import java.util.*

class Converters {



    companion object {
        private val gson = Gson()
        @TypeConverter
        fun jsonStringToGameModel(data: String) : GameModel {
            if (data == null) {Log.e("CONVERTER", "Was not able to convert null to GameModel")}
            val gameModelType = object : TypeToken<GameModel>() {
            }.type
            return gson.fromJson(data, gameModelType)
        }
        @TypeConverter
        fun gameModelToJson(gameModel: GameModel) = gson.toJson(gameModel)
    }


}