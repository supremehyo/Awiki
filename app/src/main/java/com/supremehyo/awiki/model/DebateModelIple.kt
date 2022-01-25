package com.supremehyo.awiki.model

import android.content.Context
import com.supremehyo.awiki.DTO.DebateDTO
import com.supremehyo.awiki.DTO.DebateListDTO
import com.supremehyo.awiki.retrofit.RetroServiceInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject


@HiltViewModel
class DebateModelIple @Inject constructor (@ApplicationContext private val context : Context, private val retroServiceInstance: RetroServiceInstance) : DebateModel{

    override suspend fun createDebate(wikiId: Long, debateTitle: String): Call<Long> {
        return retroServiceInstance.createDebate(wikiId, debateTitle)
    }

    override suspend fun writeDebate(wikiId: Long, title : String , dto: DebateDTO) : Call<ResponseBody> {
       return retroServiceInstance.postDebate(wikiId ,title ,dto)
    }

    override suspend fun getListDebateTitle(wikiId: Long): Call<DebateListDTO> {
        return retroServiceInstance.getDebateTitleList(wikiId)
    }

    override suspend fun getListDebateByTitle(wikiId: Long, debateTitle: String): List<DebateDTO> {
        return retroServiceInstance.getDebateListByTitle(debateTitle)
    }


}