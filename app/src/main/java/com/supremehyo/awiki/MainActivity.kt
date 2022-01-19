package com.supremehyo.awiki

import android.annotation.SuppressLint
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.plusAssign
import com.google.android.material.navigation.NavigationView
import com.supremehyo.awiki.navigation.KeepStateNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

//https://kangmin1012.tistory.com/33  코디네이트
//이 링크는 상세 글보기에서 나와야함.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationView.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId){
                    R.id.homeFragment ->{
                        item.isCheckable = true
                        findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_homeFragment)
                        drawer_layout.closeDrawers()
                        return true
                    }
                    R.id.writeFragment ->{
                        item.isCheckable = true
                        var bundle = bundleOf("readorwrite" to "write")
                        findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_writeFragment , bundle)
                        drawer_layout.closeDrawers()
                        return true
                    }

                    R.id.mywikiFragment ->{
                        item.isCheckable = true
                        drawer_layout.closeDrawers()
                        return true
                    }
                    R.id.interestFragment ->{
                        item.isCheckable = true
                        findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_interestFragment)
                        drawer_layout.closeDrawers()
                        return true
                    }
                }
                return false;
            }
        })
        //로고 클릭시 홈으로 이동
        awiki_logo.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_homeFragment)
        }


        val navController = findNavController(R.id.nav_host_fragment)

        // get fragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!

        // setup custom navigator
        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, R.id.nav_host_fragment)
        navController.navigatorProvider += navigator
        navController.setGraph(R.navigation.menu_nav_graph)
    }

    /*
    fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        drawer_layout.closeDrawers()
        fragmentTransaction.replace(R.id.main_nav_host,fragment)
        fragmentTransaction.commit()
    }*/

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