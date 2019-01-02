package mvvm.coding.story_it.data.model

import mvvm.coding.story_it.data.db.entity.Player

data class Turn(val id: Int, val player: Player, var words: List<Word>)