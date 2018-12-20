package mvvm.coding.story_it.ui.preferences

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.preferences_fragment.*


import org.koin.android.viewmodel.ext.android.viewModel

import mvvm.coding.story_it.R
import mvvm.coding.story_it.databinding.PreferencesFragmentBinding

class PreferencesFragment : Fragment() {

    companion object {
        fun newInstance() = PreferencesFragment()
    }

    private val viewModel: PreferencesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : PreferencesFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.preferences_fragment, container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListUpdate()
        setupErrorListeners()
        back_btn.setOnClickListener { findNavController() }
    }
    private fun setupListUpdate(){
        viewModel.players.observe(this, Observer {
            players-> if(players.isNotEmpty())
            viewModel.setAdapter(players)

        })
    }
    private fun setupErrorListeners() {
        viewModel.uiPlayerListError.observe(this, Observer {
            playerName.error = it
        })
        viewModel.isPossibleToStartGame.observe(this, Observer {
            start_game_btn.isEnabled = it
        })
    }
}
