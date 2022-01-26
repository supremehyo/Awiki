package com.supremehyo.awiki.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.supremehyo.awiki.DTO.DebateDTO
import com.supremehyo.awiki.R
import com.supremehyo.awiki.adapter.DebateTitleRecyclerViewAdapter
import com.supremehyo.awiki.adapter.HomeRecyclerViewAdapter
import com.supremehyo.awiki.base.BaseFragment
import com.supremehyo.awiki.databinding.FragmentDebateBinding
import com.supremehyo.awiki.repository.wiki.WikiContract
import com.supremehyo.awiki.viewmodel.DebateFragmentViewModel
import com.supremehyo.awiki.viewmodel.EditFragmentViewModel
import kotlinx.android.synthetic.main.fragment_debate.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.home_recyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class DebateFragment : BaseFragment<FragmentDebateBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_debate

    private val viewModel: DebateFragmentViewModel by activityViewModels()

    //리사이클러뷰 관리
    private var dtoList :ArrayList<DebateDTO> = ArrayList() //전체 검색값
    private var items: ArrayList<DebateDTO?> = ArrayList() //지금 리사이클러뷰에만 보여주고 있는 list값
    private lateinit var mMapLayoutManager: LinearLayoutManager
    private lateinit var mListAdapter: DebateTitleRecyclerViewAdapter
    private lateinit var mRecyclerView: RecyclerView
    private var isLoading =false
    //리사이클러뷰 관리
    
    override fun initStartView() {
        //wiki 의 문서 id가 필요
        initAdapter()
        initScrollListener()
        viewModel.getDebateTitleList(1L)
    }

    override fun initDataBinding() {
        viewModel.debateTitleListLiveData.observe(this , Observer {
            dtoList = it.dtoList
        })
    }

    override fun initAfterBinding() {
        //여기서 클릭 했을때 상세로 넘어가서 해당 wiki에 토론 title 을 가진 글들만 가져오기
        //wiki id랑 title 은 계속해서 관리하는게 화면이동하면서도 관리하기 유용할것 같으므로 viewmodel에서 livedata로 관리하도록 설계
    }

    //빈 RecyclerView 생성
    private fun initAdapter(){
        mListAdapter = DebateTitleRecyclerViewAdapter(activity!!,items , viewModel)
        mMapLayoutManager = LinearLayoutManager(requireContext())
        debate_title_recyclerView.adapter = mListAdapter
    }

    //scroll이 끝에 닿으면 데이터에 null 추가 및 Adapter에 알림
    private fun initScrollListener(){
        debate_title_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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


    fun moreItems(){
        mRecyclerView = debate_title_recyclerView
        val runnable = Runnable {
            items.add(null)
            mListAdapter.notifyItemInserted(items.size -1)
        }
        mRecyclerView.post(runnable)
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            val runnable2=  Runnable{
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