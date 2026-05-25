package com.example.contoh.WikiONNX

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class WikiTabsAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 4
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> tab1()
            1 -> tab2()
            2 -> tab3()
            3 -> tab4()
            else -> throw IllegalStateException("Posisi tidak valid")
        }
    }
}