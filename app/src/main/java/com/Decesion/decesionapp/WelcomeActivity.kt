package com.Decesion.decesionapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class WelcomeActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

//        val runnable = Runnable() {
//
//            val context = applicationContext
//            val wm = context.getSystemService(WIFI_SERVICE) as WifiManager
//            val ip: String = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
//            Log.d("ip",ip)
//
//            if (ip.contains("10.10")){
//                Log.d("address","local")
//                BaseURL = "http://192.168.0.170:5146/"
//            }else{
//                Log.d("address","out")
//                BaseURL = "http://83.244.112.170:5146/"
//            }
//        }
//        Handler().postDelayed(runnable, SPLASH_TIME_OUT)

    }

    fun  btnStart_Click(view: View){

        chooseNetwork()

//        callbackManager = CallbackManager.Factory.create();
//        shareDialog =  ShareDialog(this);
//
//        if (ShareDialog.canShow(ShareLinkContent::class.java)) {
//            val linkContent = ShareLinkContent.Builder()
//                .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
//                .build()
//            shareDialog.show(linkContent)
//        }



    }



    fun chooseNetwork(){
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage("الرجاء اختيار نوع الشبكة للتمكن من استخدام التطبيق")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("شبكة داخلية"){dialogInterface, which ->
                BaseURL ="http://192.168.0.170:5146/"


                val editor= getSharedPreferences("desision", Context.MODE_PRIVATE).edit()
                editor.putString("BaseURL", "http://192.168.0.170:5146/")

                editor.apply()

                val editor1=getSharedPreferences("decicesion", Context.MODE_PRIVATE)

                if (editor1.getString("isLogin", "").equals("true")){



                    val intent = Intent(this, MainActivity::class.java)
                    startActivityForResult(intent,1)
                }
                else{

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }



            }



            // negative button text and action
            .setNegativeButton("شبكة خارجية") {dialogInterface, which ->
                BaseURL ="http://83.244.112.170:5146/"



                val editor= getSharedPreferences("desision", Context.MODE_PRIVATE).edit()
                editor.putString("BaseURL","http://83.244.112.170:5146/")

                editor.apply()


                val editor1=getSharedPreferences("decicesion", Context.MODE_PRIVATE)

                if (editor1.getString("isLogin", "").equals("true")){



                    val intent = Intent(this, MainActivity::class.java)
                    startActivityForResult(intent,1)
                }
                else{

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }


            }
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("تنبيه")
        // show alert dialog
        alert.show()
    }


}

