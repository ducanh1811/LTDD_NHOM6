package com.example.myapplication.api;

import com.example.myapplication.model.Product;
import com.example.myapplication.model.resObj;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    // url api: http://127.0.0.1:3000/bookstore/api/v1/

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();

    ApiService apiService = new Retrofit.Builder()
            //.baseUrl("http://192.168.2.13:3000/bookstore/api/v1/")
            .baseUrl("http://192.168.47.147:3000/bookstore/api/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    // Trang Home

    // hiển thị 8 sách mới nhất
    @GET("products/new/8")
    Call<resObj> getNewProduct();

    // hiển thị 8 sách bán chạy nhất
    @GET("products/bestseller/8")
    Call<resObj> getBestSellerProduct();

    // hiển thị 8 sách bán rẻ nhất
    @GET("products/sale/8")
    Call<resObj> getLowestProduct();

    @POST("users/")
    Call<resObj> getUserByEmail(@Query("email") String email);
}
