package aris.kots.stackabledialogexample.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment


/**
 * Created by Ais Kotsomitopoulos on 03 - 09 - 2019
 * aris.kotsomitopoulos@gmail.com
 */

internal inline fun <T: Fragment> T.withArgs(argsBuilder: Bundle.() -> Unit): T =
        this.apply {
            arguments = Bundle().apply(argsBuilder)
        }