package com.jinvita.testpatternlock

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jinvita.testpatternlock.databinding.FragmentPatternBinding

class PatternFragment : BottomSheetDialogFragment() {
    private val answer = "0123"
    private lateinit var shakeAnim: Animation
    override fun getTheme(): Int = R.style.BottomSheetTheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            setOnShowListener {
                val coordinatorLayout = binding.subLayout.parent as CoordinatorLayout
                val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(binding.subLayout)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.peekHeight = binding.subLayout.height
                coordinatorLayout.parent.requestLayout()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        shakeAnim = AnimationUtils.loadAnimation(context, R.anim.shake)
        patternLockView.addPatternLockListener(object : PatternLockViewListener {
            override fun onStarted() {}
            override fun onCleared() {}
            override fun onProgress(progressPattern: List<PatternLockView.Dot>) {}
            override fun onComplete(pattern: List<PatternLockView.Dot>) {
                val pin = PatternLockUtils.patternToString(patternLockView, pattern)
                processComplete(pin)
            }
        })
        closeButton.setOnClickListener { dismiss() }
    }

    private fun processComplete(pin: String) = with(binding) {
        println("pin: $pin")
        if (pin == answer) dismiss() else {
            patternLockView.clearPattern()
            messageView.run {
                text = "패턴을 잘못 입력했습니다"
                startAnimation(shakeAnim)
            }
        }
    }

    private var _binding: FragmentPatternBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatternBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}