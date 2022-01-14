package com.supremehyo.awiki.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.supremehyo.awiki.base.BaseViewModel
import com.supremehyo.awiki.model.WikiModelImple
import com.supremehyo.awiki.repository.WikiContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditFragmentViewModel @Inject constructor(private val model : WikiModelImple) : BaseViewModel() {


    private  val _wikiDTOLiveData = MutableLiveData<WikiContract>()
    val wikiDTOLiveData: LiveData<WikiContract>
        get() = _wikiDTOLiveData


    //wiki 저장하기
    fun insertWiki(dto : WikiContract){
        Log.v("sfdsdf333" , dto.content)
        viewModelScope.launch { // 코루틴 적용
            Log.v("awgewgg2" , dto.content)
            model.insertWiki(dto)
        }
    }

    //wiki 불러오기
    fun getWiki(title : String , context: Context){
        Log.v("읽기2" , title)
        viewModelScope.launch { // 코루틴 적용
            var temp = model.getOneWiki(title ,context)
            if(temp != null){
                _wikiDTOLiveData.postValue(temp)
            }
        }
    }

}