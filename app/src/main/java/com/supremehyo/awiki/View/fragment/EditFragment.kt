package com.supremehyo.awiki.View.fragment



import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.text.parseAsHtml
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.supremehyo.awiki.MainActionListener
import com.supremehyo.awiki.R
import com.supremehyo.awiki.databinding.FragmentEditBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_edit.*
import org.wordpress.aztec.AztecTextFormat

//https://github.com/wordpress-mobile/AztecEditor-Android  라이브러리참고
@AndroidEntryPoint
class EditFragment : Fragment() , MainActionListener, ActionMode.Callback {
    private lateinit var binding: FragmentEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditBinding.inflate(layoutInflater)
        val view = binding.root
        initView()
        return view
    }
    override fun onApplyBoldStyleSpan() {
        binding.etEditor.toggleFormatting(AztecTextFormat.FORMAT_BOLD)
        onShowTextPreview()
    }


    override fun onApplyItalicStyleSpan() {
        binding.etEditor.toggleFormatting(AztecTextFormat.FORMAT_ITALIC)
        onShowTextPreview()
    }

    override fun onApplyQuoteSpan() {
        binding.etEditor.toggleFormatting(AztecTextFormat.FORMAT_QUOTE)
        onShowTextPreview()
    }

    override fun onApplyOrderedListSpan() {
        binding.etEditor.toggleFormatting(AztecTextFormat.FORMAT_ORDERED_LIST)
        onShowTextPreview()
    }

    override fun onApplyUnorderedListSpan() {
        binding.etEditor.toggleFormatting(AztecTextFormat.FORMAT_UNORDERED_LIST)
        onShowTextPreview()
    }

    override fun onShowTextPreview() {
        binding.tvPreview.text = binding.etEditor.toHtml()
        tv_preview2.setText(binding.tvPreview.text.toString().parseAsHtml())
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.menu_actions, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        menu?.children?.filterNot { it.groupId == R.id.group_format }?.forEach {
            menu.removeItem(it.itemId)
            menu.add(it.groupId, it.itemId, it.order, it.title)
        }
        return false
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_format_bold -> {
                onApplyBoldStyleSpan()
                true
            }
            R.id.action_format_italic -> {
                onApplyItalicStyleSpan()
                true
            }
            else -> false
        }
    }

    override fun onDestroyActionMode(p0: ActionMode?) {

    }


    private fun initView() {
        binding.btnBold.setOnClickListener { onApplyBoldStyleSpan() }
        binding.btnItalic.setOnClickListener { onApplyItalicStyleSpan() }
        binding.btnQuote.setOnClickListener { onApplyQuoteSpan() }
        binding.btnOrderedList.setOnClickListener { onApplyOrderedListSpan() }
        binding.btnUnorderedList.setOnClickListener { onApplyUnorderedListSpan() }

        binding.etEditor.apply {
            setCalypsoMode(false)
            doAfterTextChanged { onShowTextPreview() }
            customSelectionActionModeCallback = this@EditFragment
        }
    }


}