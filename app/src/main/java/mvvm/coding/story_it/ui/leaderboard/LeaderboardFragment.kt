package mvvm.coding.story_it.ui.leaderboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.preferences_fragment.*
import mvvm.coding.story_it.R
import mvvm.coding.story_it.databinding.LeaderboardFragmentBinding
import mvvm.coding.story_it.ui.preferences.PreferencesFragmentDirections
import org.koin.android.viewmodel.ext.android.viewModel

class LeaderboardFragment : Fragment() {
    companion object {
        fun newInstance() = LeaderboardFragment()
    }

    private val viewModel: LeaderboardViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: LeaderboardFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.leaderboard_fragment, container, false)
        binding.viewModel = viewModel

        binding.setLifecycleOwner(this)
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListUpdate()

        back_btn.setOnClickListener { findNavController().navigate(LeaderboardFragmentDirections.actionLeaderboardFragmentToStartFragment()) }
    }
    private fun setupListUpdate(){
        viewModel.scores.observe(this, Observer {
                scores-> if(scores.isNotEmpty())
            viewModel.setAdapterScores(scores)
        })
        viewModel.players.observe(this, Observer {
                players-> if(players.isNotEmpty())
            viewModel.setAdapterPlayers(players)
        })
    }


}
