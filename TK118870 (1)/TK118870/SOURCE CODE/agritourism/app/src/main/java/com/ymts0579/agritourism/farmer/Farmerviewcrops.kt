package com.ymts0579.agritourism.farmer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ymts0579.agritourism.Login
import com.ymts0579.agritourism.R
import com.ymts0579.agritourism.RetrofitClient
import com.ymts0579.agritourism.model.Crop
import com.ymts0579.agritourism.model.CropinfoResponse
import com.ymts0579.agritourism.useradapter
import com.ymts0579.fooddonationapp.model.Userresponse
import com.ymts0579.model.model.DefaultResponse
import com.ymts0579.model.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Farmerviewcrops : AppCompatActivity() {
    lateinit var list: RecyclerView
    var email=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farmerviewcrops)
        list=findViewById(R.id.listfarmercrops)
        list.layoutManager = LinearLayoutManager(this)
        list.setHasFixedSize(true)

        getSharedPreferences("user", MODE_PRIVATE).apply {
            email= getString("email","").toString()
        }
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.framercrops("$email","viewcropsframer")
                .enqueue(object : Callback<CropinfoResponse> {
                    override fun onResponse(call: Call<CropinfoResponse>, response: Response<CropinfoResponse>) {

                              list.adapter=framercropviewadapter(this@Farmerviewcrops,response.body()!!.user)


                    }

                    override fun onFailure(call: Call<CropinfoResponse>, t: Throwable) {
                        Toast.makeText(this@Farmerviewcrops, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }
}



class framercropviewadapter(var context: Context, var listdata: ArrayList<Crop>):
    RecyclerView.Adapter<framercropviewadapter.DataViewHolder>(){
    var id=0
    class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {

       var tvcname:TextView=view.findViewById(R.id.tvcname)
       var tvcfields:TextView=view.findViewById(R.id.tvcfields)
       var tvcstatus:TextView=view.findViewById(R.id.tvcstatus)
       var tvcdes:TextView=view.findViewById(R.id.tvcdes)
        var tvcid:TextView=view.findViewById(R.id.tvcid)
       var img1:ImageView=view.findViewById(R.id.img1)
       var img2:ImageView=view.findViewById(R.id.img2)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cardcropinfo, parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
        var kk=listdata.get(position)
        holder.tvcname.text=kk.name
        holder.tvcfields.text=kk.field
        holder.tvcstatus.text=kk.status
        holder.tvcdes.text=kk.des
        holder.tvcid.text=kk.cropid

        val uri= Uri.parse(kk.path.trim())
        Glide.with(context).load(uri).into(holder.img1)
        val url=Uri.parse(kk.path2.trim())
        Glide.with(context).load(url).into(holder.img2)


        holder.itemView.setOnClickListener {
            val alertdialog= AlertDialog.Builder(context)
            alertdialog.setIcon(R.drawable.ic_launcher_foreground)
            alertdialog.setTitle("Completed crop")
            alertdialog.setIcon(R.drawable.img)
            alertdialog.setPositiveButton("Yes"){ alertdialog, which->
                compeleted(kk.id)
            }
            alertdialog.setNegativeButton("No"){alertdialog,which->
                Toast.makeText(context,"thank you", Toast.LENGTH_SHORT).show()
                alertdialog.dismiss()
            }
            alertdialog.show()




        }

    }

    private fun compeleted(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.statuscrops("Completed",id,"updatestatus")
                .enqueue(object : Callback<DefaultResponse> {
                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {

                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()


                    }

                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }

    }


    override fun getItemCount() = listdata.size
}