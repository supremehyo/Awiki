package com.supremehyo.awiki.repository.interest

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity(tableName = "tb_intersetWiki")//테이블 이름
data class InterestWikiContract (
    //컬럼생성
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Long?,
    @SerializedName("date")
    var date: String, //작성일자
    @SerializedName("title")
    var title: String, //위키 제목
    @SerializedName("category")
    var category: String, //위키 카테고리
    @SerializedName("content")
    var content: String, // html 로 구성된 위키 내용
    @SerializedName("images")
    var images : String, //이미지 관련정보 저장한 다음에 내부 저장소에 이미지 따로 저장 하는 프로세스 필요
    @SerializedName("rawContent")
    var rawContent : String // html 이 아닌 일반 string
) : Serializable
