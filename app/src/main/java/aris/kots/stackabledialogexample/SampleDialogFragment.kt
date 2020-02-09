package aris.kots.stackabledialogexample

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import aris.kots.stackabledialogexample.extensions.withArgs
import aris.kots.stackabledialogfragment.StackableDialogFragment
import kotlinx.android.synthetic.main.sample_dialog_fragment.*

class SampleDialogFragment : StackableDialogFragment(){

    companion object {
        fun newInstance() = SampleDialogFragment()

        private const val ARG_SAMPLE= "arg_sample"

        fun newInstance(displayText: String) = SampleDialogFragment().withArgs {
            putString(ARG_SAMPLE, displayText)
        }
    }

    override fun getLayoutId(): Int = R.layout.sample_dialog_fragment

//    override fun isPinnedToBottom(): Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListeners()
        colorBackground()
        titleTextView.text = arguments?.getString(ARG_SAMPLE)
    }

    /**
     * Color the even dialogs for fun
     */

    private fun colorBackground() {
        backgroundConstraintLayout.backgroundTintList =

            if(getPosition() % 2 == 0)
                ColorStateList.valueOf(Color.parseColor("#e0e0e0"))
            else
                ColorStateList.valueOf(Color.parseColor("#ffffff"))
    }

    private fun clickListeners(){

        exitImageView.setOnClickListener {
            dismiss()
        }

        newDialogButton.setOnClickListener {
            fragmentManager?.let {
                newInstance(":D").show(it,null)
            }
        }

        closeAllButton.setOnClickListener {
            dismissAll()
        }
    }

}