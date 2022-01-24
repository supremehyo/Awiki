package com.supremehyo.awiki.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.supremehyo.awiki.R
import com.supremehyo.awiki.adapter.HomeRecyclerViewAdapter
import com.supremehyo.awiki.adapter.InterestRecyclerViewAdapter
import com.supremehyo.awiki.base.BaseFragment
import com.supremehyo.awiki.databinding.FragmentInterestBinding
import com.supremehyo.awiki.repository.interest.InterestWikiContract
import com.supremehyo.awiki.repository.wiki.WikiContract
import com.supremehyo.awiki.viewmodel.EditFragmentViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.search_et
import kotlinx.android.synthetic.main.fragment_interest.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class InterestFragment : BaseFragment<FragmentInterestBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_interest

    private var dtoList :ArrayList<InterestWikiContract> = ArrayList() //전체 검색값
    private var items: ArrayList<InterestWikiContract?> = ArrayList() //지금 리사이클러뷰에만 보여주고 있는 list값
    private lateinit var mMapLayoutManager: LinearLayoutManager
    private lateinit var mListAdapter: InterestRecyclerViewAdapter
    private lateinit var mRecyclerView: RecyclerView
    private val viewModel: EditFragmentViewModel by viewModels() // hilt 로 editfragment viewmodel 주입
    private var isLoading =false


    override fun initStartView() {
        initAdapter()
        initScrollListener()

    }

    override fun initDataBinding() {
        viewModel.getListInterestLiveData.observe(this, Observer {
            items.clear()
            dtoList.clear()
            initAdapter()
            isLoading =false

            if(interest_search_et.text.toString().isEmpty()) {
                viewModel.gonull2()
            }

            if(it!= null){
                dtoList= listToArrayList(it)
                if(dtoList.size > 5){
                    for (i in 0 until 5){// 5보다 크면 5개까지만 끊어서 넣어줘라
                        items.add(dtoList[i])
                    }
                }else{
                    dtoList.forEach {//그보다 적으면 그냥 다 집어넣으면 된다.
                        items.add(it)
                    }
                }
            }
        })
    }

    override fun initAfterBinding() {
        viewModel.getListInterestWikiDefault()


        interest_search_et.addTextChangedListener(object : TextWatcher {
            private var timer = Timer()
            private val DELAY: Long = 1000

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(title: Editable?) {
                timer.cancel()
                timer = Timer()
                timer.schedule(
                    object : TimerTask() {
                        override fun run() {
                            viewModel.getListInterestWiki(title.toString())
                        }
                    }, DELAY)
            }
        })

    }



    //빈 RecyclerView 생성
    private fun initAdapter(){
        mListAdapter = InterestRecyclerViewAdapter(activity!!,items , viewModel)
        mMapLayoutManager = LinearLayoutManager(requireContext())
        interestWikiRecyclerView.adapter = mListAdapter
    }

    //scroll이 끝에 닿으면 데이터에 null 추가 및 Adapter에 알림
    private fun initScrollListener(){
        interestWikiRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!isLoading){
                    if ((recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() == items.size - 1){
                        if(dtoList.size > 5){
                            moreItems()
                            isLoading =  true
                        }
                    }
                }
            }
        })
    }

    //list 를 arraylist 로 바꿔주는 함수
    fun listToArrayList(list : List<InterestWikiContract>) : ArrayList<InterestWikiContract> {
        var tempArrayList : ArrayList<InterestWikiContract> = ArrayList()
        list.forEach {
            tempArrayList.add(it)
        }
        return tempArrayList
    }

    //null 제거 후 새로운 데이터 추가 및 Adapter에 알림
    fun moreItems(){
        mRecyclerView = interestWikiRecyclerView

        val runnable = Runnable {
            items.add(null)
            mListAdapter.notifyItemInserted(items.size -1)
        }
        mRecyclerView.post(runnable)

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            val runnable2=  Runnable{
                Log.v("sdfsdf" ,items.size.toString())
                items.removeAt(items.size - 1)
                val scrollPosition = items.size
                mListAdapter.notifyItemRemoved(scrollPosition) // 프로그래스바 삭제
                var currentSize = scrollPosition
                var nextLimit = currentSize+5
                Log.e("hello", "$nextLimit")
                if (currentSize < dtoList.size-5){
                    while (currentSize-1<nextLimit){
                        items.add(dtoList[currentSize])
                        currentSize++
                    }
                }else{
                    while (currentSize!=dtoList.size){
                        items.add(dtoList[currentSize])
                        currentSize++
                    }
                }
                mListAdapter.updateItem(items)
                isLoading = false
            }
            runnable2.run()
        }
    }



}