package com.ymts0579.agritourism

import android.annotation.SuppressLint
import android.app.ProgressDialog
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

class Adminviewusers : AppCompatActivity() {
    lateinit var listuser: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adminviewusers)

        listuser=findViewById(R.id.listuser)
        listuser.layoutManager = LinearLayoutManager(this)
        listuser.setHasFixedSize(true)
        val k=findViewById<TextView>(R.id.sdsd)
        var num=0
        val p= ProgressDialog(this)
        p.show()
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.users("User","adminuser")
                .enqueue(object : Callback<Userresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<Userresponse>, response: Response<Userresponse>) {
                        p.dismiss()

                        listuser.adapter = useradminadapter(this@Adminviewusers, response.body()!!.user)
                        for (item in response.body()!!.user){
                            num++
                        }
                        k.setText("Number of User->$num")
                    }

                    override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                        Toast.makeText(this@Adminviewusers, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }
}


class useradminadapter(var context: Context, var listdata: ArrayList<User>):
    RecyclerView.Adapter<useradminadapter.DataViewHolder>(){
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