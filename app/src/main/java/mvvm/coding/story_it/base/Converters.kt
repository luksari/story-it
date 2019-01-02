package mvvm.coding.story_it.base

import android.util.Log
import androidx.room.TypeConverter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mvvm.coding.story_it.data.model.GameModel
import java.lang.Exception
import java.lang.reflect.Type
import java.util.*

class Converters {



    companion object {
        private val JSON = jacksonObjectMapper()
        @TypeConverter
        fun jsonStringToGameModel(data: String) : GameModel {
            if (data.isEmpty()) {Log.e("CONVERTER", "Was not able to convert EmptyString to GameModel")}
            return JSON.readValue(data, GameModel::class.java)
        }
        @TypeConverter
        fun gameModelToJson(gameModel: GameModel): String = JSON.writeValueAsString(gameModel)
    }


}