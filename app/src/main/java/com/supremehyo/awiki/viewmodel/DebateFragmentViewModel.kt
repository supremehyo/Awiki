package com.supremehyo.awiki.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.supremehyo.awiki.DTO.DebateDTO
import com.supremehyo.awiki.DTO.DebateListDTO
import com.supremehyo.awiki.base.BaseViewModel
import com.supremehyo.awiki.model.DebateModelIple
import com.supremehyo.awiki.model.WikiModelImple
import com.supremehyo.awiki.repository.interest.InterestWikiContract
import com.supremehyo.awiki.repository.wiki.WikiContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebateFragmentViewModel @Inject constructor(private val model : DebateModelIple) : BaseViewModel() {

    private  val _debateiDTOLiveData = MutableLiveData<DebateDTO>()
    val debateiDTOLiveData: LiveData<DebateDTO>
        get() = _debateiDTOLiveData

    private  val _debateTitleListLiveData = MutableLiveData<DebateListDTO>()
    val debateTitleListLiveData: LiveData<DebateListDTO>
        get() = _debateTitleListLiveData

    private  val _debateListLiveData = MutableLiveData<ArrayList<DebateDTO>>()
    val debateListLiveData: LiveData<ArrayList<DebateDTO>>
        get() = _debateListLiveData


    fun getDebate(){

    }

    fun getDebateTitleList(id : Long){
        viewModelScope.launch {
            model.getListDebateTitle(id)
        }
    }

    //제목으로 리스트
    fun getDebateListByTitle(wikiId : Long , title : String){
        viewModelScope.launch {
            _debateListLiveData.postValue(listToArrayList(model.getListDebateByTitle(wikiId ,title)))
        }
    }

    fun createDebate(wikiId : Long , title : String){
        viewModelScope.launch {
            model.createDebate(wikiId,title)
        }
    }

    fun postDebate(id : Long , title : String , dto : DebateDTO){
        viewModelScope.launch {
            model.writeDebate(id,title,dto)
        }
    }


    //list 를 arraylist 로 바꿔주는 함수
    fun listToArrayList(list : List<DebateDTO>) : ArrayList<DebateDTO> {
        var tempArrayList : ArrayList<DebateDTO> = ArrayList()
        list.forEach {
            tempArrayList.add(it)
        }
        return tempArrayList
    }

}