package com.mohamed.theguadiannews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mohamed.theguadiannews.databinding.ActivityMainBinding
import com.mohamed.theguadiannews.fragments.MusicFragment
import com.mohamed.theguadiannews.fragments.NewsFragment
import com.mohamed.theguadiannews.fragments.SportFragment

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    private val newsFragment = NewsFragment()
    private val sportFragment = SportFragment()
    private val musicFragment = MusicFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        replaceFragment(newsFragment)

        binding?.bottomNavigationView?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.news -> replaceFragment(newsFragment)
                R.id.sport -> replaceFragment(sportFragment)
                R.id.music -> replaceFragment(musicFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }
}