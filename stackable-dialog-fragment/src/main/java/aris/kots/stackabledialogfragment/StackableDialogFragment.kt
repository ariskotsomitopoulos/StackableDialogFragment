package aris.kots.stackabledialogfragment

import android.content.DialogInterface
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


abstract class StackableDialogFragment : DialogFragment() {

    private var currentPosition = 0
    private var currentTag: String? = null

    companion object {
        private val TAG = this::class.java.simpleName
        private const val ARG_TAG_NAME = "arg_tag_name"
        private const val MAX_DIALOGS_VISIBLE = 4
        private var uniqueIndex = AtomicInteger(0)
        private val fragmentTagStack = Stack<String>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, if(isFastAnimation()) R.style.ShortAnimThemeStackableDialog else R.style.ThemeStackableDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgumentsData()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.attributes?.windowAnimations = if(isFastAnimation()) R.style.ShortAnimThemeStackableDialog else R.style.ThemeStackableDialog
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        for (tag in fragmentTagStack) {
            fragmentManager?.findFragmentByTag(tag)?.let {
                (it as StackableDialogFragment).invalidateUI()
            }
        }
    }

    /**
     * Dismiss the current Fragment and invalidate the UI on the other fragments
     * in the stack
     */
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        fragmentTagStack.remove(currentTag)
        if (fragmentTagStack.size == 0) return
        for (tag in fragmentTagStack) {
            fragmentManager?.findFragmentByTag(tag)?.let {
                (it as StackableDialogFragment).invalidateUI()
            }
        }
    }

    /**
     * Show/Add the fragment to the stack with the appropriate tag handling.
     */
    override fun show(manager: FragmentManager, tag: String?) {
        // Argument usage available while fragment is not added yet to the fragment manager
        arguments = if(arguments == null) {
            Bundle().apply {
                putString(ARG_TAG_NAME, "$TAG${uniqueIndex.incrementAndGet()}")
            }
        }else{
            Bundle(arguments).apply {
                putString(ARG_TAG_NAME, "$TAG${uniqueIndex.incrementAndGet()}")
            }
        }
        super.show(manager, arguments?.getString(ARG_TAG_NAME))
    }

    /*
        Exposed Methods
     */

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    /**
     * Set the dialog window background
     */
    @DrawableRes
    open fun getDialogBackground(): Int = R.drawable.bg_transparent

    /**
     * Determines whether or not the dialog is pinned on the bottom.
     * If is False then the dialog bottom inset is the same with the top inset.
     */
    open fun isPinnedToBottom(): Boolean = true

    /**
     * Modifies the animation speed
     */
    open fun isFastAnimation(): Boolean = true

    /**
     * Set the top/bottom dialog inset from a dimens value
     */
    @DimenRes
    open fun getVerticalMargin(): Int = R.dimen.dialog_top_inset

    /**
     * Set the top/bottom dialog inset from a dimens value
     */
    @DimenRes
    open fun getHorizontalMargin(): Int = R.dimen.dialog_start_inset

    /**
     * Dismiss all fragments in the stack
     */
    protected fun dismissAll(){
        val stackClone = Stack<String>().apply {
            addAll(fragmentTagStack)
        }

        for (tag in stackClone) {
            fragmentManager?.findFragmentByTag(tag)?.let {
                (it as DialogFragment).dismiss()
            }
        }
    }

    /**
     * Returns the current position of the fragment
     */
    protected fun getPosition() = currentPosition

    /**
     * Redraw the position of every fragment in the stack depending on its position
     */
    private fun invalidateUI() {
        activity?.let {
            val insetDrawable = InsetDrawable(
                ContextCompat.getDrawable(it, getDialogBackground()),
                resources.getDimension(getHorizontalMargin()).toInt() + calculateInset(R.dimen.dialog_stack_width_inset),
                resources.getDimension(getVerticalMargin()).toInt() - calculateInset(R.dimen.dialog_stack_height_inset),
                resources.getDimension(getHorizontalMargin()).toInt() + calculateInset(R.dimen.dialog_stack_width_inset),
                if(isPinnedToBottom()){
                    resources.getDimension(R.dimen.dialog_bottom_inset).toInt()
                }else{
                    resources.getDimension(getVerticalMargin()).toInt() + calculateInset(R.dimen.dialog_stack_height_inset)
                }
            )
            dialog?.window?.setBackgroundDrawable(insetDrawable)
        }
    }

    /**
     * Calculate the inset for each dialog depending on their presentation order
     */
    private fun calculateInset(@DimenRes dimenRes: Int): Int {
        return when {
            fragmentTagStack.size - currentPosition <= 0 -> 0
            fragmentTagStack.size - currentPosition <= MAX_DIALOGS_VISIBLE -> (fragmentTagStack.size - currentPosition) * resources.getDimension(dimenRes).toInt()
            else -> MAX_DIALOGS_VISIBLE * resources.getDimension(dimenRes).toInt()
        }
    }

    private fun getArgumentsData() {
        arguments?.getString(ARG_TAG_NAME)?.let {
            fragmentTagStack.push(it)
            currentTag = it
            currentPosition = fragmentTagStack.size
        }
    }

}