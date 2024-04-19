package com.ymts0579.agritourism


import com.ymts0579.agritourism.model.BookingResponse
import com.ymts0579.agritourism.model.CropinfoResponse
import com.ymts0579.fooddonationapp.model.Userresponse
import com.ymts0579.model.model.DefaultResponse
import com.ymts0579.model.model.LoginResponse

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {
    @FormUrlEncoded
    @POST("users.php")
    fun register(
        @Field("name") name:String,
        @Field("mobile")mobile:String,
        @Field("email")email :String,
        @Field("city") city:String,
        @Field("password") password:String,
        @Field("address")address :String,
        @Field("type") type:String,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("users.php")
    fun login(@Field("email") email:String, @Field("password") password:String,
              @Field("condition") condition:String): Call<LoginResponse>

    @FormUrlEncoded
    @POST("users.php")
    fun users(@Field("type") type:String,@Field("condition") condition:String): Call<Userresponse>

    @FormUrlEncoded
    @POST("users.php")
    fun updateusers(
        @Field("name") name:String, @Field("mobile")moblie:String, @Field("password") password:String,
        @Field("address")address :String, @Field("city") city:String,
        @Field("id")id:Int, @Field("condition")condition:String): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("users.php")
    fun usersemail(@Field("email") email:String,@Field("condition") condition:String): Call<Userresponse>


    @FormUrlEncoded
    @POST("crops.php")
    fun addcrops(
        @Field("name") name:String,
        @Field("des") des:String,
        @Field("field") field :String,
        @Field("status") status:String,
        @Field("email") email:String,
        @Field("city") city :String,
        @Field("cropid") cropid:String,
        @Field("path") path:String,
        @Field("path2") path2:String,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>



    @FormUrlEncoded
    @POST("crops.php")
    fun statuscrops(
        @Field("status") status:String,
        @Field("id") id: Int,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>


    @FormUrlEncoded
    @POST("crops.php")
    fun framercrops(
        @Field("email") email:String,
        @Field("condition") condition:String,
    ): Call<CropinfoResponse>


    @FormUrlEncoded
    @POST("crops.php")
    fun usercrops(
        @Field("city") city :String,
        @Field("condition") condition:String,
    ): Call<CropinfoResponse>


    @FormUrlEncoded
    @POST("crops.php")
    fun cropdetails(
        @Field("cropid") cropid:String,
        @Field("condition") condition:String,
    ): Call<CropinfoResponse>

    @FormUrlEncoded
    @POST("Booking.php")
    fun addBooing(
        @Field("fname") fname:String,
        @Field("femail") femail:String,
        @Field("fnumber") fnumber :String,
        @Field("status") status:String,
        @Field("uname") uname:String,
        @Field("umob") umob :String,
        @Field("uemail") uemail :String,
        @Field("Bookid") Bookid:String,
        @Field("date") date:String,
        @Field("days") days:String,
        @Field("persons") persons:String,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>



    @FormUrlEncoded
    @POST("Booking.php")
    fun userBooing(

        @Field("uemail") uemail :String,
        @Field("condition") condition:String,
    ): Call<BookingResponse>


    @FormUrlEncoded
    @POST("Booking.php")
    fun farmerBooing(

        @Field("femail") femail :String,
        @Field("condition") condition:String,
    ): Call<BookingResponse>


    @FormUrlEncoded
    @POST("Booking.php")
    fun statusBooing(

        @Field("status") status:String,
        @Field("id")  id: Int,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>




}