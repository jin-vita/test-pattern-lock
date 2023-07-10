package com.jinvita.testpatternlock

import android.app.Dialog
import android.content.Context
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

class PatternFragment : BottomSheetDialogFragment() {
    private val type by lazy { arguments?.getString("type") ?: "login" }
    private val title by lazy { arguments?.getString("title") ?: "간편로그인 패턴입력" }
    private val answer by lazy { arguments?.getString("answer") ?: "" }

    private var listener: FragmentListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? FragmentListener ?: throw RuntimeException("$context must implement FragmentListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun getTheme(): Int = R.style.BottomSheetTheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)
        .apply {
            setOnShowListener {
                val layout = binding.subLayout
                BottomSheetBehavior.from(layout).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    peekHeight = layout.height
                }
                (layout.parent as CoordinatorLayout).parent.requestLayout()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        titleView.text = title
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
        if (pin != answer) {
            patternLockView.clearPattern()
            messageView.run {
                text = "패턴을 잘못 입력했습니다"
                startAnimation((activity as MainActivity).shakeAnim)
            }
            return
        }
        listener?.onDataReceived("로그인 성공하였습니다")
        dismiss()
    }

    private fun register(pin: String) {
        listener?.onDataReceived(pin, "answer")
        listener?.onDataReceived("패턴이 등록되었습니다")
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