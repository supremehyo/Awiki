package com.supremehyo.awiki.View.fragment


import android.R.attr
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.viewModels
import com.supremehyo.awiki.R
import com.supremehyo.awiki.databinding.FragmentEditBinding
import com.supremehyo.awiki.viewmodel.HomeFragmentViewModel
import com.supremehyo.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_edit.*
import android.text.Spanned

import android.text.style.ForegroundColorSpan

import android.R.attr.editable
import android.graphics.Color
import android.text.style.AbsoluteSizeSpan
import android.text.style.RelativeSizeSpan


@AndroidEntryPoint
class EditFragment : BaseFragment<FragmentEditBinding, HomeFragmentViewModel>() {

    override val viewModel: HomeFragmentViewModel by viewModels() // sharedViewModel 을 쓰면 activity에 있는 viewmodel을 프라그먼트에서 공유해서 쓸 수 있게 된다.
    override val layoutResourceId: Int
        get() = R.layout.fragment_edit


    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

        big_text.setOnClickListener {
            content_et.text.setSpan(
                RelativeSizeSpan(1.5f),//1.5배 상대적으로 커지기
                content_et.selectionStart,
                content_et.selectionEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        small_text.setOnClickListener {
            content_et.text.setSpan(
                RelativeSizeSpan(0.5f),//0.5배 상대적으로 작아지기
                content_et.selectionStart,
                content_et.selectionEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        color_text.setOnClickListener {
            content_et.text.setSpan(
                ForegroundColorSpan(Color.RED),
                content_et.selectionStart,
                content_et.selectionEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        content_et.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                /*
                val string = p0.toString()
                val split = string.split("\\s").toTypedArray()
                for (i in split.indices) {
                    val s = split[i]
                    if (map.containsKey(s)) {
                        val index = string.indexOf(s)
                        val color: Int = map.get(s)
                        p0.setSpan(
                            ForegroundColorSpan(color),
                            index,
                            index + s.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }*/
            }

        })
    }

}