package com.supremehyo.awiki.view.fragment

import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.supremehyo.awiki.R
import com.supremehyo.awiki.adapter.HomeRecyclerViewAdapter
import com.supremehyo.awiki.base.BaseFragment
import com.supremehyo.awiki.databinding.FragmentHomeBinding
import com.supremehyo.awiki.repository.wiki.WikiContract
import com.supremehyo.awiki.viewmodel.EditFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

//https://www.pinterest.co.kr/pin/14847873762484397/ 디자인 참고
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(){

    override val layoutResourceId: Int
        get() = R.layout.fragment_home
    private val viewModel: EditFragmentViewModel by activityViewModels() // hilt 로 editfragment viewmodel 주입


    private val mHandler: Handler = Handler()
    var title : String = ""

    private var dtoList :ArrayList<WikiContract> = ArrayList() //전체 검색값
    private var items: ArrayList<WikiContract?> = ArrayList() //지금 리사이클러뷰에만 보여주고 있는 list값
    private lateinit var mMapLayoutManager: LinearLayoutManager
    private lateinit var mListAdapter: HomeRecyclerViewAdapter
    private lateinit var mRecyclerView: RecyclerView
    private var isLoading =false


    override fun initStartView() {
        //viewModel.getWiki("title1" , requireContext() , "local")
        initAdapter()
        initScrollListener()
    }
    override fun initDataBinding() {

        //나중에 여기 랜덤으로 뽑아와서 5개 리스트로 나열하면 될듯
        viewModel.wikiDTOLiveData.observe(this , Observer {
            if (it != null) {
                Log.v("ssssss" , it.title)
               // wiki_title.text = it.title
               // wiki_subcontent.text = it.content
            }
        })

        viewModel.wikiDTOListLiveData.observe(this, Observer {
            //여기에 페이징해서 리사이클러뷰 무한 스크롤로 구현하고 처음에 리사이클러뷰 만들어질때
            //위에 awiki 이미지 애니메이션으로 사라지고 리사이클러뷰 사이즈가 확장
            //새로 검색됐기 때문에 기존 설정을 초기화 할 필요 있음
            items.clear()
            dtoList.clear()
            initAdapter()
            isLoading =false

            if(search_et.text.toString().isEmpty()) {
                viewModel.gonull()
                linearLayout.visibility = View.VISIBLE
              //  imageView.visibility = View.VISIBLE
            }

            if(it!= null){
                linearLayout.visibility = View.GONE
                dtoList= listToArrayList(it)
                if(dtoList.size > 5){
                    for (i in 0 until 5){// 5보다 크면 5개까지만 끊어서 넣어줘라
                     //   imageView.visibility = View.GONE
                        items.add(dtoList[i])
                    }
                }else{
                    dtoList.forEach {//그보다 적으면 그냥 다 집어넣으면 된다.
                      //  imageView.visibility = View.GONE
                        items.add(it)
                    }
                }
            }
        })
    }

    override fun initAfterBinding() {
        search_et.addTextChangedListener(object : TextWatcher{
            private var timer = Timer()
            private val DELAY: Long = 1000 // milliseconds

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
                            viewModel.getWikiListBySearch(title.toString())
                        }
                    }, DELAY)
            }
        })
    }

    //빈 RecyclerView 생성
    private fun initAdapter(){
        mListAdapter = HomeRecyclerViewAdapter(activity!!,items , viewModel)
        mMapLayoutManager = LinearLayoutManager(requireContext())
        home_recyclerView.adapter = mListAdapter
    }

    //scroll이 끝에 닿으면 데이터에 null 추가 및 Adapter에 알림
    private fun initScrollListener(){
        home_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
    fun listToArrayList(list : List<WikiContract>) : ArrayList<WikiContract> {
        var tempArrayList : ArrayList<WikiContract> = ArrayList()
        list.forEach {
            tempArrayList.add(it)
        }
        return tempArrayList
    }

    //null 제거 후 새로운 데이터 추가 및 Adapter에 알림
    fun moreItems(){
        mRecyclerView = home_recyclerView

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