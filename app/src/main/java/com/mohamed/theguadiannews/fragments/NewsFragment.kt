package com.mohamed.theguadiannews.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.mohamed.theguadiannews.R
import com.mohamed.theguadiannews.databinding.FragmentNewsBinding


class NewsFragment : Fragment(R.layout.fragment_news) {
    private var binding : FragmentNewsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsBinding.bind(view)
    }

}