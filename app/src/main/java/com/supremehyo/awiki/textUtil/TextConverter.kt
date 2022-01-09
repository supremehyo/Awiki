package com.supremehyo.awiki.textUtil

import android.text.Editable
import android.text.Spannable

class TextConverter {

    //fromHtml 
    // https://wiserloner.tistory.com/1350 참고
    fun smallText(content : String) : Spannable{
        var temp : Spannable = content as Spannable
        //여기서 작게 만드는거 텍스트 읽어와서 변환하도록 기능 만들어야함. 중간에 바뀌어도
        if(content.startsWith("small//") && content.endsWith("//small")){
            content.indexOf("small//")
            content.indexOf("//small")
        }
        return content
    }

    fun bigText(content : String){

    }

}