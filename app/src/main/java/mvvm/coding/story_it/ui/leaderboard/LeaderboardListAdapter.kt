package mvvm.coding.story_it.ui.leaderboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import mvvm.coding.story_it.R
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.data.db.entity.Score
import mvvm.coding.story_it.databinding.LeaderboardListItemBinding


    class LeaderboardListAdapter(@LayoutRes private val layoutId: Int, private val leaderboardViewModel: LeaderboardViewModel) : RecyclerView.Adapter<LeaderboardListAdapter.LeaderboardViewHolder>() {
        private var players : List<Player>? = leaderboardViewModel.players.value
        private var scores : List<Score>? = leaderboardViewModel.scores.value
        fun setScores(scores: List<Score>){
            this.scores = scores
        }
        fun setPlayers(players:List<Player>){
            this.players=players
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
            val binding : LeaderboardListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.leaderboard_list_item, parent, false)
            return LeaderboardViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return scores?.size ?: 0
        }

        override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
            holder.bind(leaderboardViewModel, position)
        }

        inner class LeaderboardViewHolder(private val binding: LeaderboardListItemBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(viewModel: LeaderboardViewModel, position: Int){
                binding.viewModel = viewModel
                binding.position = position
                binding.executePendingBindings()
            }
        }
    }