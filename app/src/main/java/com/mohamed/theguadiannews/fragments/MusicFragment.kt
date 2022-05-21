package com.mohamed.theguadiannews.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.mohamed.theguadiannews.R
import com.mohamed.theguadiannews.databinding.FragmentMusicBinding

class MusicFragment : Fragment(R.layout.fragment_music) {
  private var binding : FragmentMusicBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMusicBinding.bind(view)
    }
}