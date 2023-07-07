package com.jinvita.testpatternlock

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.jinvita.testpatternlock.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), DataListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomFragment: PatternFragment
    val shakeAnim: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.shake) }
    lateinit var answer: String
    lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnLogin.setOnClickListener {
                if (!::answer.isInitialized) {
                    textTitle.text = "먼저 패턴을 등록해주세요"
                    textTitle.startAnimation(shakeAnim)
                    return@setOnClickListener
                }
                showPatternBottomSheet("login")
            }
            btnRegister.setOnClickListener {
                showPatternBottomSheet("register")
            }
        }
    }

    private fun showPatternBottomSheet(type: String) {
        title = if (type == "login") "간편로그인 패턴입력" else "간편로그인 패턴등록"
        binding.textTitle.text = title
        if (::bottomFragment.isInitialized) bottomFragment.dismiss()
        bottomFragment = PatternFragment()
        bottomFragment.arguments = Bundle().apply { putString("type", type) }
        bottomFragment.show(supportFragmentManager, bottomFragment.tag)
    }

    override fun onDataReceived(data: String) {
        binding.textTitle.text = data
    }
}