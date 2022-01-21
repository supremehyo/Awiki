package com.supremehyo.awiki.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.widget.RelativeLayout
import android.widget.ScrollView
import java.io.*

class PdfUtil internal constructor(var context: Context) {

    var dirpath: String? = null


    public fun layoutToImage(edit_scroll: ScrollView) {
        val bm = Bitmap.createBitmap(
            edit_scroll.width,
            edit_scroll.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bm)
        val bgDrawable = edit_scroll.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        edit_scroll.draw(canvas)
        val bytes = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val bitmapData = bytes.toByteArray()
        var  temp = ByteArrayInputStream(bitmapData)

        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "new.jpg")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
        // 파일을 write중이라면 다른곳에서 데이터요구를 무시하겠다는 의미입니다.
        // 파일을 write중이라면 다른곳에서 데이터요구를 무시하겠다는 의미입니다.
        values.put(MediaStore.Images.Media.IS_PENDING, 1)

        val contentResolver: ContentResolver = context.contentResolver
        val collection: Uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val item: Uri? = contentResolver.insert(collection, values)

        try {
            val pdf: ParcelFileDescriptor? = contentResolver.openFileDescriptor(item!!, "w", null)
            if (pdf == null) {
            } else {
                val inputStream: InputStream = temp
                val strToByte: ByteArray = getBytes(inputStream)!!
                val fos: FileOutputStream = FileOutputStream(pdf.getFileDescriptor())
                fos.write(strToByte)
                fos.close()
                inputStream.close()
                pdf.close()
                contentResolver.update(item!!, values, null, null)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        values.clear()
        // 파일을 모두 write하고 다른곳에서 사용할 수 있도록 0으로 업데이트를 해줍니다.
        values.put(MediaStore.Images.Media.IS_PENDING, 0)
        contentResolver.update(item!!, values, null, null)
        /*

        Log.v("sdfsdf ", Environment.getExternalStorageDirectory().toString() + File.separator + "image.jpg")
        val f = File(
            Environment.getExternalStorageDirectory().toString() + File.separator + "image.jpg"
        )
        try {
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            val document = Document()
            dirpath = Environment.getExternalStorageDirectory().toString()

            PdfWriter.getInstance(document, FileOutputStream("$dirpath/newPDF.pdf"))
            document.open()
            val image = Image.getInstance(f.toString())
            val scaler = (document.pageSize.width - document.leftMargin()
                    - document.rightMargin() - 0) / image.width * 100
            image.scalePercent(scaler)
            image.alignment = Image.ALIGN_CENTER or Image.ALIGN_TOP
            document.add(image)
            document.close()
            Toast.makeText(context, "PDF 파일 저장성공", Toast.LENGTH_SHORT).show()
            f.delete()


        } catch (e: Exception) {
            e.printStackTrace()
        }*/
    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }
}