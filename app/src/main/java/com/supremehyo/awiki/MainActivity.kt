package com.supremehyo.awiki

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.supremehyo.awiki.view.fragment.EditFragment
import com.supremehyo.awiki.view.fragment.HomeFragment
import com.supremehyo.awiki.view.fragment.InterestFragment
import com.supremehyo.awiki.view.fragment.MywikiFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

//https://kangmin1012.tistory.com/33  코디네이트
//이 링크는 상세 글보기에서 나와야함.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var homeFragment = HomeFragment()
        var editFragment = EditFragment()
        var mywikiFragment = MywikiFragment()
        var interestFragment = InterestFragment()

        //초기에 home 으로 화면
        replaceFragment(homeFragment)

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
        //로고 클릭시 홈으로 이동
        awiki_logo.setOnClickListener {
            replaceFragment(homeFragment)
        }
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        drawer_layout.closeDrawers()
        fragmentTransaction.replace(R.id.main_nav_host,fragment)
        fragmentTransaction.commit()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView: View? = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev!!.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}