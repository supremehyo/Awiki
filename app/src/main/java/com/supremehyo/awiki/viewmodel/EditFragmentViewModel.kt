package com.supremehyo.awiki.viewmodel

import com.supremehyo.awiki.base.BaseViewModel
import com.supremehyo.awiki.model.WikiModel
import com.supremehyo.awiki.repository.WikiContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditFragmentViewModel @Inject constructor(private val model : WikiModel) : BaseViewModel() {

    //wiki 저장하기
    fun insertWiki(dto : WikiContract){
        model.insertWiki(dto)
    }

}