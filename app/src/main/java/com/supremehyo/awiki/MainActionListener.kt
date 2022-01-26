package com.supremehyo.awiki

/**
 * Created by Sandi on 03/05/2020.
 */
interface MainActionListener {

    fun onApplyBoldStyleSpan()

    fun onApplyItalicStyleSpan()

    fun onApplyQuoteSpan()

    fun onApplyOrderedListSpan()

    fun onApplyUnorderedListSpan()

    fun onShowTextPreview()

    fun onJumpText(title : String)
}