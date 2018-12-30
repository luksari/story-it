package mvvm.coding.story_it.ui.summary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import mvvm.coding.story_it.R
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.databinding.OptionsListItemBinding
import mvvm.coding.story_it.databinding.PlayerListItemBinding
import mvvm.coding.story_it.ui.preferences.PreferencesViewModel

class OptionsListAdapter(@LayoutRes private val layoutId: Int, private val summaryViewModel: SummaryViewModel) : RecyclerView.Adapter<OptionsListAdapter.OptionViewHolder>() {
    private var options : List<Player>? = summaryViewModel.listOfPlayersToVoteFor.value

    fun setOptions(options: List<Player>){
        this.options = options
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val binding : OptionsListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.options_list_item, parent, false)
        return OptionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return options?.size ?: 0
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(summaryViewModel, position)
    }


    inner class OptionViewHolder(private val binding: OptionsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: SummaryViewModel, position: Int){
            binding.viewModel = viewModel
            binding.position = position
            binding.executePendingBindings()

        }

    }

}