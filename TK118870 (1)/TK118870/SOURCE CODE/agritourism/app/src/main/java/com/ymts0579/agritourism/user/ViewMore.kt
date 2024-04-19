package com.ymts0579.agritourism.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.agritourism.Login
import com.ymts0579.agritourism.R
import com.ymts0579.agritourism.RetrofitClient
import com.ymts0579.agritourism.Showlocation
import com.ymts0579.agritourism.model.CropinfoResponse
import com.ymts0579.fooddonationapp.model.Userresponse
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewMore : AppCompatActivity() {
    lateinit var tvcid:TextView
    lateinit var  tvcname:TextView
    lateinit var  tvcfields:TextView
    lateinit var  tvcstatus:TextView
    lateinit var  tvcdes:TextView
    lateinit var tvfname:TextView
    lateinit var tvfemail:TextView
    lateinit var tvfnum:TextView
    lateinit var tvfcity:TextView
    lateinit var tvfaddress:TextView
    lateinit var  img1:ImageView
    lateinit var  img2:ImageView
    lateinit var taptoview:TextView
    lateinit var linermode:LinearLayout
    lateinit var viewlocations:TextView
    lateinit var etdays:EditText
    lateinit var  etpersons:EditText

    lateinit var  etdate:EditText
    lateinit var btnbook:Button
    var email=""
    lateinit var fused: FusedLocationProviderClient
    var pl = ""
    var lat = ""
    var lon = ""
    var name=""
    var mob=""
    var uemail=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_more)
        tvcid=findViewById(R.id.tvcid)
        tvcname=findViewById(R.id.tvcname)
        tvcfields=findViewById(R.id.tvcfields)
        tvcstatus=findViewById(R.id.tvcstatus)
        tvcdes=findViewById(R.id.tvcdes)
        tvfname=findViewById(R.id.tvfname)
        tvfemail=findViewById(R.id.tvfemail)
        tvfnum=findViewById(R.id.tvfnum)
        tvfcity=findViewById(R.id.tvfcity)
        tvfaddress=findViewById(R.id.tvfaddress)
        img1=findViewById(R.id.img1)
        taptoview=findViewById(R.id.taptoview)
        img2=findViewById(R.id.img2)
        linermode=findViewById(R.id.linermode)
        viewlocations=findViewById(R.id.viewlocations)
        etdays=findViewById(R.id.etdays)
        etpersons=findViewById(R.id.etpersons)
        etdate=findViewById(R.id.etdate)
        btnbook=findViewById(R.id.btnbook)
        val id=intent.getStringExtra("id")
        linermode.visibility= View.GONE
        fused= LocationServices.getFusedLocationProviderClient(this)
        getSharedPreferences("user", MODE_PRIVATE).apply {
          mob= getString("mob","").toString()
           name=getString("name","").toString()
            uemail=getString("email","").toString()

        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),100)
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),100)
        }else {
            fused.lastLocation.addOnSuccessListener { it ->
                val geo = Geocoder(this)
                lat = it.latitude.toString()
                lon = it.longitude.toString()
                val k = geo.getFromLocation(it.latitude, it.longitude, 1)
               // place = k.get(0).locality
                pl = k.get(0).getAddressLine(0)
                 Toast.makeText(this, "$pl", Toast.LENGTH_SHORT).show()
                getSharedPreferences("userlocation", MODE_PRIVATE).edit().apply {
                    putString("address",pl)

                    apply()
                }
            }
            fused.lastLocation.addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()

            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.cropdetails("$id","cropdetails")
                .enqueue(object : Callback<CropinfoResponse> {
                    override fun onFailure(call: Call<CropinfoResponse>, t: Throwable) {
                        Toast.makeText(this@ViewMore, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<CropinfoResponse>, response: Response<CropinfoResponse>) {
                        Toast.makeText(this@ViewMore, response.body()!!.message, Toast.LENGTH_SHORT).show()
                         val iii=response.body()!!.user[0]
                        tvcid.setText(id)
                        tvcdes.setText(iii.des)
                        tvcname.setText(iii.name)
                        tvcfields.setText(iii.field)
                        tvcstatus.setText(iii.status)
                        email=iii.email

                        CoroutineScope(Dispatchers.IO).launch {
                            RetrofitClient.instance.usersemail("$email","useremail")
                                .enqueue(object : Callback<Userresponse> {
                                    override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                                        Toast.makeText(this@ViewMore, t.message, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onResponse(call: Call<Userresponse>, response: Response<Userresponse>) {
                                        Toast.makeText(this@ViewMore, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                        val iii=response.body()!!
                                        if(iii.user.isNotEmpty()) {
                                            val iiii=iii.user[0]
                                            tvfname.setText(iiii.name)
                                            tvfemail.setText(iiii.email)
                                            tvfnum.setText(iiii.moblie)
                                            tvfcity.setText(iiii.city)
                                            tvfaddress.setText(iiii.address)
                                        }

                                    }
                                })
                        }
                        val uri= Uri.parse(iii.path.trim())
                        Glide.with(this@ViewMore).load(uri).into(img1)
                        val url= Uri.parse(iii.path2.trim())
                        Glide.with(this@ViewMore).load(url).into(img2)


                    }
                })
        }



        taptoview.setOnClickListener {
            linermode.visibility= View.VISIBLE
            Handler().postDelayed({
                linermode.visibility= View.GONE
            },9000)


        }

        viewlocations.setOnClickListener {
            val intent=Intent(this, Showlocation::class.java)
            intent.putExtra("to",tvfaddress.text.toString())
            intent.putExtra("from",pl)
            startActivity(intent)
        }

     btnbook.setOnClickListener {
        val date=etdate.text.toString()
        val days=etdays.text.toString()
        val persons=etpersons.text.toString()
         val bookid="slot${(1000..9999).shuffled().last()}"
         if(date.isEmpty()&&days.isEmpty()&&persons.isEmpty()) {
             Snackbar.make(it,"Enter the required Fields",Snackbar.LENGTH_SHORT).show()
         }else{
             CoroutineScope(Dispatchers.IO).launch {
                 RetrofitClient.instance.addBooing(
                     tvfname.text.toString(), tvfemail.text.toString(), tvfnum.text.toString(),"Pending",
                     name, mob, uemail,
                     bookid, etdate.text.toString(), etdays.text.toString(),
                     etpersons.text.toString(),"addBookings")
                     .enqueue(object : Callback<DefaultResponse> {
                         override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                             Toast.makeText(this@ViewMore, t.message, Toast.LENGTH_SHORT).show()
                         }

                         override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                             Toast.makeText(this@ViewMore, response.body()!!.message, Toast.LENGTH_SHORT).show()



                         }
                     })
             }

         }
     }

    }
}