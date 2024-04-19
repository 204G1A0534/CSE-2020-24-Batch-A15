package com.ymts0579.agritourism

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFarmers : AppCompatActivity() {
    lateinit var etfname:EditText
   lateinit var  etfnum:EditText
    lateinit var etfemail:EditText
    lateinit var etfaddress:EditText
    lateinit var etfcity:EditText
    lateinit var etfpass:EditText
    lateinit var btnfadd:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_framers)
        etfname=findViewById(R.id.etfname)
        etfnum=findViewById(R.id.etfnum)
        etfemail=findViewById(R.id.etfemail)
        etfaddress=findViewById(R.id.etfaddress)
        etfcity=findViewById(R.id.etfcity)
        etfpass=findViewById(R.id.etfpass)
        btnfadd=findViewById(R.id.btnfadd)


        btnfadd.setOnClickListener {
            val name=etfname.text.toString()
            val num=etfnum.text.toString()
            val email=etfemail.text.toString()
            val address=etfaddress.text.toString()
            val city=etfcity.text.toString()
            val pass=etfpass.text.toString()


            if(name.isNotEmpty()&&num.isNotEmpty()&&email.isNotEmpty()
                &&address.isNotEmpty()&&city.isNotEmpty()&&pass.isNotEmpty()&&pass.isNotEmpty()){
                if(num.count()==10 ){

                    CoroutineScope(Dispatchers.IO).launch {
                        RetrofitClient.instance.register(name,num,email,city,pass,
                            address,"Farmer","register")
                            .enqueue(object: Callback<DefaultResponse> {
                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                    t.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show() }
                                }
                                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                    response.body()?.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show()}
                                    etfname.text!!.clear()
                                    etfnum.text!!.clear()
                                    etfemail.text!!.clear()
                                    etfaddress.text!!.clear()
                                    etfcity.text!!.clear()
                                    etfpass.text!!.clear()

                                    Handler().postDelayed({
                                       startActivity(Intent(this@AddFarmers,ViewFarmersAdmin::class.java))
                                    },2000)
                                }
                            })
                    }


                }else{

                        Snackbar.make(it,"Enter Farmer phone number properly", Snackbar.LENGTH_SHORT).show()
                        etfnum.setError("Enter Farmer Number properly")
                }

            }else{
                Snackbar.make(it,"Enter the fields", Snackbar.LENGTH_SHORT).show()
                etfname.setError("Enter Farmer Name")
                etfnum.setError("Enter Farmer Number")
                etfemail.setError("Enter Farmer Email")
                etfaddress.setError("Enter Farmer Address")
                etfcity.setError("Enter Farmer City")
                etfpass.setError("Enter Farmer Password ")

            }

        }
    }
}