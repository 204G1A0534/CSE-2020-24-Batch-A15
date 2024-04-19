package com.ymts0579.agritourism.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ymts0579.agritourism.R
import com.ymts0579.agritourism.RetrofitClient
import com.ymts0579.agritourism.farmer.framercropviewadapter
import com.ymts0579.agritourism.model.Crop
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Usercropadapter(var context: Context, var listdata: ArrayList<Crop>):
    RecyclerView.Adapter<Usercropadapter.DataViewHolder>(){
    var id=0
    class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvcname: TextView =view.findViewById(R.id.tvcname)
        var tvcfields: TextView =view.findViewById(R.id.tvcfields)
        var tvcstatus: TextView =view.findViewById(R.id.tvcstatus)
        var tvcdes: TextView =view.findViewById(R.id.tvcdes)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cardusercrops, parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
        var kk=listdata.get(position)
        holder.tvcname.text=kk.name
        holder.tvcfields.text=kk.field
        holder.tvcstatus.text=kk.status
        holder.tvcdes.text=kk.des

        holder.itemView.setOnClickListener {
            val ii=Intent(context,ViewMore::class.java)
            ii.putExtra("id",kk.cropid)
            context.startActivity(ii)
        }




    }




    override fun getItemCount() = listdata.size
}