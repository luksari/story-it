package mvvm.coding.story_it.data.model

import mvvm.coding.story_it.data.db.entity.Player

data class Preferences(val name: String, val maxChars: Int, val numberOfRounds: Int, val players: List<Player>)
