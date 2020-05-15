package com.tt.it_dictionary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tt.it_dictionary.databinding.FragmentMeaningBinding

class TabFragmentMeaning : Fragment() {

    private lateinit var textView: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentMeaningBinding.inflate(inflater, container, false).also {
            textView = it.textView
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textView.text = vn
    }

    companion object {
        private lateinit var vn: String

        fun newInstance(vn: String) {
            this.vn = vn
        }
    }
}