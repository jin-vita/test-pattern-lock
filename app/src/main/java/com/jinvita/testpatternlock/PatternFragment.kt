package com.jinvita.testpatternlock

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jinvita.testpatternlock.databinding.FragmentPatternBinding

class PatternFragment() : BottomSheetDialogFragment() {
    val type: String by lazy { arguments?.getString("type") ?: "login" }
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
        titleView.text = (activity as MainActivity).title
        patternLockView.addPatternLockListener(object : PatternLockViewListener {
            override fun onStarted() {}
            override fun onCleared() {}
            override fun onProgress(progressPattern: List<PatternLockView.Dot>) {}
            override fun onComplete(pattern: List<PatternLockView.Dot>) {
                PatternLockUtils.patternToString(patternLockView, pattern)
                    .also { if (type == "login") login(it) else register(it) }
            }
        })
        closeButton.setOnClickListener { dismiss() }
    }

    private fun login(pin: String) = with(binding) {
        println("pin: $pin")
        if (pin != (activity as MainActivity).answer) {
            patternLockView.clearPattern()
            messageView.run {
                text = "패턴을 잘못 입력했습니다"
                startAnimation((activity as MainActivity).shakeAnim)
            }
            return
        }
        (activity as DataListener).onDataReceived("로그인 성공하였습니다")
        dismiss()
    }

    private fun register(pin: String) {
        (activity as MainActivity).answer = pin
        (activity as DataListener).onDataReceived("패턴이 등록되었습니다")
        dismiss()
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