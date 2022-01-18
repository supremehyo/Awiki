package com.supremehyo.awiki.repository.wiki

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_wiki")//테이블 이름
data class WikiContract (
    //컬럼생성
    @PrimaryKey(autoGenerate = true) val id: Long?,
    var date: String, //작성일자
    var title: String, //위키 제목
    var category: String, //위키 카테고리
    var content: String, // html 로 구성된 위키 내용
    var images : String, //이미지 관련정보 저장한 다음에 내부 저장소에 이미지 따로 저장 하는 프로세스 필요
    var rawContent : String // html 이 아닌 일반 string
)
