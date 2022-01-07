package com.supremehyo.awiki.View.fragment


import androidx.fragment.app.viewModels
import com.supremehyo.awiki.R
import com.supremehyo.awiki.databinding.FragmentEditBinding
import com.supremehyo.awiki.viewmodel.HomeFragmentViewModel
import com.supremehyo.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

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

    }

}