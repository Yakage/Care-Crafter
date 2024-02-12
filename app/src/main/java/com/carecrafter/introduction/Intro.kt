package com.carecrafter.introduction
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewpager.widget.ViewPager
import com.carecrafter.R
import com.carecrafter.registration.SignIn
import com.carecrafter.registration.Welcome

class Intro : AppCompatActivity() {
    lateinit var viewPager: ViewPager
    lateinit var sliderAdapter: SliderAdapter
    lateinit var sliderList: ArrayList<SliderData>

    lateinit var skipBtn: TextView
    lateinit var indicatorSlideOneTV: TextView
    lateinit var indicatorSlideTwoTV: TextView
    lateinit var indicatorSlideThreeTV: TextView
    lateinit var indicatorSlideFourTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()
        setContentView(R.layout.introduction_intro)

        viewPager = findViewById(R.id.idViewPager)
        skipBtn = findViewById(R.id.idBtnSkip)
        indicatorSlideOneTV = findViewById(R.id.idTVSlideOne)
        indicatorSlideTwoTV = findViewById(R.id.idTVSlideTwo)
        indicatorSlideThreeTV = findViewById(R.id.idTVSlideThree)
        indicatorSlideFourTV = findViewById(R.id.idTVSlideFour)

        skipBtn.setOnClickListener {
            val intent = Intent(this@Intro, Welcome::class.java)
            startActivity(intent)
        }

        sliderList = ArrayList()

        sliderList.add(
            SliderData(
                "Every step counts.",
                "Let CareCrafter track your\n" +
                        "journey to wellness.",
                R.drawable.run
            )
        )

        sliderList.add(
            SliderData(
                "Dream better, live better.",
                "Trust CareCrafter to track your sleep.",
                R.drawable.sleep
            )
        )

        sliderList.add(
            SliderData(
                "Quench your thirst for wellness.",
                "Trust CareCrafter to remind you to hydrate.",
                R.drawable.drink
            )
        )

        sliderList.add(
            SliderData(
                "Eating right made easy.",
                "Explore tailored diet suggestions with CareCrafter.",
                R.drawable.eat
            )
        )

        sliderAdapter = SliderAdapter(this, sliderList)

        viewPager.adapter = sliderAdapter

        viewPager.addOnPageChangeListener(viewListener)

    }

    var viewListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {

            if (position == 0) {
                indicatorSlideOneTV.setTextColor(resources.getColor(R.color.introButtonColor))
                indicatorSlideTwoTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideThreeTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideFourTV.setTextColor(resources.getColor(R.color.grey))
                skipBtn.setText("Skip")

            } else if (position == 1) {
                indicatorSlideTwoTV.setTextColor(resources.getColor(R.color.introButtonColor))
                indicatorSlideThreeTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideFourTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideOneTV.setTextColor(resources.getColor(R.color.grey))
                skipBtn.setText("Skip")
            } else if (position == 2){
                indicatorSlideThreeTV.setTextColor(resources.getColor(R.color.introButtonColor))
                indicatorSlideFourTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideOneTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideTwoTV.setTextColor(resources.getColor(R.color.grey))
                skipBtn.setText("Skip")
            } else {
                indicatorSlideFourTV.setTextColor(resources.getColor(R.color.introButtonColor))
                indicatorSlideOneTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideTwoTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideThreeTV.setTextColor(resources.getColor(R.color.grey))
                skipBtn.setText("Get Started")
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
}