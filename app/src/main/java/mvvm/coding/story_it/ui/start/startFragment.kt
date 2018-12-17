package mvvm.coding.story_it.ui.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.start_fragment.*

import mvvm.coding.story_it.R
import mvvm.coding.story_it.base.FragmentsEnum
import mvvm.coding.story_it.databinding.StartFragmentBinding

import org.koin.android.viewmodel.ext.android.viewModel

class startFragment : Fragment() {

    companion object {
        fun newInstance() = startFragment()
    }

    private val viewModel: StartViewModel by viewModel()
    lateinit var action : startFragmentDirections


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : StartFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.start_fragment, container, false)
        binding.viewModel = viewModel

        binding.setLifecycleOwner(this)
        return binding.root
    }
    private fun setupListeners(){
        new_game_btn.setOnClickListener { findNavController().navigate(startFragmentDirections.actionStartFragmentToPreferencesFragment()) }
        leaderboard_btn.setOnClickListener { findNavController().navigate(startFragmentDirections.actionStartFragmentToLeaderboardFragment()) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupListeners()
        super.onViewCreated(view, savedInstanceState)

    }


}
