package mvvm.coding.story_it.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import mvvm.coding.story_it.R
import mvvm.coding.story_it.databinding.ActivityMainBinding
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val viewModeFactory: ViewModelProvider.Factory by instance()

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, viewModeFactory).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel

        binding.executePendingBindings()

    }
}
