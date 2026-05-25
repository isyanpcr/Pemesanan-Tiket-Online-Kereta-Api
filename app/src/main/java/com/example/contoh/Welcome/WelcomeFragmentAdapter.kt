package com.example.contoh.Welcome

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class WelcomeFragmentAdapter (
    activity: WelcomeMessageActivity,
    private val fragments: List<Fragment>
)  : FragmentStateAdapter(activity)  {
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}