package com.supremehyo.awiki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.supremehyo.awiki.base.BaseViewModel
import com.supremehyo.awiki.model.WikiModelImple
import com.supremehyo.awiki.repository.wiki.WikiContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val model : WikiModelImple) : BaseViewModel() {

    private val _LiveData = MutableLiveData<Call<ResponseBody>>()
    val LiveData: LiveData<Call<ResponseBody>>
        get() = _LiveData


    //wiki 저장하기
    fun gettest( ){
        viewModelScope.launch { // 코루틴 적용
            var temp = ""
            temp = model.gettest()
           // _LiveData.postValue(temp)
        }
    }

    fun posttest(wikiContent : WikiContract){
        viewModelScope.launch { // 코루틴 적용
           var temp =  model.posttest(wikiContent)
           // _LiveData.postValue(temp)
        }
    }

}