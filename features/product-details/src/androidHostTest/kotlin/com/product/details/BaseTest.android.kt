package com.product.details

import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34], manifest = Config.NONE)
actual abstract class BaseTest actual constructor() {
    @Before
    fun setupRobolectric() {
        val pm = Shadows.shadowOf(RuntimeEnvironment.getApplication().packageManager)
        val componentName =
            ComponentName("com.product.details.test", "androidx.activity.ComponentActivity")
        val activityInfo =
            ActivityInfo().apply {
                name = componentName.className
                packageName = componentName.packageName
                enabled = true
                exported = true
            }

        val filter =
            IntentFilter().apply {
                addAction(Intent.ACTION_MAIN)
                addCategory(Intent.CATEGORY_LAUNCHER)
            }

        pm.addOrUpdateActivity(activityInfo)
        pm.addIntentFilterForActivity(componentName, filter)
    }
}
