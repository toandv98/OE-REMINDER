package com.edu.sun.oereminder.ui.checkin

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.Gravity.END
import android.view.Gravity.START
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.transition.Slide
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.utils.beginTransition
import com.edu.sun.oereminder.utils.changeResource
import com.edu.sun.oereminder.utils.toTime
import kotlinx.android.synthetic.main.dialog_checkin.*
import java.util.*

class CheckInDialogFragment : DialogFragment() {

    private var isCheckIn = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.run { isCheckIn = getBoolean(IS_CHECK_IN, true) }
        return inflater.inflate(R.layout.dialog_checkin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isCheckIn) {
            textCheckIn.text = getString(R.string.label_daily_check_out)
            btnSendReport.text = getString(R.string.text_daily_report)
            switchCheckIn.run {
                setThumbResource(R.drawable.ic_close)
                isChecked = true
            }
        }

        btnBack.setOnClickListener { dismiss() }
        btnSendReport.setOnClickListener {
            setFragmentResult(REQUEST_CHECK_IN, bundleOf(REQUEST_CHECK_IN to CODE_SEND_REPORT))
        }

        switchCheckIn.setOnCheckedChangeListener { _, isChecked ->
            rootLayout.beginTransition(
                Slide(if (isCheckIn) END else START),
                R.id.switchCheckIn,
                R.id.btnSendReport
            )
            if (isChecked && isCheckIn) checkIn()
            if (!isChecked && !isCheckIn) checkOut()
        }
    }

    private fun checkIn() {
        imageCheckIn.drawable.run { if (this is Animatable) start() }
        switchCheckIn.visibility = INVISIBLE
        btnSendReport.visibility = VISIBLE
        textCheckIn.text = getString(R.string.time_in_format, GregorianCalendar().toTime())
        setFragmentResult(REQUEST_CHECK_IN, bundleOf(REQUEST_CHECK_IN to CODE_CHECK_IN))
    }

    private fun checkOut() {
        switchCheckIn.visibility = INVISIBLE
        btnSendReport.visibility = VISIBLE
        imageCheckIn.changeResource(R.drawable.ic_check_out)
        textCheckIn.text = getString(R.string.time_out_format, GregorianCalendar().toTime())
        setFragmentResult(REQUEST_CHECK_IN, bundleOf(REQUEST_CHECK_IN to CODE_CHECK_OUT))
    }

    override fun getTheme() = R.style.CheckInDialogStyle

    companion object {
        const val CODE_SEND_REPORT = 2
        const val CODE_CHECK_IN = 0
        const val CODE_CHECK_OUT = 1
        const val REQUEST_CHECK_IN = "request_check_in"
        const val IS_CHECK_IN = "is_check_in"

        fun newInstance(isCheckIn: Boolean) = CheckInDialogFragment().apply {
            arguments = bundleOf(IS_CHECK_IN to isCheckIn)
        }
    }
}
