package com.example.sequencegame


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.widget.FrameLayout

class MainActivity : AppCompatActivity() {

    private lateinit var mMainNav : BottomNavigationView
    private lateinit var mMainFrame : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMainNav = findViewById(R.id.mainNav)
        mMainFrame = findViewById(R.id.mainFrame)



        mMainNav.setSelectedItemId(R.id.nav_game)

        mMainNav.setOnNavigationItemSelectedListener {item ->
            when(item.itemId) {
                R.id.nav_account -> {
                    setFragment(AccountFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_game -> {
                    setFragment(GameFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_ranking -> {
                    setFragment(RankingFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
        setFragment(GameFragment())
    }

    private fun setFragment(fragment : Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrame, fragment)
        fragmentTransaction.commit()
    }

}


