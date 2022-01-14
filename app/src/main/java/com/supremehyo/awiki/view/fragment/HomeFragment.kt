package com.supremehyo.awiki.view.fragment

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.supremehyo.awiki.R
import com.supremehyo.awiki.base.BaseFragment
import com.supremehyo.awiki.databinding.FragmentHomeBinding
import com.supremehyo.awiki.viewmodel.EditFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(){

    override val layoutResourceId: Int
        get() = R.layout.fragment_home
    private val viewModel: EditFragmentViewModel by viewModels() // hilt 로 editfragment viewmodel 주입

    var title : String = ""

    override fun initStartView() {
        viewModel.getWiki("text" , requireContext())
    }
    override fun initDataBinding() {

        viewModel.wikiDTOLiveData.observe(this , Observer {
            if (it != null) {
                wiki_title.text = it.title
                wiki_subcontent.text = it.content
            }
        })
    }
    override fun initAfterBinding() {

    }
    override fun onResume() {
        super.onResume()
    }
}