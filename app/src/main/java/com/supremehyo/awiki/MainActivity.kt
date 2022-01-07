package com.supremehyo.awiki

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.navigation.NavigationView
import com.supremehyo.awiki.View.fragment.EditFragment
import com.supremehyo.awiki.View.fragment.HomeFragment
import com.supremehyo.awiki.View.fragment.InterestFragment
import com.supremehyo.awiki.View.fragment.MywikiFragment
import kotlinx.android.synthetic.main.activity_main.*

//https://kangmin1012.tistory.com/33  코디네이트
//이 링크는 상세 글보기에서 나와야함.

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var homeFragment = HomeFragment()
        var editFragment = EditFragment()
        var mywikiFragment = MywikiFragment()
        var interestFragment = InterestFragment()

        navigationView.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId){
                    R.id.homeFragment ->{
                        item.isCheckable = true
                        replaceFragment(homeFragment)
                        return true
                    }
                    R.id.writeFragment ->{
                        item.isCheckable = true
                        replaceFragment(editFragment)
                        return true
                    }

                    R.id.mywikiFragment ->{
                        item.isCheckable = true
                        replaceFragment(mywikiFragment)
                        return true
                    }
                    R.id.interestFragment ->{
                        item.isCheckable = true
                        replaceFragment(interestFragment)
                        return true
                    }
                }
                return false;
            }
        })
    }


    fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        drawer_layout.closeDrawers()
        fragmentTransaction.replace(R.id.main_nav_host,fragment)
        fragmentTransaction.commit()
    }
}