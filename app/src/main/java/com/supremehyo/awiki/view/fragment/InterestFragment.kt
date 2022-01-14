package com.supremehyo.awiki.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.supremehyo.awiki.R
import com.supremehyo.awiki.base.BaseFragment
import com.supremehyo.awiki.databinding.FragmentInterestBinding
import com.supremehyo.awiki.viewmodel.EditFragmentViewModel


class InterestFragment : BaseFragment<FragmentInterestBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_interest
    private val viewModel: EditFragmentViewModel by viewModels() // hilt 로 editfragment viewmodel 주입


    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }


}