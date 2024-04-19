package com.ymts0579.agritourism.farmer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.ymts0579.agritourism.R


class Farmerhome : Fragment() {

    lateinit var  btnaddcrops:Button
    lateinit var  btnfviewcrops:Button
    lateinit var  btnfrequest:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_farmerhome, container, false)
        btnaddcrops=view.findViewById(R.id.btnaddcrops)
        btnfviewcrops=view.findViewById(R.id.btnfviewcrops)
        btnfrequest=view.findViewById(R.id.btnfrequest)


        btnaddcrops.setOnClickListener { startActivity(Intent(context,AddCrops::class.java)) }
        btnfviewcrops.setOnClickListener { startActivity(Intent(context,Farmerviewcrops::class.java)) }
        btnfrequest.setOnClickListener { startActivity(Intent(context,farmerbooking::class.java)) }
        return view
    }


}