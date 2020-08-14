package com.edu.sun.oereminder.ui.message

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.edu.sun.oereminder.R

class ConfirmDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setIcon(R.drawable.ic_round_warning)
            .setTitle(R.string.label_confirm)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                setFragmentResult(KEY_CONFIRM_RESULT, bundleOf(KEY_RESULT_CODE to CODE_OK))
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                setFragmentResult(KEY_CONFIRM_RESULT, bundleOf(KEY_RESULT_CODE to CODE_CANCEL))
                dismiss()
            }
            .create()
    }

    companion object {
        const val KEY_CONFIRM_RESULT = "confirm_result"
        const val KEY_RESULT_CODE = "result_code"
        const val CODE_OK = 1
        const val CODE_CANCEL = 0
    }
}
