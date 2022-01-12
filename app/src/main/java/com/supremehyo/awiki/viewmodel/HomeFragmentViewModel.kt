package com.supremehyo.awiki.viewmodel

import com.supremehyo.awiki.base.BaseViewModel
import com.supremehyo.awiki.model.WikiModel
import com.supremehyo.awiki.model.WikiModelImple
import com.supremehyo.awiki.repository.WikiContract
import com.supremehyo.awiki.repository.WikiDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val model : WikiModelImple) : BaseViewModel() {

    //wiki 하나 가져오기
    fun getWiki(title : String){
       var wiki =  model.getOneWiki(title)
    }

}