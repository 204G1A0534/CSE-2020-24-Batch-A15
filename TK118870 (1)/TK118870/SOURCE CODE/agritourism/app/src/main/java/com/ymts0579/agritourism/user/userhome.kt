package com.ymts0579.agritourism.user

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.agritourism.R
import com.ymts0579.agritourism.RetrofitClient
import com.ymts0579.agritourism.farmer.framercropviewadapter
import com.ymts0579.agritourism.model.Crop
import com.ymts0579.agritourism.model.CropinfoResponse
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class userhome : Fragment() {

    lateinit var  etsearch:EditText
    lateinit var  imgserach:ImageView
    lateinit var  listusercrops:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_userhome, container, false)
        etsearch=view.findViewById(R.id.etsearch)
        imgserach=view.findViewById(R.id.imgserach)
        listusercrops=view.findViewById(R.id.listusercrops)
        listusercrops.layoutManager = LinearLayoutManager(context)
        listusercrops.setHasFixedSize(true)



        imgserach.setOnClickListener {
            if(etsearch.text.toString()==""){
                Snackbar.make(it,"Enter place",Snackbar.LENGTH_SHORT).show()
                etsearch.setError("Enter place")
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.usercrops(etsearch.text.toString(),"cityuser")
                        .enqueue(object : Callback<CropinfoResponse> {
                            override fun onResponse(call: Call<CropinfoResponse>, response: Response<CropinfoResponse>) {

                                listusercrops.adapter= context?.let { Usercropadapter(it,response.body()!!.user) }


                            }

                            override fun onFailure(call: Call<CropinfoResponse>, t: Throwable) {
                                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()

                            }

                        })
                }
            }

        }


        return view
    }

}



