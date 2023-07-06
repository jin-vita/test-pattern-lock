package com.jinvita.testpatternlock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jinvita.testpatternlock.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomFragment: PatternFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnLogin.setOnClickListener { showPatternBottomSheet() }
            btnRegister.setOnClickListener { }
        }
    }

    private fun showPatternBottomSheet() {
        if (::bottomFragment.isInitialized) bottomFragment.dismiss()
        bottomFragment = PatternFragment()
        bottomFragment.show(supportFragmentManager, bottomFragment.tag)
    }
}