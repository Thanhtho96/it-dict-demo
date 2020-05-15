package com.tt.it_dictionary.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tt.it_dictionary.ui.TabFragmentImage
import com.tt.it_dictionary.ui.TabFragmentMeaning

class TabAdapter(fm: FragmentActivity, private val en: String, private val vn: String) :
    FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TabFragmentMeaning().also {
                TabFragmentMeaning.newInstance(vn)
            }
            1 -> TabFragmentImage().also {
                TabFragmentImage.newInstance(en)
            }
            else -> TabFragmentMeaning().also {
                TabFragmentMeaning.newInstance(vn)
            }
        }
    }
}
