package com.Decesion.decesionapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Decesion.decesionapp.R.layout.activity_main

import org.json.JSONArray
import org.json.JSONObject
import java.util.*


class MainActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {


    lateinit var edit_fromdate: EditText
    lateinit var edit_todate: EditText
    lateinit var recycler_List: RecyclerView


    lateinit var decNum: EditText
    lateinit var decText: EditText

    //  lateinit var comlist_sp: Spinner

    lateinit var oList: ArrayList<listModel>
    lateinit var CList: ArrayList<CommintyModel>
    lateinit var adapter: CustomAdapter
    lateinit var pdia: ProgressDialog
    lateinit var list_of_items : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)

//        callbackManager = CallbackManager.Factory.create();
//        shareDialog =  ShareDialog(this);
//
//        if (ShareDialog.canShow(ShareLinkContent::class.java)) {
//            val linkContent = ShareLinkContent.Builder()
//                .setQuote("ddddd")
//                .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
//                 .build()
//            shareDialog.show(linkContent)
//        }
//


        this.getSupportActionBar()?.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()?.setDisplayShowCustomEnabled(true);
        getSupportActionBar()?.setCustomView(R.layout.actionbar);
        val image=findViewById<View>(resources.getIdentifier("profile", "id", packageName)) as ImageView

        image.setOnClickListener{
            val editor=getSharedPreferences("decicesion", Context.MODE_PRIVATE).edit()

            editor.putString("isLogin", "false")
            editor.apply()
            finish()
        }

        edit_todate = findViewById(R.id.edit_todate) as EditText
        edit_fromdate = findViewById(R.id.edit_fromdate) as EditText
        //   comlist_sp = findViewById(R.id.comlist_sp) as Spinner
        recycler_List = findViewById(R.id.recycler_List) as RecyclerView

        decNum = findViewById(R.id.decNum) as EditText
        decText = findViewById(R.id.decText) as EditText

        //  getCommityList()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
      //  callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        // use position to know the selected item
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

    fun btn_Todate(view: View) {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this@MainActivity, DatePickerDialog.OnDateSetListener
        { view, year, monthOfYear, dayOfMonth ->
            edit_todate.setText("" + dayOfMonth + "/" + (monthOfYear+1) + "/" + year)
        }, year, month, day)
        datePickerDialog.show()


    }


    fun btn_formdate(view: View) {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this@MainActivity, DatePickerDialog.OnDateSetListener
        { view, year, monthOfYear, dayOfMonth ->
            edit_fromdate.setText("" + dayOfMonth + "/" + (monthOfYear+1) + "/" + year)
        }, year, month, day)
        datePickerDialog.show()

    }

    fun btn_getlist(view: View) {


        getDecesionList()
    }



    fun getDecesionList(){

        oList = java.util.ArrayList<listModel>()

        pdia =  ProgressDialog(this);

        pdia.setMessage("Loading...")
        pdia.show()

        val editor=getSharedPreferences("decicesion", Context.MODE_PRIVATE)

        val params=HashMap<String, String?>()
        params["Login_Name"]= editor.getString("Login_Name", "")
        // params["Login_Name"]= "hsalfi"
        params["Decision_Text"]= decText.text.toString()
        params["Sub_No"]="00"
        params["Decision_NO"]=decNum.text.toString()
        params["Comm_No"]=""
        params["To_Session_Date"]=edit_todate.text.toString()
        params["From_Session_Date"]=edit_fromdate.text.toString()
        params["Session_No"]=""
        params["Session_ID"]=""


        val jsonArray=JSONObject(
            params as Map<*, *>
        )
        val editor1=getSharedPreferences("desision", Context.MODE_PRIVATE)

        Log.d("jsonArray",jsonArray.toString())
        Log.d("BaseURL",editor1.getString("BaseURL","") +"api/Decision/DecisionList")

        val task=GeneralTask(
            editor1.getString("BaseURL","") +"api/Decision/DecisionList" ,
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
                            var oModel: listModel
                            var intIndex: Int = 0

                            while (intIndex < jsonArr.length()) {

                                Log.d("d: ",  jsonArr.getJSONObject(intIndex).toString())
                                oModel = listModel(
                                    jsonArr.getJSONObject(intIndex).getString("Decision_Text"),
                                    jsonArr.getJSONObject(intIndex).getString("Session_Date"),
                                    jsonArr.getJSONObject(intIndex).getString("Session_Status_Desc"),
                                    jsonArr.getJSONObject(intIndex).getString("Decision_ID"),
                                )
                                oList.add(oModel)
                                intIndex++
                            }
                            callAdapter()

                        }
                        pdia.dismiss()
                    }else{
                        showDialogMesseage("البيانات المدخلة غير صحيحة")
                        callAdapter()
                    }
                }
            }
        )

        task.execute()


    }

    fun callAdapter() {

        hideKeyboard()

        adapter = CustomAdapter(oList,this)
        recycler_List.layoutManager = LinearLayoutManager(this)
        recycler_List.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recycler_List.adapter = adapter

    }






//    fun getCommityList(){
//
//        pdia =  ProgressDialog(this);
//
//        pdia.setMessage("Loading...")
//        pdia.show()
//
//        CList = java.util.ArrayList<CommintyModel>()
//
//        val task=GeneralTask(
//            BaseURL + "api/Decision/CommityList" ,
//            "GET",
//            "",
//
//
//            object : GeneralTask.TaskListener {
//
//                override fun onFinished(result: String) {
//
//
//                    Log.d("get", result)
//
//                    val jsonArr: JSONObject = JSONObject(result)
//                    var Result_Object = jsonArr.getJSONArray("Result_Object")
//
//                    if (Result_Object.length() == 0) {
//                        showDialogMesseage("لا تتوفر بيانات")
//                    } else {
//
//
//                        var intIndex: Int = 0
//
//                        var oModel: CommintyModel
//
//                        while (intIndex < Result_Object.length()) {
//                            oModel = CommintyModel(
//                                Result_Object.getJSONObject(intIndex).getInt("Comm_no"),
//                                Result_Object.getJSONObject(intIndex).getString("Comm_Name"),
//
//                                )
//                            CList.add(oModel)
//
//                            intIndex++
//                        }
//
//
//
//                        setSpinner()
//                        pdia.dismiss()
//                    }
//                }
//            })
//
//        task.execute()
//    }


//fun  setSpinner(){
//    comlist_sp!!.setOnItemSelectedListener(this)
//
//    list_of_items = java.util.ArrayList<String>()
//
//    for (item in CList){
//        list_of_items.add(item.Comm_Name)
//    }
//
//    // Create an ArrayAdapter using a simple spinner layout and languages array
//    val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item,list_of_items )
//    // Set layout to use when the list of choices appear
//    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//    // Set Adapter to Spinner
//    comlist_sp!!.setAdapter(aa)
//
//}

    fun showDialogMesseage(Message: String){


        val builder = AlertDialog.Builder(this)
        builder.setTitle("تنبيه")
        builder.setMessage(Message)

        builder.setPositiveButton("موافق") { dialog, which ->



        }
        builder.show()
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}