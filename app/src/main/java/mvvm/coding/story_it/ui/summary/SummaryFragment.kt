package mvvm.coding.story_it.ui.summary

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.options_list_item.*
import mvvm.coding.story_it.R
import mvvm.coding.story_it.databinding.SummaryFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel

class SummaryFragment : Fragment() {

    companion object {
        fun newInstance() = SummaryFragment()
    }

    private val viewModel: SummaryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<SummaryFragmentBinding>(inflater,R.layout.summary_fragment,container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.summaryHasEnded.observe(this, Observer{
            if(it && !viewModel.gameHasEnded.value!!) findNavController().navigate(SummaryFragmentDirections.ActionSummaryFragmentToRoundFragment())
        })
        viewModel.gameHasEnded.observe(this, Observer {
            if(it) viewModel.showGameSummary()
        })
        setupOptionsUpdate()

    }
    private fun setupOptionsUpdate(){
        viewModel.listOfPlayersToVoteFor.observe(this, Observer {
                options-> if(options.isNotEmpty())
            viewModel.setAdapter(options)
        })
    }

}
