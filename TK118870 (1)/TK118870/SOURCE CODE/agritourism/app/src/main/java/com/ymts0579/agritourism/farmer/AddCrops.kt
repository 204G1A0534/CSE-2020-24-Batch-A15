package com.ymts0579.agritourism.farmer

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.agritourism.R
import com.ymts0579.agritourism.RetrofitClient
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class AddCrops : AppCompatActivity() {
    lateinit var etcropname:EditText
    lateinit var etcropfields:EditText
    lateinit var etcropdes:EditText
    lateinit var img1:ImageView
    lateinit var img2:ImageView
    lateinit var btnaddcrop:Button
    var encode=""
    var encoded=""
    var email=""
    var city=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_crops)
        etcropname=findViewById(R.id.etcropname)
        etcropfields=findViewById(R.id.etcropfields)
        etcropdes=findViewById(R.id.etcropdes)
        img1=findViewById(R.id.img1)
        img2=findViewById(R.id.img2)
        btnaddcrop=findViewById(R.id.btnaddcrop)


        getSharedPreferences("user", MODE_PRIVATE).apply {

            email= getString("email","").toString()
            city=getString("city","").toString()


        }



        img1.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent,90)
        }


        img2.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent,100)
        }



        btnaddcrop.setOnClickListener {
            val name=etcropname.text.toString()
            val field=etcropfields.text.toString()
            val des=etcropdes.text.toString()
            val cropid="crop${(1000..9999).shuffled().last()}"

            if(name.isEmpty() && field.isEmpty() && des.isEmpty()&& cropid.isEmpty()&&
                encode==""&&
                encoded==""){
                Snackbar.make(it,"Enter the required fields",Snackbar.LENGTH_SHORT).show()
                etcropdes.setError("Enter Crop description")
                etcropfields.setError("Enter Crop Fields ")
                etcropname.setError("Enter Crop Name")
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.addcrops("$name","$des","$field","Pending","$email","$city",
                        "$cropid","$encode","$encoded","addcrop")
                        .enqueue(object: Callback<DefaultResponse> {
                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                t.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show() }
                            }
                            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                response.body()?.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show()}
                                etcropdes.text.clear()
                                etcropfields.text.clear()
                                etcropname.text.clear()

                                Handler().postDelayed({
                                     startActivity(Intent(this@AddCrops,Farmerviewcrops::class.java))
                                },1000)
                            }
                        })
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 90 && resultCode == RESULT_OK) {
            val tt1 = data?.data.toString()
            val uri = Uri.parse(tt1)
            img1.setImageURI(uri)
            val bit= MediaStore.Images.Media.getBitmap(contentResolver,uri)
            val byte = ByteArrayOutputStream()
            bit.compress(Bitmap.CompressFormat.JPEG, 100, byte)
            val image = byte.toByteArray()
            encode = android.util.Base64.encodeToString(image, Base64.DEFAULT)


        }else if(requestCode == 100 && resultCode == RESULT_OK){
            val tt1 = data?.data.toString()
            val uri = Uri.parse(tt1)
            img2.setImageURI(uri)
            val bit= MediaStore.Images.Media.getBitmap(contentResolver,uri)
            val byte = ByteArrayOutputStream()
            bit.compress(Bitmap.CompressFormat.JPEG, 100, byte)
            val image = byte.toByteArray()
            encoded = android.util.Base64.encodeToString(image, Base64.DEFAULT)

        }
    }
}