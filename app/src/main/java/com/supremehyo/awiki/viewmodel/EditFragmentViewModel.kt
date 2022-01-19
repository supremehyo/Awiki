package com.supremehyo.awiki.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.supremehyo.awiki.DTO.HometoEditDTO
import com.supremehyo.awiki.base.BaseViewModel
import com.supremehyo.awiki.model.WikiModelImple
import com.supremehyo.awiki.repository.wiki.WikiContract
import com.supremehyo.awiki.utils.EventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditFragmentViewModel @Inject constructor(private val model : WikiModelImple) : BaseViewModel() {


    private  val _wikiDTOLiveData = MutableLiveData<WikiContract>()
    val wikiDTOLiveData: LiveData<WikiContract>
        get() = _wikiDTOLiveData

    private  val _wikiDTORandomLiveData = MutableLiveData<WikiContract>()
    val wikiDTORandomLiveData: LiveData<WikiContract>
        get() = _wikiDTORandomLiveData

    private  val _wikiDTOListLiveData = MutableLiveData<List<WikiContract>>()
    val wikiDTOListLiveData: LiveData<List<WikiContract>>
        get() = _wikiDTOListLiveData

    private  val _wikiDTOInsertLiveData = MutableLiveData<Long>()
    val wikiDTOInsertLiveData: LiveData<Long>
        get() = _wikiDTOInsertLiveData

    private  val _clickWikiItem =  MutableLiveData<HometoEditDTO>()
    val clickWikiItem: LiveData<HometoEditDTO>
        get() = _clickWikiItem

    //wiki 저장하기
    fun insertWiki(dto : WikiContract , localOrApi : String ){
        Log.v("sfdsdf333" , dto.content)
        var temp = 0L
        viewModelScope.launch { // 코루틴 적용
            Log.v("awgewgg2" , dto.content)
            temp = model.insertWiki(dto ,localOrApi)
            _wikiDTOInsertLiveData.postValue(temp)
        }
    }

    //wiki 불러오기
    fun getWiki(title : String , context: Context  , localOrApi : String){
        Log.v("읽기2" , title)
        viewModelScope.launch { // 코루틴 적용
            var temp = model.getOneWiki(title ,context ,localOrApi)
            if(temp != null){
                _wikiDTOLiveData.postValue(temp)
            }
        }
    }
    
    //랜덤으로 가져오기 
    fun randomGetOneWiki(id : Long){
        viewModelScope.launch {
            var randomWiki = model.randomGetOneWiki(id)
            if(randomWiki != null){
                _wikiDTORandomLiveData.postValue(randomWiki)
            }
        }
    }
    

    //검색으로 가져오기
    fun getWikiListBySearch(title : String){
        viewModelScope.launch {
            var searchWiki = model.getWikiListBySearch(title)
            if(searchWiki != null){
                _wikiDTOListLiveData.postValue(searchWiki)
            }
        }
    }

    fun gonull(){
        _wikiDTOListLiveData.postValue(null)
    }
    fun clickHomeWikiListItemnull(){
        _clickWikiItem.postValue(null)
    }

    fun clickHomeWikiListItem(dto : HometoEditDTO){
        viewModelScope.launch { // 코루틴 적용
            _clickWikiItem.postValue(dto)
        }
    }

}