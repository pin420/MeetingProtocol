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
        setImmersiveMode()
        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(
            AppIntroFragment.createInstance(
            title = "Добро пожаловать в приложение\nMeeting protocol",
            description = "Здесь можно делать такие вещи, как...",
            backgroundColorRes = androidx.appcompat.R.color.material_deep_teal_500,
            imageDrawable = R.drawable.icon_alpha_square,
        ))
        addSlide(AppIntroFragment.createInstance(
            title = "Записывать встречи",
            description = "Чтобы помнить когда, с кем и о чем вы разговаривали...",
            imageDrawable = R.drawable.baseline_draw_24,
            backgroundColorRes = R.color.teal_700
        ))
        addSlide(AppIntroFragment.createInstance(
            title = "Найти встречу",
            description = "-О чем я разговаривал вчера с другом?\nНаше приложение ответит",
            backgroundColorRes = com.google.android.material.R.color.accent_material_light
        ))
        addSlide(AppIntroFragment.createInstance(
            title = "Поделиться!",
            description = "Показывать другим свои протоколы",
            backgroundColorRes = com.google.android.material.R.color.accent_material_light
        ))
        addSlide(AppIntroFragment.createInstance(
            title = "Давайте попробуем!",
            description = "Сейчас объясним что, куда",
            backgroundColorRes = com.google.android.material.R.color.m3_sys_color_light_primary
        ))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)

        SharedPreferences.setPrefLearn(this, ACTIVITY)
        // Decide what to do when the user clicks on "Skip"
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)

        SharedPreferences.setPrefLearn(this, ACTIVITY)
        // Decide what to do when the user clicks on "Done"
        finish()
    }
}