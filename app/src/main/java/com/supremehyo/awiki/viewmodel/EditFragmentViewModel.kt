package com.supremehyo.awiki.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.supremehyo.awiki.base.BaseViewModel
import com.supremehyo.awiki.model.WikiModelImple
import com.supremehyo.awiki.repository.wiki.WikiContract
import dagger.hilt.android.lifecycle.HiltViewModel
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

    //wiki 저장하기
    fun insertWiki(dto : WikiContract , localOrApi : String ){
        Log.v("sfdsdf333" , dto.content)
        viewModelScope.launch { // 코루틴 적용
            Log.v("awgewgg2" , dto.content)
            model.insertWiki(dto ,localOrApi)
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
                Log.v("rerere2" , title )
                Log.v("rerere3" ,searchWiki.get(0).title)
                _wikiDTOListLiveData.postValue(searchWiki)
            }
        }
    }

    fun gonull(){
        _wikiDTOListLiveData.postValue(null)
    }
}