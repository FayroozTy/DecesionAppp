package com.Decesion.decesionapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class DeatilsActivity : AppCompatActivity() {
    lateinit var dectxt: TextView
    lateinit var attchment: ImageButton
    lateinit var h: ImageView
    lateinit var oList: java.util.ArrayList<listModel>
    lateinit var CList: java.util.ArrayList<CommintyModel>
    lateinit var adapter: CustomAdapter
    lateinit var pdia: ProgressDialog
    lateinit var dID: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deatils)

        dectxt = findViewById(R.id.tt)

        attchment  = findViewById(R.id.attchment)

        var decesionText = intent.getStringExtra("decesionText")
        dectxt.setText(decesionText)

        dID = intent.getStringExtra("decesionid").toString()


    }

    fun btn_attch(view: View) {

        getattchList()


    }




    fun getattchList(){

        oList = java.util.ArrayList<listModel>()

        pdia =  ProgressDialog(this);

        pdia.setMessage("Loading...")
        pdia.show()

        val editor=getSharedPreferences("decicesion", Context.MODE_PRIVATE)

        val params= HashMap<String, String?>()
        params["LoginName"]= editor.getString("Login_Name", "")

        params["DecisionID"]=dID



        val jsonArray= JSONObject(
            params as Map<*, *>
        )
        val editor1=getSharedPreferences("desision", Context.MODE_PRIVATE)

        Log.d("jsonArray",jsonArray.toString())
        Log.d("BaseURL",editor1.getString("BaseURL","") +"api/Decision/DecisionListAttachments")

        val task=GeneralTask(
            editor1.getString("BaseURL","") +"api/Decision/DecisionListAttachments" ,
            "POST",
            jsonArray.toString(),


            object : GeneralTask.TaskListener {

                override fun onFinished(result: String) {
                    Log.d("result: ", "f" + result + "hhh")


                    var reader: JSONObject = JSONObject(result)

                    if (result.contains("Success")) {

                        val jsonArr: JSONArray = reader.getJSONArray("Result_Object");

                        if (jsonArr.length() == 0) {
                            showDialogMesseage("لا تتوفر بيانات")
                        }else {

                            var intIndex: Int = 0

                            while (intIndex < jsonArr.length()) {

//                                val imgFile = File(" http://83.244.112.170:5146/ArchivingFiles/Kararat/97fbe17c-8a5c-4b87-bc19-16fbfd1c5a09.tif")
//
//
//                                val imageBytes = Base64.decode("http:/83.244.112.170:5146/ArchivingFiles/Kararat/97fbe17c-8a5c-4b87-bc19-16fbfd1c5a09.tif", Base64.DEFAULT)
//                                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//
//                                h.setImageBitmap(decodedImage)
//
//decodedImage

                                Log.d("d: ", editor1.getString("BaseURL","")+jsonArr[intIndex].toString())

                                val imageList = ArrayList<SlideModel>() // Create image list


                                imageList.add(SlideModel(editor1.getString("BaseURL","")+jsonArr[intIndex].toString(), ""))


                                val imageSlider = findViewById<ImageSlider>(R.id.image_slider)

                                imageSlider.setImageList(imageList, ScaleTypes.FIT)
                                imageSlider.setItemClickListener(object : ItemClickListener {
                                    override fun onItemSelected(position: Int) {




                                    }
                                })
                                imageSlider.setImageList(imageList)

                                intIndex++
                            }


                        }
                        pdia.dismiss()
                    }else{
                        showDialogMesseage("البيانات المدخلة غير صحيحة")
                    }
                }
            }
        )

        task.execute()


    }

    fun showDialogMesseage(Message: String){


        val builder = AlertDialog.Builder(this)
        builder.setTitle("تنبيه")
        builder.setMessage(Message)

        builder.setPositiveButton("موافق") { dialog, which ->



        }
        builder.show()
    }



}
