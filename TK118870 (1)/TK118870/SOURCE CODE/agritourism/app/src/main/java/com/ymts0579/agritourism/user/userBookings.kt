package com.ymts0579.agritourism.user

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ymts0579.agritourism.R
import com.ymts0579.agritourism.RetrofitClient
import com.ymts0579.agritourism.Showlocation
import com.ymts0579.agritourism.model.Booking
import com.ymts0579.agritourism.model.BookingResponse
import com.ymts0579.agritourism.useradminadapter
import com.ymts0579.fooddonationapp.model.Userresponse
import com.ymts0579.model.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class userBookings : AppCompatActivity() {
    lateinit var listuserBooking:RecyclerView
    var email=""
    lateinit var fused: FusedLocationProviderClient
    var pl = ""
    var lat = ""
    var lon = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_bookings)
        fused= LocationServices.getFusedLocationProviderClient(this)
        listuserBooking=findViewById(R.id.listuserBooking)
        listuserBooking.layoutManager = LinearLayoutManager(this)
        listuserBooking.setHasFixedSize(true)

        getSharedPreferences("user", MODE_PRIVATE).apply {
            email=getString("email","").toString()

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


        val p= ProgressDialog(this)
        p.show()
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.userBooing("$email","viewuserbooking")
                .enqueue(object : Callback<BookingResponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<BookingResponse>, response: Response<BookingResponse>) {
                        p.dismiss()
                        listuserBooking.adapter=userbookingadapter(this@userBookings,response.body()!!.user,pl)
                        Toast.makeText(this@userBookings, "${response.body()!!.user}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                        Toast.makeText(this@userBookings, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }
}


class userbookingadapter(var context: Context, var listdata: ArrayList<Booking>,var to:String):
    RecyclerView.Adapter<userbookingadapter.DataViewHolder>(){
    var id=0
    var from=""
    class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvfname1:TextView=view.findViewById(R.id.tvfname1)
        val tvcfnum:TextView=view.findViewById(R.id.tvcfnum)
        val tvcstatus:TextView=view.findViewById(R.id.tvcstatus)
        val tvdays:TextView=view.findViewById(R.id.tvdays)
        val tvpersons:TextView=view.findViewById(R.id.tvpersons)
        val tvdate:TextView=view.findViewById(R.id.tvdate)
        val tvfbook:TextView=view.findViewById(R.id.tvfbook)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cardyuserbooking, parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
        val kk=listdata[position]
        holder.tvfname1.text=kk.fname
        holder.tvcfnum.text=kk.fnumber
        holder.tvcstatus.text=kk.status
        holder.tvdays.text=kk.days
        holder.tvpersons.text=kk.persons
        holder.tvdate.text=kk.date
        holder.tvfbook.text=kk.Bookid
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.usersemail("${kk.femail}","useremail")
                .enqueue(object : Callback<Userresponse> {
                    override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<Userresponse>, response: Response<Userresponse>) {
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        val iii=response.body()!!
                        if(iii.user.isNotEmpty()) {
                            val iiii=iii.user[0]

                           from=iiii.address
                        }

                    }
                })
        }
        holder.itemView.setOnClickListener {
            val intent= Intent(context, Showlocation::class.java)
            intent.putExtra("to",from)
            intent.putExtra("from",to)
                context.startActivity(intent)
        }



    }


    override fun getItemCount() = listdata.size
}