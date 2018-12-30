package mvvm.coding.story_it.data.model

import androidx.lifecycle.LiveData
import java.util.*

data class Round(val id: Int, var storyPart: List<Word>, val turns: List<Turn>, var wasCurrent : Boolean = false)
