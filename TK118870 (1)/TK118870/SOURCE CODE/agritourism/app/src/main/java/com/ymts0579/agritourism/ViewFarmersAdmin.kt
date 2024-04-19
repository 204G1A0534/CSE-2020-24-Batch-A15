package com.ymts0579.agritourism

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ymts0579.fooddonationapp.model.Userresponse
import com.ymts0579.model.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewFarmersAdmin : AppCompatActivity() {
    lateinit var listfarmers:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_farmers)
        listfarmers=findViewById(R.id.listfarmers)
        listfarmers.layoutManager = LinearLayoutManager(this)
        listfarmers.setHasFixedSize(true)
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.users("Farmer","adminuser")
                .enqueue(object : Callback<Userresponse> {
                    override fun onResponse(call: Call<Userresponse>, response: Response<Userresponse>) {


                        listfarmers.adapter = useradapter(this@ViewFarmersAdmin, response.body()!!.user)

                    }

                    override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                        Toast.makeText(this@ViewFarmersAdmin, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }

    }
}



class useradapter(var context: Context,var listdata: ArrayList<User>):
    RecyclerView.Adapter<useradapter.DataViewHolder>(){
    var id=0
    class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvfname:TextView=view.findViewById(R.id.tvfname);
        val tvfemail:TextView=view.findViewById(R.id.tvfemail);
        val tvfnum:TextView=view.findViewById(R.id.tvfnum);
        val tvfcity:TextView=view.findViewById(R.id.tvfcity);


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cardfarmers, parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
           var kk=listdata.get(position)
        holder.tvfname.text=kk.name
        holder.tvfemail.text=kk.email
        holder.tvfnum.text=kk.moblie
        holder.tvfcity.text=kk.city



    }


    override fun getItemCount() = listdata.size
}