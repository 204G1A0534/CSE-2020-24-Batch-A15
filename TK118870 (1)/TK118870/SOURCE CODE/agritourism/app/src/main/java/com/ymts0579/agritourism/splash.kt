package com.ymts0579.agritourism

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.ymts0579.agritourism.farmer.FarmerDashboard
import com.ymts0579.agritourism.user.UserDashboard

class splash : AppCompatActivity() {
    val splashScreenTimeout=4000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var email=""
        getSharedPreferences("user", MODE_PRIVATE).apply {
            email=getString("type","").toString()
        }
        if(email.isNotEmpty()) {
            if (email=="Farmer"){
                startActivity(Intent(this,FarmerDashboard::class.java))
                finish()
            }else if(email=="User"){
                startActivity(Intent(this,UserDashboard::class.java))
                finish()
            }

        }else{
            val id=getSharedPreferences("admin", MODE_PRIVATE)
            val email=id.getString("ad",null).toString()
            if(email=="admin"){
               val ii= Intent(this, AdminDashBoard::class.java)
                Handler().postDelayed({
                    startActivity(ii)
                    finish()
                },0.toLong())
            }
            else{
                val ii= Intent(this,Login::class.java)
                Handler().postDelayed({
                    startActivity(ii)
                    finish()
                },splashScreenTimeout.toLong())
            }

        }

    }
}