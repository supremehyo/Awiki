package com.supremehyo.awiki

import android.annotation.SuppressLint
import android.graphics.Rect
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.plusAssign
import com.google.android.material.navigation.NavigationView
import com.supremehyo.awiki.navigation.KeepStateNavigator
import com.supremehyo.awiki.utils.PdfUtil
import com.supremehyo.awiki.viewmodel.EditFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

//https://kangmin1012.tistory.com/33  코디네이트
//이 링크는 상세 글보기에서 나와야함.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var backKeyPressedTime: Long = 0
    lateinit var toast : Toast
    private val viewModel: EditFragmentViewModel by viewModels()
    private var decorView: View? = null
    private var uiOption = 0
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        decorView = window.decorView
        uiOption = window.decorView.systemUiVisibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) uiOption =
            uiOption or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) uiOption =
            uiOption or View.SYSTEM_UI_FLAG_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) uiOption =
            uiOption or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        decorView!!.setSystemUiVisibility(uiOption)

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
                        findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_writeFragment)
                        viewModel.setState("write")
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
                return false
            }
        })

        //왼쪽 상단 awiki 눌렀을때 동작
        wiki_logo.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_homeFragment)
            drawer_layout.closeDrawers()
        }


        menu_open_iv.setOnClickListener {
            if(!drawer_layout.isOpen){
                drawer_layout.openDrawer(Gravity.LEFT)
            }else{
                drawer_layout.closeDrawers()
            }
        }

        val navController = findNavController(R.id.nav_host_fragment)

        // get fragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!

        // setup custom navigator
        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, R.id.nav_host_fragment)
        navController.navigatorProvider += navigator
        navController.setGraph(R.navigation.menu_nav_graph)
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



    override fun onBackPressed() {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show()
            return
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish()
            toast.cancel()
        }
    }
}