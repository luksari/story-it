package mvvm.coding.story_it.data.model

import mvvm.coding.story_it.data.db.entity.Player

data class Word(val id: Int, val text: String, val owner: Player)
