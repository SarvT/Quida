package com.example.quida

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.quida.databinding.ActivityMainBinding
import com.example.quida.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel:MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConvert.setOnClickListener {
            viewModel.convert(
                binding.etFrom.text.toString(),
                binding.spFromCurrency.selectedItem.toString(),
                binding.spToCurrency.selectedItem.toString()
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect{
                when(it){
                    is MainViewModel.CurrencyEvent.Success->{
                        binding.progressBar.isVisible=false
                        binding.tvResult.setTextColor(Color.BLACK)
                        binding.tvResult.text = it.resText
                    }
                    is MainViewModel.CurrencyEvent.Error->{
                        binding.progressBar.isVisible=false
                        binding.tvResult.setTextColor(Color.RED)
                        binding.tvResult.text = it.errText

                    }
                    is MainViewModel.CurrencyEvent.Loading->{
                        binding.progressBar.isVisible=true
                    }
                    else-> Unit
                }
            }
        }
    }
}