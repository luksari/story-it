package mvvm.coding.story_it.data.model

import androidx.lifecycle.LiveData
import mvvm.coding.story_it.data.db.entity.Player

data class Preferences(val maxChars: Int,val numberOfWordsShown : Int, val numberOfRounds: Int,val turnOrderRandom : Boolean, val players: List<Player>)
