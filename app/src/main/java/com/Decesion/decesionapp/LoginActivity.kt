package com.Decesion.decesionapp

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText


import org.json.JSONObject
import java.util.HashMap
var Token = ""
class LoginActivity : AppCompatActivity() {

    lateinit var user_name: EditText
    lateinit var password: EditText
    lateinit var pdia: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.getSupportActionBar()?.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()?.setDisplayShowCustomEnabled(true);
        getSupportActionBar()?.setCustomView(R.layout.actionbar);

        setContentView(R.layout.activity_login)

        user_name = findViewById(R.id.email) as EditText
        password = findViewById(R.id.password) as EditText
        appContext = this
    }


    fun btn_login(view: View) {



        var ed_Name = user_name.text.toString()
        var password_ed = password.text.toString()

        if (ed_Name.isEmpty() || password_ed.isEmpty()) {


            showDialogMesseage("الرجاء ادخال كافة المعلومات")


        } else {

            pdia = ProgressDialog(this);
            pdia.setMessage("Loading...")
            pdia.show()
            //omdbi

            val params = HashMap<String, String?>()
            params["UserName"] = user_name.text.toString()
            params["Password"] = password.text.toString()
            params["Program_Copy_ID"] = "100"


            val jsonArray = JSONObject(
                params as Map<*, *>)

            Log.d("jsonArray",jsonArray.toString())

            val editor=getSharedPreferences("desision", Context.MODE_PRIVATE)

            Log.d("BaseURL",editor.getString("BaseURL","") +"api/Resp/SignIn")

            val task = GeneralTask(
                editor.getString("BaseURL","") +"api/Resp/SignIn",
                "POST",
                jsonArray.toString(),


                object : GeneralTask.TaskListener {

                    override fun onFinished(result: String) {


                        Log.d("result: ", "r" + result)

                        if (result.contains("Success")){



                            var reader: JSONObject =  JSONObject(result)

                            val jsonArr: JSONObject = reader.getJSONObject("Result_Object");

//                                var Api_User_Name =  jsonArr.getString("Api_User_Name")
//                                var Api_Password =  jsonArr.getString("Api_Password")

                            val editor=getSharedPreferences("decicesion", Context.MODE_PRIVATE).edit()

                            editor.putString("isLogin", "true")
                            editor.putString("Login_Name", jsonArr.getString("Login_Name"))


                            editor.apply()



                            val message = jsonArr.getString("Access_Token")


                            Token = "Bearer " + message
                            Log.d("Token",Token)

                            val editor1= getSharedPreferences("Token", Context.MODE_PRIVATE).edit()
                            editor1.putString("token", Token)

                            editor1.apply()


                            pdia.dismiss()
                            nextScreen()


                            // getToken(Api_User_Name,Api_Password)

                        }else{
                            showDialogMesseage("البيانات المدخلة غير صحيحة")
                            pdia.dismiss()
                        }

                    }

                }
            )

            task.execute()

        }
    }











//    fun getToken(Api_User_Name: String,Api_Password: String){
//
//        val params = HashMap<String, String?>()
//        params["Content-Type"] = "application/x-www-form-urlencoded"
//        params["username"] = Api_User_Name
//        params["password"] = Api_Password
//        params["grant_type"] = "password"
//
//        var parmeter = "grant_type=password&password=" + Api_Password + "&username=" + Api_User_Name + "&Content-Type=application/x-www-form-urlencoded"
//
//        val task = GeneralTask(
//            BaseURL + "token",
//            "POST",
//            parmeter,
//
//
//            object : GeneralTask.TaskListener {
//
//                override fun onFinished(result: String) {
//                    Log.e("TAG", "response: " + result)
//
//                    val responseObj = JSONObject(result)
//
//                    val message = responseObj.getString("access_token")
//
//
//                    Token = "Bearer " + message
//
//                    val editor= getSharedPreferences("Token", Context.MODE_PRIVATE).edit()
//                    editor.putString("token", Token)
//
//                    editor.apply()
//
//                    Log.d("resultssss", Token)
//                    pdia.dismiss()
//
//                    nextScreen()
//
//                }
//
//            }
//        )
//
//        task.execute()
//
//    }






//    fun getToken(Api_User_Name: String,Api_Password: String){
//        var volleyRequestQueue: RequestQueue? = null
//        //  var dialog: ProgressDialog? = null
//        val serverAPIURL: String = BaseURL + "token"
//        val TAG = "Handy Opinion Tutorials"
//        volleyRequestQueue = Volley.newRequestQueue(this)
//        //    dialog = ProgressDialog.show(this.requireContext(), "", "Please wait...", true);
//        val parameters: MutableMap<String, String> = HashMap()
//        // Add your parameters in HashMap
//        parameters.put("Content-Type","application/x-www-form-urlencoded");
//        parameters.put("username",Api_User_Name);
//        parameters.put("grant_type","password");
//        parameters.put("password",Api_Password);
//
//        Log.d("parameters", parameters.toString())
//
//
//        val strReq: StringRequest = object : StringRequest(
//            Method.POST,serverAPIURL,
//            Response.Listener { response ->
//
//                Log.e(TAG, "response: " + response)
//                // dialog?.dismiss()
//                pdia.dismiss()
//
//                // Handle Server response here
//                try {
//                    val responseObj = JSONObject(response)
//
//                    val message = responseObj.getString("access_token")
//
//
//                    Token = "Bearer " + message
//
//                    val editor= getSharedPreferences("Token", Context.MODE_PRIVATE).edit()
//                    editor.putString("token", Token)
//
//                    editor.apply()
//
//                    Log.d("resultssss", Token)
//
//                    nextScreen()
//
//
//
//
//                } catch (e: Exception) { // caught while parsing the response
//                    Log.e(TAG, "problem occurred")
//                    e.printStackTrace()
//                }
//            },
//            Response.ErrorListener { volleyError -> // error occurred
//                Log.e(TAG, "problem occurred, volley error: " + volleyError.message)
//            }) {
//
//            override fun getParams(): MutableMap<String, String> {
//                return parameters;
//            }
//
//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//
//                val headers: MutableMap<String, String> = HashMap()
//                // Add your Header paramters here
//                return headers
//            }
//        }
//        // Adding request to request queue
//        volleyRequestQueue?.add(strReq)
//        /////////////
//
//
//    }

    fun nextScreen(){

        var intent=Intent(this, MainActivity::class.java)
        startActivity(intent)
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