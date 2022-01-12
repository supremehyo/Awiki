package com.supremehyo.awiki.repository

data class WikiDTO(
    var date: String, //작성일자
    var title: String, //위키 제목
    var category: String, //위키 카테고리
    var content: String, // 위키 내용
    var images : String
)
