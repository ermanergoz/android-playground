package com.group7.jhealth

import android.app.Activity
import android.view.View
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.group7.jhealth.fragments.SleepMonitoringFragment
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class SleepMonitoringFragmentTest {

    var mActivity: Activity? = null

    @Rule
    @JvmField
    val mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        mActivity = mActivityTestRule.activity
    }

    @Test
    fun testLaunch() {
        val fragmentManager = mActivityTestRule.activity.supportFragmentManager

        fragmentManager
            .beginTransaction().replace(R.id.nav_host_fragment,
                SleepMonitoringFragment()
            )
            .commitAllowingStateLoss()

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        val view: View = mActivity!!.findViewById(R.id.nav_sleep_monitoring)
        assertNotNull(view)
    }

    @After
    fun tearDown() {
        mActivity = null
    }
}