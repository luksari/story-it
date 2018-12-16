package mvvm.coding.story_it.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

import androidx.lifecycle.Observer
import mvvm.coding.story_it.R
import mvvm.coding.story_it.databinding.ActivityMainBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


       val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.addPlayerAfter5SecondsToTest()

        viewModel.loadPlayers()
        // Observes viewModel players livedata
        viewModel.players.observe(this, Observer {
            Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
        })



    }
}
