package com.supremehyo.awiki.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.supremehyo.awiki.R
import com.supremehyo.awiki.base.BaseFragment
import com.supremehyo.awiki.databinding.FragmentDebateBinding
import com.supremehyo.awiki.viewmodel.DebateFragmentViewModel
import com.supremehyo.awiki.viewmodel.EditFragmentViewModel


class DebateFragment : BaseFragment<FragmentDebateBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_debate
    private val viewModel: DebateFragmentViewModel by activityViewModels() // hilt 로 editfragment viewmodel 주입


    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }


}