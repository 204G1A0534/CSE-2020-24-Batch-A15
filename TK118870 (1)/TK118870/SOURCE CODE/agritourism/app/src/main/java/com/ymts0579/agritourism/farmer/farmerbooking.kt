package com.ymts0579.agritourism.farmer

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.gsm.SmsManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ymts0579.agritourism.R
import com.ymts0579.agritourism.RetrofitClient
import com.ymts0579.agritourism.Showlocation
import com.ymts0579.agritourism.model.Booking
import com.ymts0579.agritourism.model.BookingResponse
import com.ymts0579.agritourism.user.userbookingadapter
import com.ymts0579.fooddonationapp.model.Userresponse
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class farmerbooking : AppCompatActivity() {
    lateinit var listuserBooking: RecyclerView
    var email=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farmerbooking)
        listuserBooking=findViewById(R.id.listuserBooking)
        listuserBooking.layoutManager = LinearLayoutManager(this)
        listuserBooking.setHasFixedSize(true)

        getSharedPreferences("user", MODE_PRIVATE).apply {
            email=getString("email","").toString()

        }

        val p= ProgressDialog(this)
        p.show()
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.farmerBooing("$email","viewfarmersbooking")
                .enqueue(object : Callback<BookingResponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<BookingResponse>, response: Response<BookingResponse>) {
                        p.dismiss()
                        listuserBooking.adapter=
                            farmerbookingadapter(this@farmerbooking,response.body()!!.user)
                        Toast.makeText(this@farmerbooking, "${response.body()!!.user}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                        Toast.makeText(this@farmerbooking, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }
}


class farmerbookingadapter(var context: Context, var listdata: ArrayList<Booking>):
    RecyclerView.Adapter<farmerbookingadapter.DataViewHolder>(){
    var id=0
    var from=""
    class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvfname1: TextView =view.findViewById(R.id.tvfname1)
        val tvcfnum: TextView =view.findViewById(R.id.tvcfnum)
        val tvcstatus: TextView =view.findViewById(R.id.tvcstatus)
        val tvdays: TextView =view.findViewById(R.id.tvdays)
        val tvpersons: TextView =view.findViewById(R.id.tvpersons)
        val tvdate: TextView =view.findViewById(R.id.tvdate)
        val tvfbook: TextView =view.findViewById(R.id.tvfbook)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cardyuserbooking, parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
        val kk=listdata[position]
        holder.tvfname1.text=kk.uname
        holder.tvcfnum.text=kk.umob
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


        if(kk.status=="Completed"){
            Toast.makeText(context, "Already Completed", Toast.LENGTH_SHORT).show()
        }else{
            holder.itemView.setOnClickListener {

                id=listdata[position].id
                var alertdialog= AlertDialog.Builder(context)
                alertdialog.setIcon(R.drawable.ic_launcher_foreground)
                alertdialog.setTitle("Accept/Reject/Completed")
                alertdialog.setIcon(R.drawable.img)
                alertdialog.setCancelable(false)
                alertdialog.setMessage("Are you Accept or Reject or Completed the request?")
                alertdialog.setPositiveButton("Yes"){ alertdialog, which->
                    Acceptedrequest(id,kk.umob)
                    alertdialog.dismiss()
                }
                alertdialog.setNegativeButton("No"){alertdialog,which->
                    rejectedrequest(id,kk.umob)
                    alertdialog.dismiss()
                }

                alertdialog.setNeutralButton("completed"){alertdialog,which->
                    completedrequest(id,kk.umob)
                    alertdialog.dismiss()
                }
                alertdialog.show()
            }
        }






    }

    private fun completedrequest(id: Int, num: String) {
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.statusBooing("Completed",id,"updatestatus")
                .enqueue(object : Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()

                    }
                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        if (TextUtils.isDigitsOnly(num)) {
                            val smsManager: SmsManager = SmsManager.getDefault()
                            smsManager.sendTextMessage(num, null, "your Request is Accepted", null, null)

                        }
                    }
                })
        }

    }

    private fun rejectedrequest(id: Int,num:String) {
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.statusBooing("Rejected",id,"updatestatus")
                .enqueue(object : Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()

                    }
                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        if (TextUtils.isDigitsOnly(num)) {
                            val smsManager: SmsManager = SmsManager.getDefault()
                            smsManager.sendTextMessage(num, null, "your Request is Accepted", null, null)

                        }
                    }
                })
        }
    }

    private fun Acceptedrequest(id: Int,num:String) {
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.statusBooing("Accepted",id,"updatestatus")
                .enqueue(object : Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()

                    }
                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        if (TextUtils.isDigitsOnly(num)) {
                            val smsManager: SmsManager = SmsManager.getDefault()
                            smsManager.sendTextMessage(num, null, "your Request is Accepted", null, null)

                        }
                    }
                })
        }

    }


    override fun getItemCount() = listdata.size
}