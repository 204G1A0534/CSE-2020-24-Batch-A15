package com.ymts0579.agritourism

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class AdminDashBoard : AppCompatActivity() {
    lateinit var btnaddfarmers:Button
    lateinit var btnadminefarmers:Button
    lateinit var btnlogout:Button
    lateinit var btnadmineuser:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dash_board)
        btnaddfarmers=findViewById(R.id.btnaddfarmers)
        btnadminefarmers=findViewById(R.id.btnadminefarmers)
        btnlogout=findViewById(R.id.btnlogout)
        btnadmineuser=findViewById(R.id.btnadmineuser)

        btnadmineuser.setOnClickListener { startActivity(Intent(this,Adminviewusers::class.java)) }
        btnaddfarmers.setOnClickListener { startActivity(Intent(this,AddFarmers::class.java)) }
        btnadminefarmers.setOnClickListener { startActivity(Intent(this,ViewFarmersAdmin::class.java)) }
        btnlogout.setOnClickListener {
            val alertdialog= AlertDialog.Builder(this)
            alertdialog.setIcon(R.drawable.ic_launcher_foreground)
            alertdialog.setTitle("LOGOUT")
            alertdialog.setIcon(R.drawable.img)
            alertdialog.setCancelable(false)
            alertdialog.setMessage("Do you Want to Logout?")
            alertdialog.setPositiveButton("Yes"){ alertdialog, which->
                startActivity(Intent(this, Login::class.java))
                finish()
                val  shared=getSharedPreferences("admin", AppCompatActivity.MODE_PRIVATE)
                shared.edit().clear().apply()
                alertdialog.dismiss()
            }
            alertdialog.setNegativeButton("No"){alertdialog,which->
                Toast.makeText(this,"thank you", Toast.LENGTH_SHORT).show()
                alertdialog.dismiss()
            }
            alertdialog.show()
        }
    }
}




