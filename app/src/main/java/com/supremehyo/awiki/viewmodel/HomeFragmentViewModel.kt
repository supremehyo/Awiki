package com.supremehyo.awiki.viewmodel

import com.supremehyo.awiki.base.BaseViewModel
import com.supremehyo.awiki.model.WikiModelImple
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val model : WikiModelImple) : BaseViewModel() {


}