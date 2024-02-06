package com.Decesion.decesionapp

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicHeader
import org.apache.http.protocol.HTTP
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
var BaseURL: String ="http://192.168.0.170:5146/"
//var BaseURL: String = "http://83.244.112.170:5146/"
var SPLASH_TIME_OUT: Long = 4000
public interface VolleyCallback {
    abstract fun onSuccessResponse(result: String)
    operator fun invoke(strResp: String) {

    }
}

var appContext: Context? = null

class GeneralTask(
    url: String,
    methoud: String,

    parm: String,

    // This is the reference to the associated listener


    private val taskListener: TaskListener?
) : AsyncTask<Void, Void, String>() {
    internal var URL_link=""
    internal var Method=""
    internal var Parm=""



    interface TaskListener {
        fun onFinished(result: String)
    }

    init {
        this.URL_link=url
        this.Method=methoud
        this.Parm = parm




    }// The listener reference is passed in through the constructor

    override fun doInBackground(vararg params: Void): String? {


        if (this.Method == "GET"){
            Log.d("m","GET")
            var result = ""
            try {
                val url = URL( this.URL_link)
                val httpURLConnection = url.openConnection() as HttpURLConnection

                //httpURLConnection.readTimeout = 8000
                //httpURLConnection.connectTimeout = 8000
                // httpURLConnection.doOutput = true
                val editor= appContext?.getSharedPreferences("Token", Context.MODE_PRIVATE)

               var t = editor?.getString("token", "")?.let { Log.d("Authorization", it) }

                httpURLConnection.setRequestProperty("Authorization",
                    editor?.getString("token", "") )

//                httpURLConnection.setRequestProperty("Authorization",
//                 "Bearer nWNcfwKEHErFiN_1US77nA27hVyiQvEVLGozkES2HulWAcM1vBpxfwHz4w19q9sL0KC0LCRoq7cwDMIyePxEOBrO5UMmC10tspex1_5goX2O5sQRjqRxrjyhtK2xckReUWafRZtF610f0CpQgE2JQ2vawmTKdzTAk3p75-RRW-S_7Wz5g0fJi4UG5YHYH6lYAmrxr6eksD8Yi-Wtq1-dOcgdN8yS1nFqKUv3XY5-1tks0biujEFW2d4kqVIUm8DHmGP0kyIr3lLoMALD-6zU312DmMcg0hnftjRC-YVbioHF26KDpO5vh6We28UQ2qkGxzdLHD2GeNdoid6oJ_XcyoPyaRj87-TM_801jfp6NLDdGl9HnWv2MS5hzXcCkaCM4QjqrrQ9E6AZ2HtIuhu6AXymGPQg6IRj9Wrf-rrFOnAYcH0HPGfBiyOl8mNSxhlrw2JmaaByF8G_DiShXbKqpTAfLEDxyfev_ULuVSLJSCTBw2a8GPqq5FM0SNUDzG4n")


                httpURLConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded")
                httpURLConnection.connect()

                val responseCode: Int = httpURLConnection.responseCode
                Log.d("responseCode", "responseCode - " + responseCode)

                //   if (responseCode == 200) {
                val inStream: InputStream= httpURLConnection.inputStream
                val isReader = InputStreamReader(inStream)
                val bReader = BufferedReader(isReader)
                var tempStr: String?

                try {

                    while (true) {
                        tempStr = bReader.readLine()
                        if (tempStr == null) {
                            break
                        }
                        result += tempStr
                    }
                } catch (Ex: Exception) {
                    Log.e("Error", "Error in convertToString " + Ex.printStackTrace())
                }
                //  }
            } catch (ex: Exception) {
                Log.d("", "Error in doInBackground " + ex.message)
            }
            return result
        }

        else{



            Log.d("m","post")
            val httpclient=DefaultHttpClient()
            val httppost=HttpPost(this.URL_link)
            var origresponseText=""


            try {
                // Add your data

                val entity=StringEntity(this.Parm, "UTF8")
                entity.contentType=BasicHeader(HTTP.CONTENT_TYPE, "application/json")

                httppost.setEntity( entity);

                val editor= appContext?.getSharedPreferences("Token", Context.MODE_PRIVATE)

                httppost.setHeader("Authorization", editor?.getString("token", ""))

               // httppost.setHeader("Authorization", "Bearer nWNcfwKEHErFiN_1US77nA27hVyiQvEVLGozkES2HulWAcM1vBpxfwHz4w19q9sL0KC0LCRoq7cwDMIyePxEOBrO5UMmC10tspex1_5goX2O5sQRjqRxrjyhtK2xckReUWafRZtF610f0CpQgE2JQ2vawmTKdzTAk3p75-RRW-S_7Wz5g0fJi4UG5YHYH6lYAmrxr6eksD8Yi-Wtq1-dOcgdN8yS1nFqKUv3XY5-1tks0biujEFW2d4kqVIUm8DHmGP0kyIr3lLoMALD-6zU312DmMcg0hnftjRC-YVbioHF26KDpO5vh6We28UQ2qkGxzdLHD2GeNdoid6oJ_XcyoPyaRj87-TM_801jfp6NLDdGl9HnWv2MS5hzXcCkaCM4QjqrrQ9E6AZ2HtIuhu6AXymGPQg6IRj9Wrf-rrFOnAYcH0HPGfBiyOl8mNSxhlrw2JmaaByF8G_DiShXbKqpTAfLEDxyfev_ULuVSLJSCTBw2a8GPqq5FM0SNUDzG4n")


                /* execute */
                val response=httpclient.execute(httppost)
                val rp=response.entity
                origresponseText= readContent(response)
            } catch (e: ClientProtocolException) {
                // TODO Auto-generated catch block
            } catch (e: IOException) {
                // TODO Auto-generated catch block
            }
            return origresponseText
           // return origresponseText.substring(7, origresponseText.length)

        }

    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)


        // Log.d("r",result);
        // In onPostExecute we check if the listener is valid
        if (this.taskListener != null) {

            // And if it is we call the callback function on it.
            this.taskListener.onFinished(result)
        }
    }


    internal fun readContent(response: HttpResponse): String {
        var text=""
        var `in`: InputStream?=null

        try {
            `in`=response.entity.content
            val reader=BufferedReader(InputStreamReader(`in`!!))
            val sb=StringBuilder()
            var line: String?=null


            /////
            while (true) {
                line = reader.readLine()
                if (line == null) {
                    break
                }
                sb.append(line!! + "\n")
            }
            ///



            text=sb.toString()
        } catch (e: IllegalStateException) {
            e.printStackTrace()

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {

                `in`!!.close()
            } catch (ex: Exception) {
            }

        }

        return text
    }

    public fun getAPIResult_JSONObject(context: Context, URL: String, method: Int, callback: VolleyCallback): String {
        //val URL = "http://data.daral-aalam.ps/API/SrearchBook"
        //val criteria = "كنز"
        //val criteria_Model: Criteria_Model = Criteria_Model(Field_Name = "Book_Title", Field_Value = criteria)
        val requestQueue= Volley.newRequestQueue(context)
        var jsonObject: JSONObject = JSONObject()
        //var jsonObject: JSONArray = JSONArray()
        //jsonObject.put("Field_Name", "Book_Title")
        //jsonObject.put("Field_Value", criteria)
        var result: String=""
        val jsonObjReq=object : JsonObjectRequest(method,
            URL, jsonObject,

            Response.Listener {

                callback.onSuccessResponse(it.toString())
            },
            Response.ErrorListener {

                if ("NoConnectionError" in it.toString()){
                    Toast.makeText(context, "لا يوجد اتصال بالانترنت الرجاء حاول لاحقا", Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                }
                Log.d("Error", it.toString())
            }){
            /** Passing some request headers*  */
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()

                val editor= context.getSharedPreferences("Token", Context.MODE_PRIVATE)
                var tokenValue = editor.getString("token", "")


                Log.d("tokenValue", tokenValue.toString())
                headers["Authorization"]=
                    tokenValue.toString()
                return headers
            }
        }
        val comparable=try {


            jsonObjReq.retryPolicy= DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            requestQueue.add(jsonObjReq)
        } catch (ex: Exception) {
            Toast.makeText(context, ex.message.toString(), Toast.LENGTH_LONG).show()
            Log.d("Error", ex.message.toString())
        }
        return result// objectRequest.toString()
    }

}






