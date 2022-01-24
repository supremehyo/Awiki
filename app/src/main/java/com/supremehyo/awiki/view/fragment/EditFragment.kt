package com.supremehyo.awiki.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.text.InputType
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.utils.widget.MotionButton
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.supremehyo.awiki.DTO.HometoEditDTO
import com.supremehyo.awiki.MainActionListener
import com.supremehyo.awiki.MainActivity
import com.supremehyo.awiki.R
import com.supremehyo.awiki.databinding.FragmentEditBinding
import com.supremehyo.awiki.repository.interest.InterestWikiContract
import com.supremehyo.awiki.repository.wiki.WikiContract
import com.supremehyo.awiki.utils.EventBus
import com.supremehyo.awiki.utils.MediaToolbarCameraButton
import com.supremehyo.awiki.utils.MediaToolbarGalleryButton
import com.supremehyo.awiki.utils.PdfUtil
import com.supremehyo.awiki.viewmodel.EditFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.fragment_edit.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.wordpress.android.util.AppLog
import org.wordpress.android.util.ImageUtils
import org.wordpress.android.util.PermissionUtils
import org.wordpress.aztec.*
import org.wordpress.aztec.glideloader.GlideImageLoader
import org.wordpress.aztec.glideloader.GlideVideoThumbnailLoader
import org.wordpress.aztec.plugins.CssUnderlinePlugin
import org.wordpress.aztec.plugins.IMediaToolbarButton
import org.wordpress.aztec.plugins.shortcodes.AudioShortcodePlugin
import org.wordpress.aztec.plugins.shortcodes.CaptionShortcodePlugin
import org.wordpress.aztec.plugins.shortcodes.VideoShortcodePlugin
import org.wordpress.aztec.plugins.shortcodes.extensions.ATTRIBUTE_VIDEOPRESS_HIDDEN_ID
import org.wordpress.aztec.plugins.shortcodes.extensions.ATTRIBUTE_VIDEOPRESS_HIDDEN_SRC
import org.wordpress.aztec.plugins.shortcodes.extensions.updateVideoPressThumb
import org.wordpress.aztec.plugins.wpcomments.HiddenGutenbergPlugin
import org.wordpress.aztec.plugins.wpcomments.WordPressCommentsPlugin
import org.wordpress.aztec.plugins.wpcomments.toolbar.MoreToolbarButton
import org.wordpress.aztec.plugins.wpcomments.toolbar.PageToolbarButton
import org.wordpress.aztec.source.SourceViewEditText
import org.wordpress.aztec.toolbar.AztecToolbar
import org.wordpress.aztec.toolbar.IAztecToolbarClickListener
import org.wordpress.aztec.util.AztecLog
import org.xml.sax.Attributes
import java.io.File
import java.util.*
//https://github.com/wordpress-mobile/AztecEditor-Android  라이브러리참고
@AndroidEntryPoint
class EditFragment : Fragment() ,
    AztecText.OnImeBackListener,
    AztecText.OnImageTappedListener,
    AztecText.OnVideoTappedListener,
    AztecText.OnAudioTappedListener,
    AztecText.OnMediaDeletedListener,
    AztecText.OnVideoInfoRequestedListener,
    IAztecToolbarClickListener,
    IHistoryListener,
    MainActionListener, ActionMode.Callback,
    ActivityCompat.OnRequestPermissionsResultCallback,
    PopupMenu.OnMenuItemClickListener,
    View.OnTouchListener {


    lateinit var pdfUtil : PdfUtil

    private var mIsKeyboardOpen = false
    private var mHideActionBarOnSoftKeyboardUp = false
    private lateinit var binding: FragmentEditBinding
    private var mediaMenu: PopupMenu? = null
    protected lateinit var aztec: Aztec
    private lateinit var mediaFile: String
    private lateinit var mediaPath: String
    lateinit var visualEditor : AztecText
    lateinit var sourceEditor : SourceViewEditText
    lateinit var  toolbar: AztecToolbar
    private lateinit var invalidateOptionsHandler: Handler
    private lateinit var invalidateOptionsRunnable: Runnable
    private val MEDIA_CAMERA_PHOTO_PERMISSION_REQUEST_CODE: Int = 1001
    private val MEDIA_CAMERA_VIDEO_PERMISSION_REQUEST_CODE: Int = 1002
    private val MEDIA_PHOTOS_PERMISSION_REQUEST_CODE: Int = 1003
    private val MEDIA_VIDEOS_PERMISSION_REQUEST_CODE: Int = 1004
    private val REQUEST_MEDIA_CAMERA_PHOTO: Int = 2001
    private val REQUEST_MEDIA_CAMERA_VIDEO: Int = 2002
    private val REQUEST_MEDIA_PHOTO: Int = 2003
    private val REQUEST_MEDIA_VIDEO: Int = 2004

    var editorContentParsedSHA256LastSwitch: ByteArray = ByteArray(0)
    var sourceContentParsedSHA256LastSwitch: ByteArray = ByteArray(0)
    var homeFragment = HomeFragment()
    lateinit var edit_title : EditText
    lateinit var edit_category : EditText
    lateinit var edit_save : LinearLayout
    lateinit var like_bt : LinearLayout
    lateinit var pdf_iv : ImageView
    lateinit var  edit_scroll :ScrollView
    var likeFlag : Boolean = false

    lateinit var tempDTO : InterestWikiContract

    private val isRunningTest: Boolean by lazy {
        try {
            Class.forName("androidx.test.espresso.Espresso")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }

    private val viewModel: EditFragmentViewModel by activityViewModels() // hilt 로 editfragment viewmodel 주입
    var ReadOrWrite : String = "write"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //글이 작성되었을때 리턴값을 받아와서 그때 toast 를 보내는 함수
        viewModel.wikiDTOInsertLiveData.observe(this , androidx.lifecycle.Observer {
            if(it != 0L){
                Toast.makeText(context, "발행되었습니다.", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.editStateLiveData.observe(this, androidx.lifecycle.Observer {
            readOrwirteTypeCheck(it)
        })

        viewModel.insertInterestLiveData.observe(this , androidx.lifecycle.Observer {
            if(it != 0L) {
                likeFlag = true
                like_iv.setImageResource(R.drawable.ic_baseline_favorite_24)
                Toast.makeText(context, "관심 목록에 추가됐습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.deleteInterestLiveData.observe(this , androidx.lifecycle.Observer {
            if(it == "삭제"){
                likeFlag = false
                like_iv.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                Toast.makeText(context, "관심 목록에서 해제됐습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.getInterestLiveData.observe(this , androidx.lifecycle.Observer {
            if(it!=null){
                likeFlag = true
                like_iv.setImageResource(R.drawable.ic_baseline_favorite_24)
            }else{
                likeFlag = false
                like_iv.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditBinding.inflate(layoutInflater)
        val view = binding.root

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            && !resources.getBoolean(R.bool.is_large_tablet_landscape)) {
            mHideActionBarOnSoftKeyboardUp = true
        }

        visualEditor = view.findViewById<AztecText>(R.id.et_editor)
        sourceEditor = view.findViewById<SourceViewEditText>(R.id.tv_preview)
        toolbar = view.findViewById<AztecToolbar>(R.id.formatting_toolbar)
        edit_title = view.findViewById<EditText>(R.id.edit_title)
        edit_category = view.findViewById<EditText>(R.id.edit_category)
        edit_save = view.findViewById<LinearLayout>(R.id.edit_save)
        like_bt = view.findViewById<LinearLayout>(R.id.like_bt)
        pdf_iv = view.findViewById(R.id.pdf_iv)
        edit_scroll = view.findViewById(R.id.edit_scroll)

        val galleryButton = MediaToolbarGalleryButton(toolbar)
        galleryButton.setMediaToolbarButtonClickListener(object : IMediaToolbarButton.IMediaToolbarClickListener {
            override fun onClick(view: View) {
                mediaMenu = PopupMenu(context, view)
                mediaMenu?.setOnMenuItemClickListener(this@EditFragment)
                mediaMenu?.inflate(R.menu.menu_gallery)
                mediaMenu?.show()
                if (view is ToggleButton) {
                    view.isChecked = false
                }
            }
        })

        val cameraButton = MediaToolbarCameraButton(toolbar)
        cameraButton.setMediaToolbarButtonClickListener(object : IMediaToolbarButton.IMediaToolbarClickListener {
            override fun onClick(view: View) {
                mediaMenu = PopupMenu(context, view)
                mediaMenu?.setOnMenuItemClickListener(this@EditFragment)
                mediaMenu?.inflate(R.menu.menu_camera)
                mediaMenu?.show()
                if (view is ToggleButton) {
                    view.isChecked = false
                }
            }
        })

        //읽기 모드에서는 보이고 쓰기 모드에서는 안보여야한다.
        edit_save.setOnClickListener {
            readOrwirteTypeCheck("edit")
        }


        //pdf 버튼 누르기
        pdf_iv.setOnClickListener {
            pdfUtil = PdfUtil(requireContext())
            pdfUtil.layoutToImage(edit_scroll) // pdf 다운로드
        }


        //좋아요 누르기
        like_bt.setOnClickListener {
            if(!likeFlag){
                viewModel.insertInterestWiki(tempDTO) //wiki 를 클릭해서 상세 페이지로 넘어왔을때 interest 로 넘길 수 있는 객체로 생성해준 변수 tempDTO
            }else{
                viewModel.deleteInterestWiki(tempDTO.id!!)
            }
        }

        //TODO 처음 발행일때랑 수정하는거랑 구분하는게 필요함. 처음 발행일때는 insert 수정할때는 update 해야해서
        view.edit_emit.setOnClickListener {
            ////html으로 바로 변경
            val editorHtml = visualEditor!!.toPlainHtml(true)
            val sha256 = AztecText.calculateSHA256(editorHtml)
            if (editorContentParsedSHA256LastSwitch.isEmpty()) {
                editorContentParsedSHA256LastSwitch = sha256
            }
            if (visualEditor!!.hasChanges() != AztecText.EditorHasChanges.NO_CHANGES || !Arrays.equals(editorContentParsedSHA256LastSwitch, sha256)) {
                sourceEditor!!.displayStyledAndFormattedHtml(editorHtml)
            }
            editorContentParsedSHA256LastSwitch = sha256
            Log.v("sfdsdf" , visualEditor.text.toString())

            ////html으로 바로 변경
            saveWiki(edit_title.text.toString() , edit_category.text.toString() ,
                "" , sourceEditor.text.toString() , "" , visualEditor.text.toString())
        }

        aztec = Aztec.with(visualEditor, sourceEditor , toolbar, this)
            .setImageGetter(GlideImageLoader(requireContext()))
            .setVideoThumbnailGetter(GlideVideoThumbnailLoader(requireContext()))
            .setOnImeBackListener(this)
            .setOnTouchListener(this)
            .setHistoryListener(this)
            .setOnImageTappedListener(this)
            .setOnVideoTappedListener(this)
            .setOnAudioTappedListener(this)
            .setOnMediaDeletedListener(this)
            .setOnVideoInfoRequestedListener(this)
            .addPlugin(WordPressCommentsPlugin(visualEditor))
            .addPlugin(MoreToolbarButton(visualEditor))
            .addPlugin(PageToolbarButton(visualEditor))
            .addPlugin(CaptionShortcodePlugin(visualEditor))
            .addPlugin(VideoShortcodePlugin())
            .addPlugin(AudioShortcodePlugin())
            .addPlugin(HiddenGutenbergPlugin(visualEditor))
            .addPlugin(galleryButton)
            .addPlugin(cameraButton)

        initHtmltoString("")


        viewModel.clickWikiItem.observe(this , androidx.lifecycle.Observer {
            if(it != null){
                var temp = it.wikiDTO!!
                tempDTO = InterestWikiContract(temp.id , temp.date ,temp.title ,temp.category,temp.content, temp.images,temp.rawContent)

                initHtmltoString(it.wikiDTO?.content.toString())
                edit_title.setText(it.wikiDTO?.title.toString())
                edit_category.setText(it.wikiDTO?.category.toString())
                viewModel.setState(it.readOrWrite)

                viewModel.getInterestWiki(temp.title) // 지금 읽어온 wiki가 내 관심 목록에 있는가? 알아오는 함수
            }
        })

        if (!isRunningTest) {
            aztec.visualEditor.enableCrashLogging(object : AztecExceptionHandler.ExceptionHandlerHelper {
                override fun shouldLog(ex: Throwable): Boolean {
                    return true
                }
            })
            aztec.visualEditor.setCalypsoMode(false)
            aztec.sourceEditor?.setCalypsoMode(false)
            aztec.addPlugin(CssUnderlinePlugin())
        }

        invalidateOptionsHandler = Handler()
        invalidateOptionsRunnable = Runnable {invalidateOptionsRunnable}
        initView()


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            var bitmap: Bitmap
            when (requestCode) {
                REQUEST_MEDIA_CAMERA_PHOTO -> {
                    val options = BitmapFactory.Options()
                    options.inDensity = DisplayMetrics.DENSITY_DEFAULT
                    bitmap = BitmapFactory.decodeFile(mediaPath, options)
                    insertImageAndSimulateUpload(bitmap, mediaPath)
                }
                REQUEST_MEDIA_PHOTO -> {
                    mediaPath = data?.data.toString()
                    val stream = requireActivity().contentResolver.openInputStream(Uri.parse(mediaPath))
                    val options = BitmapFactory.Options()
                    options.inDensity = DisplayMetrics.DENSITY_DEFAULT
                    bitmap = BitmapFactory.decodeStream(stream, null, options)!!
                    insertImageAndSimulateUpload(bitmap, mediaPath)
                }
                REQUEST_MEDIA_CAMERA_VIDEO -> {
                    mediaPath = data?.data.toString()
                }
                REQUEST_MEDIA_VIDEO -> {
                    mediaPath = data?.data.toString()

                    aztec.visualEditor.videoThumbnailGetter?.loadVideoThumbnail(mediaPath, object : Html.VideoThumbnailGetter.Callbacks {
                        override fun onThumbnailFailed() {}
                        override fun onThumbnailLoaded(drawable: Drawable?) {
                            val conf = Bitmap.Config.ARGB_8888 // see other conf types
                            bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight, conf)
                            val canvas = Canvas(bitmap)
                            drawable.setBounds(0, 0, canvas.width, canvas.height)
                            drawable.draw(canvas)
                        //    insertVideoAndSimulateUpload(bitmap, mediaPath)
                        }
                        override fun onThumbnailLoading(drawable: Drawable?) {}
                    }, this.resources.displayMetrics.widthPixels)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)



    }

    fun saveWiki(title : String , category : String , date : String , content : String , image: String , rawContent: String){
        var tempDto = WikiContract(null,date,title,category,content,image , rawContent)
        viewModel.insertWiki(tempDto , "local") // api 통신할거면 여기에 api를 적을것
    }


    fun readOrwirteTypeCheck(rw : String){
        when(rw){
            "read" ->{
                toolbar.visibility = View.GONE
                edit_save.visibility = View.VISIBLE
                edit_emit.visibility = View.GONE
                like_bt.visibility = View.VISIBLE

                edit_title.setClickable(false)
                edit_title.setFocusable(false)
                edit_category.setClickable(false)
                edit_category.setFocusable(false)
                visualEditor.setClickable(false)
                visualEditor.setFocusable(false)

            }
            "write"->{
                edit_title.setText("")
                edit_category.setText("")
                visualEditor.setText("")
                toolbar.visibility = View.VISIBLE
                edit_emit.visibility = View.VISIBLE
                edit_save.visibility = View.GONE
                like_bt.visibility = View.GONE

                edit_title.setFocusableInTouchMode(true)
                edit_title.setClickable(true)
                edit_title.setFocusable(true)

                edit_category.setFocusableInTouchMode(true)
                edit_category.setClickable(true)
                edit_category.setFocusable(true)

                visualEditor.setFocusableInTouchMode(true)
                visualEditor.setClickable(true)
                visualEditor.setFocusable(true)
            }
            "edit"->{
                toolbar.visibility = View.VISIBLE
                edit_emit.visibility = View.VISIBLE
                edit_save.visibility = View.GONE

                visualEditor.setFocusableInTouchMode(true)
                visualEditor.setClickable(true)
                visualEditor.setFocusable(true)
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }

    //db에서 html 코드를 불러왔을때 그걸 위키 형식에 맞게 자동으로 변환해주는 함수
    //aztec 객체 설정뒤에 불러줘야 모든 값이 온전하게 출력된다.
    fun initHtmltoString(text : String){
        sourceEditor.setText(text)
        val sourceHtml = sourceEditor!!.getPureHtml(true)
        val sha256 = AztecText.calculateSHA256(sourceHtml)
        if (sourceContentParsedSHA256LastSwitch.isEmpty()) {
            sourceContentParsedSHA256LastSwitch = sha256
        }
        if (sourceEditor!!.hasChanges() != AztecText.EditorHasChanges.NO_CHANGES || !Arrays.equals(sourceContentParsedSHA256LastSwitch, sha256)) {
            visualEditor!!.fromHtml(sourceHtml)
        }
        sourceContentParsedSHA256LastSwitch = sha256
    }

    private fun insertImageAndSimulateUpload(bitmap: Bitmap?, mediaPath: String) {
        val bitmapResized = ImageUtils.getScaledBitmapAtLongestSide(bitmap, aztec.visualEditor.maxImagesWidth)
        val (id, attrs) = generateAttributesForMedia(mediaPath, isVideo = false)
        aztec.visualEditor.insertImage(BitmapDrawable(resources, bitmapResized), attrs)
        insertMediaAndSimulateUpload(id, attrs)
        aztec.toolbar.toggleMediaToolbar()
    }

    fun insertVideoAndSimulateUpload(bitmap: Bitmap?, mediaPath: String) {
        val bitmapResized = ImageUtils.getScaledBitmapAtLongestSide(bitmap, aztec.visualEditor.maxImagesWidth)
        val (id, attrs) = generateAttributesForMedia(mediaPath, isVideo = true)
        aztec.visualEditor.insertVideo(BitmapDrawable(resources, bitmapResized), attrs)
        insertMediaAndSimulateUpload(id, attrs)
        aztec.toolbar.toggleMediaToolbar()
    }

    private fun generateAttributesForMedia(mediaPath: String, isVideo: Boolean): Pair<String, AztecAttributes> {
        val id = Random().nextInt(Integer.MAX_VALUE).toString()
        val attrs = AztecAttributes()
        attrs.setValue("src", mediaPath) // Temporary source value.  Replace with URL after uploaded.
        attrs.setValue("id", id)
        attrs.setValue("uploading", "true")

        if (isVideo) {
            attrs.setValue("video", "true")
        }

        return Pair(id, attrs)
    }

    private fun insertMediaAndSimulateUpload(id: String, attrs: AztecAttributes) {
        val predicate = object : AztecText.AttributePredicate {
            override fun matches(attrs: Attributes): Boolean {
                return attrs.getValue("id") == id
            }
        }

        aztec.visualEditor.setOverlay(predicate, 0, ColorDrawable(0x80000000.toInt()), Gravity.FILL)
        aztec.visualEditor.updateElementAttributes(predicate, attrs)

        val progressDrawable = AppCompatResources.getDrawable(requireContext(), android.R.drawable.progress_horizontal)!!
        // set the height of the progress bar to 2 (it's in dp since the drawable will be adjusted by the span)
        progressDrawable.setBounds(0, 0, 0, 4)

        aztec.visualEditor.setOverlay(predicate, 1, progressDrawable, Gravity.FILL_HORIZONTAL or Gravity.TOP)
        aztec.visualEditor.updateElementAttributes(predicate, attrs)

        var progress = 0

        val runnable = Runnable {
            aztec.visualEditor.setOverlayLevel(predicate, 1, progress)
            aztec.visualEditor.updateElementAttributes(predicate, attrs)
            aztec.visualEditor.resetAttributedMediaSpan(predicate)
            progress += 2000

            if (progress >= 10000) {
                attrs.removeAttribute(attrs.getIndex("uploading"))
                aztec.visualEditor.clearOverlays(predicate)

                if (attrs.hasAttribute("video")) {
                    attrs.removeAttribute(attrs.getIndex("video"))
                    aztec.visualEditor.setOverlay(predicate, 0, AppCompatResources.getDrawable(requireContext(), android.R.drawable.ic_media_play), Gravity.CENTER)
                }

                aztec.visualEditor.updateElementAttributes(predicate, attrs)
            }
        }

        Handler().post(runnable)
        Handler().postDelayed(runnable, 2000)
        Handler().postDelayed(runnable, 4000)
        Handler().postDelayed(runnable, 6000)
        Handler().postDelayed(runnable, 8000)

        aztec.visualEditor.refreshText()
    }


    private fun initView() {

        binding.etEditor.apply {
            setCalypsoMode(false)
            doAfterTextChanged { onShowTextPreview() }
            customSelectionActionModeCallback = this@EditFragment
        }

        visualEditor.externalLogger = object : AztecLog.ExternalLogger {
            override fun log(message: String) {
            }

            override fun logException(tr: Throwable) {
            }

            override fun logException(tr: Throwable, message: String) {
            }
        }

        val galleryButton = MediaToolbarGalleryButton(toolbar)
        galleryButton.setMediaToolbarButtonClickListener(object : IMediaToolbarButton.IMediaToolbarClickListener {
            override fun onClick(view: View) {
                mediaMenu = PopupMenu(context, view)
                mediaMenu?.setOnMenuItemClickListener(this@EditFragment)
                mediaMenu?.inflate(R.menu.menu_gallery)
                mediaMenu?.show()
                if (view is ToggleButton) {
                    view.isChecked = false
                }
            }
        })

        val cameraButton = MediaToolbarCameraButton(toolbar)
        cameraButton.setMediaToolbarButtonClickListener(object : IMediaToolbarButton.IMediaToolbarClickListener {
            override fun onClick(view: View) {
                mediaMenu = PopupMenu(context, view)
                mediaMenu?.setOnMenuItemClickListener(this@EditFragment)
                mediaMenu?.inflate(R.menu.menu_camera)
                mediaMenu?.show()
                if (view is ToggleButton) {
                    view.isChecked = false
                }
            }
        })

    }

    override fun onAudioTapped(attrs: AztecAttributes) {
        val url = attrs.getValue("src")
        url?.let {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.setDataAndType(Uri.parse(url), "audio/*")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                try {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                } catch (e: ActivityNotFoundException) {

                }
            }
        }
    }

    override fun onImageTapped(attrs: AztecAttributes, naturalWidth: Int, naturalHeight: Int) {
    }

    override fun onImeBack() {
        mIsKeyboardOpen = false
        showActionBarIfNeeded()
    }

    override fun onMediaDeleted(attrs: AztecAttributes) {
        val url = attrs.getValue("src")
    }

    override fun onVideoInfoRequested(attrs: AztecAttributes) {
        if (attrs.hasAttribute(ATTRIBUTE_VIDEOPRESS_HIDDEN_ID)) {
            aztec.visualEditor.postDelayed({
                aztec.visualEditor.updateVideoPressThumb(
                    "https://videos.files.wordpress.com/OcobLTqC/img_5786_hd.original.jpg",
                    "https://videos.files.wordpress.com/OcobLTqC/img_5786.m4v",
                    attrs.getValue(ATTRIBUTE_VIDEOPRESS_HIDDEN_ID))
            }, 500)
        }
    }

    override fun onVideoTapped(attrs: AztecAttributes) {
        val url = if (attrs.hasAttribute(ATTRIBUTE_VIDEOPRESS_HIDDEN_SRC)) {
            attrs.getValue(ATTRIBUTE_VIDEOPRESS_HIDDEN_SRC)
        } else {
            attrs.getValue("src")
        }

        url?.let {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.setDataAndType(Uri.parse(url), "video/*")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                try {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                } catch (e: ActivityNotFoundException) {
               //     ToastUtils.showToast(this, "Video tapped!")
                }
            }
        }
    }



    override fun onRedo() {
    }

    override fun onRedoEnabled() {
       // invalidateOptionsHandler.removeCallbacks(invalidateOptionsRunnable)
       // invalidateOptionsHandler.postDelayed(invalidateOptionsRunnable, resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
    }

    override fun onUndo() {
    }

    override fun onUndoEnabled() {
       // invalidateOptionsHandler.removeCallbacks(invalidateOptionsRunnable)
      //  invalidateOptionsHandler.postDelayed(invalidateOptionsRunnable, resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
    }

    override fun onToolbarCollapseButtonClicked() {
    }

    override fun onToolbarExpandButtonClicked() {
    }

    override fun onToolbarFormatButtonClicked(format: ITextFormat, isKeyboardShortcut: Boolean) {
       // ToastUtils.showToast(this, format.toString())
    }

    override fun onToolbarHeadingButtonClicked() {
    }

    override fun onToolbarHtmlButtonClicked() {
        val uploadingPredicate = object : AztecText.AttributePredicate {
            override fun matches(attrs: Attributes): Boolean {
                return attrs.getIndex("uploading") > -1
            }
        }

        val mediaPending = aztec.visualEditor.getAllElementAttributes(uploadingPredicate).isNotEmpty()

        if (mediaPending) {
            //ToastUtils.showToast(this, R.string.media_upload_dialog_message)
        } else {
            aztec.toolbar.toggleEditorMode()
        }
    }

    override fun onToolbarListButtonClicked() {
    }

    override fun onToolbarMediaButtonClicked(): Boolean {
        return false
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        item?.isChecked = (item?.isChecked == false)

        return when (item?.itemId) {
            R.id.take_photo -> {
                onCameraPhotoMediaOptionSelected()
                true
            }
            R.id.take_video -> {
                onCameraVideoMediaOptionSelected()
                true
            }
            R.id.gallery_photo -> {
                onPhotosMediaOptionSelected()
                true
            }
            R.id.gallery_video -> {
                onVideosMediaOptionSelected()
                true
            }
            else -> false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            // If the WebView or EditText has received a touch event, the keyboard will be displayed and the action bar
            // should hide
            mIsKeyboardOpen = true
            hideActionBarIfNeeded()
        }
        return false
    }



    private fun onCameraPhotoMediaOptionSelected() {
        if (PermissionUtils.checkAndRequestCameraAndStoragePermissions(this, MEDIA_CAMERA_PHOTO_PERMISSION_REQUEST_CODE)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE) // MediaStore 사용

            mediaFile = "wp-" + System.currentTimeMillis() + ".jpg"
            mediaPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() +
                    File.separator + "Camera" + File.separator + mediaFile
            intent.putExtra(
                MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(requireContext(),
                "com.supremehyo.awiki" + ".provider", File(mediaPath)
                ))

            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(intent, REQUEST_MEDIA_CAMERA_PHOTO)
            }
        }
    }

    private fun onCameraVideoMediaOptionSelected() {
        if (PermissionUtils.checkAndRequestCameraAndStoragePermissions(this, MEDIA_CAMERA_PHOTO_PERMISSION_REQUEST_CODE)) {
            val intent = Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA)

            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(intent, REQUEST_MEDIA_CAMERA_VIDEO)
            }
        }
    }

    private fun onPhotosMediaOptionSelected() {
        if (PermissionUtils.checkAndRequestStoragePermission(this, MEDIA_PHOTOS_PERMISSION_REQUEST_CODE)) {
            val intent: Intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent(Intent.ACTION_OPEN_DOCUMENT)
            } else {
                Intent.createChooser(Intent(Intent.ACTION_GET_CONTENT), getString(R.string.title_select_photo))
            }

            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"

            try {
                startActivityForResult(intent, REQUEST_MEDIA_PHOTO)
            } catch (exception: ActivityNotFoundException) {
                AppLog.e(AppLog.T.EDITOR, exception.message)
            }
        }
    }

    private fun onVideosMediaOptionSelected() {
        if (PermissionUtils.checkAndRequestStoragePermission(this, MEDIA_PHOTOS_PERMISSION_REQUEST_CODE)) {
            val intent: Intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent(Intent.ACTION_OPEN_DOCUMENT)
            } else {
                Intent.createChooser(Intent(Intent.ACTION_GET_CONTENT), getString(R.string.title_select_video))
            }

            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "video/*"

            try {
                startActivityForResult(intent, REQUEST_MEDIA_VIDEO)
            } catch (exception: ActivityNotFoundException) {
                AppLog.e(AppLog.T.EDITOR, exception.message)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MEDIA_CAMERA_PHOTO_PERMISSION_REQUEST_CODE,
            MEDIA_CAMERA_VIDEO_PERMISSION_REQUEST_CODE -> {
                var isPermissionDenied = false

                for (i in grantResults.indices) {
                    when (permissions[i]) {
                        Manifest.permission.CAMERA -> {
                            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                                isPermissionDenied = true
                            }
                        }
                        Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                                isPermissionDenied = true
                            }
                        }
                    }
                }

                if (isPermissionDenied) {
                } else {
                    when (requestCode) {
                        MEDIA_CAMERA_PHOTO_PERMISSION_REQUEST_CODE -> {
                            onCameraPhotoMediaOptionSelected()
                        }
                        MEDIA_CAMERA_VIDEO_PERMISSION_REQUEST_CODE -> {
                            onCameraVideoMediaOptionSelected()
                        }
                    }
                }
            }
            MEDIA_PHOTOS_PERMISSION_REQUEST_CODE,
            MEDIA_VIDEOS_PERMISSION_REQUEST_CODE -> {
                var isPermissionDenied = false

                for (i in grantResults.indices) {
                    when (permissions[i]) {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                                isPermissionDenied = true
                            }
                        }
                    }
                }

                when (requestCode) {
                    MEDIA_PHOTOS_PERMISSION_REQUEST_CODE -> {
                        if (isPermissionDenied) {
                        } else {
                            onPhotosMediaOptionSelected()
                        }
                    }
                    MEDIA_VIDEOS_PERMISSION_REQUEST_CODE -> {
                        if (isPermissionDenied) {
                        } else {
                            onVideosMediaOptionSelected()
                        }
                    }
                }
            }
            else -> {
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun hideActionBarIfNeeded() {
/*
        val actionBar = supportActionBar
        if (actionBar != null
            && !isHardwareKeyboardPresent()
            && mHideActionBarOnSoftKeyboardUp
            && mIsKeyboardOpen
            && actionBar.isShowing) {
            actionBar.hide()
        }*/
    }
    private fun isHardwareKeyboardPresent(): Boolean {
        val config = resources.configuration
        var returnValue = false
        if (config.keyboard != Configuration.KEYBOARD_NOKEYS) {
            returnValue = true
        }
        return returnValue
    }
    /**
     * Show the action bar if needed.
     */
    private fun showActionBarIfNeeded() {

        /*
        val actionBar = supportActionBar
        if (actionBar != null && !actionBar.isShowing) {
            actionBar.show()
        }*/
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

        // Update HTML result in live preview
        onShowTextPreview()
    }

    override fun onApplyOrderedListSpan() {
        binding.etEditor.toggleFormatting(AztecTextFormat.FORMAT_ORDERED_LIST)

        // Update HTML result in live preview
        onShowTextPreview()
    }

    override fun onApplyUnorderedListSpan() {
        binding.etEditor.toggleFormatting(AztecTextFormat.FORMAT_UNORDERED_LIST)

        // Update HTML result in live preview
        onShowTextPreview()
    }
    override fun onShowTextPreview() {

    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.menu_actions, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        // Reorder menu item. Make custom action mode menu first
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




}