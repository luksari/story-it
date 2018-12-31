package mvvm.coding.story_it.ui.leaderboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import mvvm.coding.story_it.R
import mvvm.coding.story_it.databinding.LeaderboardFragmentBinding
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
    }
    private fun setupListUpdate(){
        viewModel.scores.observe(this, Observer {
                scores-> if(scores.isNotEmpty())
            viewModel.setAdapter(scores)
        })

    }


}
