package com.ymts0579.agritourism

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.agritourism.farmer.FarmerDashboard
import com.ymts0579.agritourism.user.UserDashboard
import com.ymts0579.model.model.DefaultResponse
import com.ymts0579.model.model.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.readFieldOrNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    lateinit var btnlogin:Button
    lateinit var btnregister:TextView
    lateinit var cardlogin:CardView
    lateinit var cardregister:CardView
    lateinit var etname: EditText
    lateinit var etunum: EditText
    lateinit var etemail: EditText
    lateinit var etuaddress: EditText
    lateinit var etucity: EditText
    lateinit var etupass: EditText
    lateinit var etupass1: EditText
    lateinit var btnsubmit:Button
    lateinit var etloginEmail:EditText
    lateinit var etloginpassword:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnlogin= findViewById(R.id.btnlogin)
        btnregister=findViewById(R.id.btnregister)
        cardlogin=findViewById(R.id.cardlogin)
        cardregister=findViewById(R.id.cardregister)
        etname=findViewById(R.id.etname)
        etunum=findViewById(R.id.etunum)
        etemail=findViewById(R.id.etemail)
        etuaddress=findViewById(R.id.etuaddress)
        etucity=findViewById(R.id.etucity)
        etupass=findViewById(R.id.etupass)
        etupass1=findViewById(R.id.etupass1)
        etloginEmail=findViewById(R.id.etloginEmail)
        etloginpassword=findViewById(R.id.etloginpassword)
        btnsubmit=findViewById(R.id.btnsubmit)
        cardregister.visibility=View.GONE

        btnlogin.setOnClickListener {

            val email=etloginEmail.text.toString()
            val pass=etloginpassword.text.toString()


            if(email.isEmpty()&&pass.isEmpty()){
                Snackbar.make(it,"Enter your Email and Password",Snackbar.LENGTH_SHORT).show()
                etloginEmail.setError("Enter your Email")
                etloginpassword.setError("Enter your password")
            }else {
                if (email == "admin" && pass == "admin") {
                    getSharedPreferences("admin", MODE_PRIVATE).edit().apply {
                        putString("ad", email)
                        apply()
                    }
                    startActivity(Intent(this, AdminDashBoard::class.java))
                    finish()

                }else{
                    CoroutineScope(Dispatchers.IO).launch {
                        RetrofitClient.instance.login(email,pass,"login")
                            .enqueue(object: Callback<LoginResponse> {
                                override fun onResponse(
                                    call: Call<LoginResponse>, response: Response<LoginResponse>
                                ) {
                                    if(!response.body()?.error!!){
                                        val type=response.body()?.user
                                        if (type!=null) {
                                            getSharedPreferences("user", MODE_PRIVATE).edit().apply {
                                                putString("mob",type.moblie)
                                                putString("pass",type.password)
                                                putString("email",type.email)
                                                putString("name",type.name)
                                                putString("address",type.address)
                                                putString("city",type.city)
                                                putString("type",type.type)
                                                putInt("id",type.id)
                                                apply()
                                            }

                                            val kk=type.type

                                            if(kk=="Farmer"){
                                                Toast.makeText(this@Login, "Farmer", Toast.LENGTH_SHORT).show()
                                                startActivity(Intent(this@Login,FarmerDashboard::class.java))
                                                finish()


                                            }else if(kk=="User"){
                                                Toast.makeText(this@Login, "User", Toast.LENGTH_SHORT).show()
                                                startActivity(Intent(this@Login,UserDashboard::class.java))
                                                finish()
                                            }



                                        }
                                    }else{
                                        Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
                                    }

                                }

                                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()


                                }

                            })
                    }

                }
            }
        }


        btnregister.setOnClickListener {
            cardlogin.visibility= View.GONE
            cardregister.visibility=View.VISIBLE
            btnregister.visibility=View.GONE
        }



        btnsubmit.setOnClickListener {
            val nam=etname.text.toString()
            val num=etunum.text.toString()
            val email=etemail.text.toString()
            val add=etuaddress.text.toString()
            val city=etucity.text.toString()
            val pas=etupass.text.toString()
            val pass=etupass1.text.toString()
            if(nam.isNotEmpty()&&num.isNotEmpty()&&email.isNotEmpty()
                &&add.isNotEmpty()&&city.isNotEmpty()&&pas.isNotEmpty()&&pass.isNotEmpty()){
                if(num.count()==10 && pas==pass){

                    CoroutineScope(Dispatchers.IO).launch {
                        RetrofitClient.instance.register(nam,num,email,city,pass,add,"User","register")
                            .enqueue(object: Callback<DefaultResponse> {
                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                    t.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show() }
                                }
                                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                    response.body()?.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show()}
                                    etname.text!!.clear()
                                    etunum.text!!.clear()
                                    etemail.text!!.clear()
                                    etuaddress.text!!.clear()
                                    etucity.text!!.clear()
                                    etupass.text!!.clear()
                                    etupass1.text!!.clear()

                                    Handler().postDelayed({
                                        cardlogin.visibility= View.VISIBLE
                                        cardregister.visibility=View.GONE
                                        btnregister.visibility=View.VISIBLE
                                    },2000)
                                }
                            })
                    }

                }else{
                    if(num.count()==10){
                        Snackbar.make(it,"password and conform password are not matched", Snackbar.LENGTH_SHORT).show()
                        etupass.setError("Enter your Password Properly")
                        etupass1.setError("Enter your Conform Password Properly")
                    }else{
                        Snackbar.make(it,"Enter Your phone number properly", Snackbar.LENGTH_SHORT).show()
                        etunum.setError("Enter your Number properly")
                    }
                }

            }else{
                Snackbar.make(it,"Enter the fields", Snackbar.LENGTH_SHORT).show()
                etname.setError("Enter your Name")
                etunum.setError("Enter your Number")
                etemail.setError("Enter your Email")
                etuaddress.setError("Enter your Address")
                etucity.setError("Enter your City")
                etupass.setError("Enter your Password ")
                etupass1.setError("Enter your Conform Password ")
            }

        }




    }
}