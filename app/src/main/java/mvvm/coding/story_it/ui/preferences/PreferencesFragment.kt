package mvvm.coding.story_it.ui.preferences

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import org.koin.android.viewmodel.ext.android.viewModel

import mvvm.coding.story_it.R
import mvvm.coding.story_it.databinding.PreferencesFragmentBinding
import mvvm.coding.story_it.databinding.StartFragmentBinding

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
        // TODO: Use the ViewModel
    }

}
