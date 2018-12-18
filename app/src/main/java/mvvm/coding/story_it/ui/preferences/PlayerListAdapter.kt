package mvvm.coding.story_it.ui.preferences

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import mvvm.coding.story_it.R
import mvvm.coding.story_it.data.db.entity.Player

class PlayerListAdapter(@LayoutRes private val layoutId: Int, private val preferencesViewModel: PreferencesViewModel) : RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder>() {
    private var players : List<Player>? = preferencesViewModel.players.value

    fun setPlayers(players: List<Player>){
        this.players = players
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding : ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.player_list_item, parent, false)
        return PlayerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return players?.size ?: 0
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    inner class PlayerViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: PreferencesViewModel, position: Int){
            binding.setVariable(BR.viewModel, viewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()

        }

    }

}