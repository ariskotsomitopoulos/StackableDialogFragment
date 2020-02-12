# StackableDialogFragment

[![](https://jitpack.io/v/ariskotsomitopoulos/stackabledialogfragment.svg)](https://jitpack.io/#ariskotsomitopoulos/stackabledialogfragment)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/ariskotsomitopoulos/StackableDialogFragment/blob/master/LICENSE)


**StackableDialogFragment** is a simple lightweight kotlin library that let you stack multiple large *DialogFragments* together. It respects the creation order and represents the fragments/dialogs with a nice user friendly way.

#### Features
* Smooth enter/exit animations
* Bottom pinned functionality like a modal
* The UI let the user see that there are multiple dialogs opened in the background
* You can pass your own fragment bundles arguments without any problem

<img src="https://github.com/ariskotsomitopoulos/StackableDialogFragment/blob/master/demo/stackable_dialog_fragment_record_01.gif" width="380">


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
	    implementation 'com.github.ariskotsomitopoulos:stackabledialogfragment:v1.0.2'
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


