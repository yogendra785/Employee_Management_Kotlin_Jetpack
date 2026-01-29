package com.example.neutron.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import com.example.neutron.domain.model.SalaryRecord
import java.io.File
import java.io.FileOutputStream

class PdfGenerator (private val context: Context){
    fun generateSalarySlip(record: SalaryRecord, employeeName: String){
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300,500,1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        //PDF Design

        paint.textSize=16f
        paint.isFakeBoldText=true
        canvas.drawText("Salary Slip",100f,100f,paint)

        paint.textSize = 10f
        paint.isFakeBoldText=false
        canvas.drawText("Month: ${record.month}",20f,80f,paint)
        canvas.drawText("Employee:$employeeName",20f,100f,paint)

        canvas.drawLine(20f,110f,280f,110f,paint)

        canvas.drawText("Base Salary:", 20f, 140f, paint)
        canvas.drawText("₹${record.baseSalary}", 200f, 140f, paint)

        paint.color = Color.RED
        canvas.drawText("Absence Penalty:", 20f, 170f, paint)
        canvas.drawText("-₹${record.absentDays * record.perDayDeduction}", 200f, 170f, paint)

        canvas.drawText("Advance Taken:", 20f, 200f, paint)
        canvas.drawText("-₹${record.advancePaid}", 200f, 200f, paint)

        paint.color = Color.BLACK
        canvas.drawLine(20f, 220f, 280f, 220f, paint)

        paint.isFakeBoldText = true
        canvas.drawText("NET PAYABLE:", 20f, 250f, paint)
        canvas.drawText("₹${record.netPayable}", 200f, 250f, paint)

        pdfDocument.finishPage(page)

        //--save to storage

        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(directory,"PaySlip_${employeeName}_${record.month.replace(" ","_")}.pdf")

        try{
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(context,"PDF Saved: ${file.absolutePath}",Toast.LENGTH_LONG).show()

        }catch (e: Exception){
            Toast.makeText(context,"Error: ${e.message}",Toast.LENGTH_SHORT).show()

        }finally {
            pdfDocument.close()
        }

    }
}