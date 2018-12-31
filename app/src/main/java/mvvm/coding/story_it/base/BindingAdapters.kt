package mvvm.coding.story_it.base

import android.text.TextUtils
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mvvm.coding.story_it.data.db.entity.Player
import kotlin.math.max

@BindingAdapter("setAdapter")
fun bindRecyclerViewAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>){
    println("CONTEXT: "+adapter)
    recyclerView.hasFixedSize()
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
}

// region Adapters for a Validation
@BindingAdapter("roundNumberValidator")
fun roundNumberValidator(editText: EditText, value: Int){
    val minVal = 2
    val maxVal = 35
    if((editText.text.toString() == "" )){
        editText.error = null
        return
    }
    when {
        editText.text.toString().toInt() < minVal -> editText.error = "Number of Rounds must be bigger than $minVal"
        editText.text.toString().toInt() > maxVal -> editText.error = "Number of Rounds must be smaller than $maxVal"
        else -> editText.error = null
    }
}
@BindingAdapter("charsNumberValidator")
fun charsNumberValidator(editText: EditText, value: Int){
    val minVal = 10
    val maxVal = 140
    if((editText.text.toString() == "" )){
        editText.error = null
        return
    }
    when {
        editText.text.toString().toInt() < minVal -> editText.error = "Number of characters must be bigger than $minVal"
        editText.text.toString().toInt() > maxVal -> editText.error = "Number of characters must be smaller than $maxVal"
        else -> editText.error = null
    }
}
@BindingAdapter("wordsNumberValidator")
fun wordsNumberValidator(editText: EditText, value: Int){
    val minVal = 1
    val maxVal = 4
    if((editText.text.toString() == "" )){
        editText.error = null
        return
    }
    when {
        editText.text.toString().toInt() < minVal -> editText.error = "Number of words must be bigger than $minVal"
        editText.text.toString().toInt() > maxVal -> editText.error = "Number of words must be smaller than $maxVal"
        else -> editText.error = null
    }
}
@BindingAdapter(value = ["playerName"], requireAll = false)
fun playerNameValidator(editText: EditText, playerName: String?){
    val minLength = 2
    val maxLength = 15
    if(playerName == null){
        editText.error = null
        return
    }
    when {
        editText.text.toString().length < minLength -> editText.error = "Length of nickname must be longer than $minLength"
        editText.text.toString().length > maxLength -> editText.error = "Length of nickname must be shorter than $maxLength"
        else -> editText.error = null
    }
}

//endregion