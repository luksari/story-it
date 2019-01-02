package mvvm.coding.story_it.ui.round

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.round_fragment.*
import mvvm.coding.story_it.R
import mvvm.coding.story_it.databinding.RoundFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel

class RoundFragment : Fragment() {

    companion object {
        fun newInstance() = RoundFragment()
    }

    private val viewModel: RoundViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : RoundFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.round_fragment, container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.hasRoundEnded.observe(this, Observer {
            if(it) {
                findNavController().navigate(RoundFragmentDirections.actionRoundFragmentToSummaryFragment())
            }
        })
        viewModel.currentTurnStoryPart.observe(this, Observer {
            start_turn_btn.isEnabled = it.length <= viewModel.MAX_CHARS.value!!
            if (it.length > viewModel.MAX_CHARS.value!!)
                word_edittext.error =
                        "Length of written text must be shorter than ${viewModel.MAX_CHARS.value.toString()}."
        })
    }
}
