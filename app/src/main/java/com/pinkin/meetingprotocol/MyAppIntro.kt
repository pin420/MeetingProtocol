package com.pinkin.meetingprotocol

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment


private const val ACTIVITY = "ACTIVITY"


class MyAppIntro : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!
        isColorTransitionsEnabled = true
        isVibrate = true
        vibrateDuration = 50L
        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(
            AppIntroFragment.createInstance(
            title = getString(R.string.intro_1_title),
            description = getString(R.string.intro_1_description),
            backgroundColorRes = R.color.slide1,
            imageDrawable = R.drawable.icon_alpha_square,
        ))
        addSlide(AppIntroFragment.createInstance(
            title = getString(R.string.intro_2_title),
            description = getString(R.string.intro_2_description),
            imageDrawable = R.drawable.intro_baseline_draw_24,
            backgroundColorRes = R.color.slide2,
        ))
        addSlide(AppIntroFragment.createInstance(
            title = getString(R.string.intro_3_title),
            description = getString(R.string.intro_3_description),
            backgroundColorRes = R.color.slide3,
            imageDrawable = R.drawable.intro_baseline_search_24
        ))
        addSlide(AppIntroFragment.createInstance(
            title = getString(R.string.intro_4_title),
            description = getString(R.string.intro_4_description),
            backgroundColorRes = R.color.slide4,
            imageDrawable = R.drawable.intro_baseline_people_24
        ))
        addSlide(AppIntroFragment.createInstance(
            title = getString(R.string.intro_5_title),
            description = getString(R.string.intro_5_description),
            backgroundColorRes = R.color.slide5,
            imageDrawable = R.drawable.intro_baseline_school_24
        ))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)

        SharedPreferences.setPrefLearnTrue(this, ACTIVITY)
        // Decide what to do when the user clicks on "Skip"
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)

        SharedPreferences.setPrefLearnTrue(this, ACTIVITY)
        // Decide what to do when the user clicks on "Done"
        finish()
    }
}