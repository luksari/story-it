package mvvm.coding.story_it.ui.playerForm

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import mvvm.coding.story_it.R

class PlayerFormFragment : Fragment() {

    companion object {
        fun newInstance() = PlayerFormFragment()
    }

    private lateinit var viewModel: PlayerFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.player_form_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlayerFormViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
