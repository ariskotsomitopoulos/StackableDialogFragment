# StackableDialogFragment

[![](https://jitpack.io/v/ariskotsomitopoulos/stackabledialogfragment.svg)](https://jitpack.io/#ariskotsomitopoulos/stackabledialogfragment)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/ariskotsomitopoulos/StackableDialogFragment/blob/master/LICENSE)


**StackableDialogFragment** is a simple lightweight kotlin library that let you stack multiple large *DialogFragments* together. It respects the creation order and represents the fragments/dialogs with a nice user friendly way.

#### Features
* Smooth enter/exit animations
* Bottom pinned functionality like a modal
* Resizable dialog, you can edit vertical and horizontal margin
* The UI let the user see that there are multiple dialogs opened in the background
* You can pass your own fragment bundles arguments without any problem

<p float="left">
<img src="https://github.com/ariskotsomitopoulos/StackableDialogFragment/blob/master/demo/stackable_dialog_fragment_pinned_bottom.gif" width="300">
<img src="https://github.com/ariskotsomitopoulos/StackableDialogFragment/blob/master/demo/stackable_dialog_fragment.gif" width="300">
</p>
How to use it
------
**Step 1** Add jitpack.io in your root build.gradle at the end of repositories:
```
  allprojects {
	 	repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
**Step 2** Add the dependency
```
  dependencies {
	    implementation 'com.github.ariskotsomitopoulos:stackabledialogfragment:v1.0.3'
	}
```

**Step 3**  Now you can simple create your own fragment that extends *StackableDialogFragment*. Override *getLayoutId()* with your fragment's layout and you are done.
```
class SampleDialogFragment : StackableDialogFragment(){

    companion object {
        fun newInstance() = SampleDialogFragment()
    }

    override fun getLayoutId(): Int = R.layout.sample_dialog_fragment

    override fun isPinnedToBottom(): Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleTextView.text = arguments?.getString(ARG_SAMPLE)
    }
}
```
**Step 4**  You can now use your custom dialog fragment like:
```
SampleDialogFragment.newInstance().show(supportFragmentManager,null)
```

Usefull Methods
------
You can override the following methods in order to define your own custom resources

*Set the dialog pinned to the bottom*
```
override fun isPinnedToBottom(): Boolean = false
```
*Set the vertical margins of the dialog*
```
override fun getVerticalMargin(): Int = R.dimen.your_custom_vertical_margin
```
*Set the horizontal margins of the dialog*
```
override fun getHorizontalMargin(): Int = R.dimen.your_custom_horizontal_margin
```
*Set the window dialog background*
```
override fun getDialogBackground(): Int = R.drawable.your_custom_drawable
```
*Modifies the animation speed*
```
override fun isFastAnimation(): Boolean = true
```
