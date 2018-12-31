package mvvm.coding.story_it.ui.leaderboard

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import mvvm.coding.story_it.R
import mvvm.coding.story_it.databinding.LeaderboardFragmentBinding
import mvvm.coding.story_it.databinding.StartFragmentBinding
import mvvm.coding.story_it.ui.start.StartViewModel
import mvvm.coding.story_it.ui.start.startFragment
import mvvm.coding.story_it.ui.start.startFragmentDirections
import org.koin.android.viewmodel.ext.android.viewModel

class LeaderboardFragment : Fragment() {

//    companion object {
//        fun newInstance() = LeaderboardFragment()
//    }
//
//    private lateinit var viewModel: LeaderboardViewModel
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.leaderboard_fragment, container, false)
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(LeaderboardViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

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

//    private fun setupListeners(){
//        new_game_btn.setOnClickListener { findNavController().navigate(startFragmentDirections.actionStartFragmentToPreferencesFragment()) }
//        leaderboard_btn.setOnClickListener { findNavController().navigate(startFragmentDirections.actionStartFragmentToLeaderboardFragment()) }
//    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        setupListeners()
        super.onActivityCreated(savedInstanceState)
        setupListUpdate()
    }
    private fun setupListUpdate(){
        println("FRAGMENT")
        viewModel.scores.observe(this, Observer {
                scores-> if(scores.isNotEmpty())
            viewModel.setAdapter(scores)
        })

    }


}
