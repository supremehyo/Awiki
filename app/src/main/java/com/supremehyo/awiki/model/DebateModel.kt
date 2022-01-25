package com.supremehyo.awiki.model

import com.supremehyo.awiki.DTO.DebateDTO
import com.supremehyo.awiki.DTO.DebateListDTO
import com.supremehyo.awiki.repository.interest.InterestWikiContract
import okhttp3.ResponseBody
import retrofit2.Call

interface DebateModel {

    //토론관련 함수
    // 문서 id를 넘겨서 생성되게 만듬 생성되면서 토론이 생성되었습니다 라는 문구가 백엔드에서 insert 되도록, 그리고 토론을 처음 생성할때는 제목을 적어야함.
    suspend fun createDebate(wikiId : Long , debateTitle : String) : Call<Long>
    // 문서 id와 토론 id는 따로 돔, 여기서 넘겨주는 id는 문서 id 이고 이걸 저장하면서 토론 id는 따로 auto 로 증가
    suspend fun writeDebate(wikiId: Long, title : String , dto: DebateDTO) : Call<ResponseBody>
    //해당 문서에서 열린 토론 목록 제목을 가져오는 함수
    suspend fun getListDebateTitle(wikiId : Long) : Call<DebateListDTO>
    // 문서 id를 넘기면서 그 id에 달린 list를 백엔드에서 만들고 그 안에서 또 어떤 문제와 관련된 토론인지 찾는 debateTitle로  글을 가져오면 해당 문서에서 특정 토론제목에서 발생한 토론 리스트를 가져올 수 있음.
    suspend fun getListDebateByTitle(wikiId : Long ,debateTitle : String ) : List<DebateDTO>

}