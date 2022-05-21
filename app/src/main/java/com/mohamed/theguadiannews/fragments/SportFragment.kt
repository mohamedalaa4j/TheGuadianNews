package com.mohamed.theguadiannews.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.mohamed.theguadiannews.R
import com.mohamed.theguadiannews.databinding.FragmentSportBinding


class SportFragment : Fragment(R.layout.fragment_sport) {
    private var binding : FragmentSportBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSportBinding.bind(view)
    }
}