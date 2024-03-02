package com.carecrafter.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.carecrafter.R
import com.carecrafter.body.features.sleep_tracker.LeaderboardSleepTrackerFragment
import com.carecrafter.body.features.step_tracker.LeaderBoardStepTrackerFragment
import com.carecrafter.body.features.water_intake.LeaderboardWaterIntakeFragment
import com.google.android.material.tabs.TabLayout

class LeaderboardsFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.body_leaderboards, container, false)
        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)

        // Create and set up the adapter for the ViewPager
        val adapter = LeaderboardPagerAdapter(childFragmentManager)
        adapter.addFragment(LeaderBoardStepTrackerFragment(), "Step Tracker")
        adapter.addFragment(LeaderboardSleepTrackerFragment(), "Sleep Tracker")
        adapter.addFragment(LeaderboardWaterIntakeFragment(), "Water Reminder")
        viewPager.adapter = adapter

        // Connect the ViewPager with the TabLayout
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    // Adapter for the ViewPager
    inner class LeaderboardPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val fragmentList: MutableList<Fragment> = ArrayList()
        private val fragmentTitleList: MutableList<String> = ArrayList()

        override fun getCount(): Int = fragmentList.size

        override fun getItem(position: Int): Fragment = fragmentList[position]

        override fun getPageTitle(position: Int): CharSequence? = fragmentTitleList[position]

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }
    }


}