package com.supremehyo.awiki.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Environment
import android.widget.RelativeLayout
import android.widget.Toast
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class PdfUtil internal constructor(var context: Context) {

    var dirpath: String? = null


    public fun layoutToImage(relativeLayout: RelativeLayout) {
        val bm = Bitmap.createBitmap(
            relativeLayout.width,
            relativeLayout.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bm)
        val bgDrawable = relativeLayout.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        relativeLayout.draw(canvas)
        val bytes = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
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
        }
    }
}