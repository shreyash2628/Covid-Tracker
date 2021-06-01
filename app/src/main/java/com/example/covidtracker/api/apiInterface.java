package com.example.covidtracker.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface apiInterface {


    static final String BASE_URL="https://corona.lmao.ninja/v2/";

    @GET("countries")
    Call<List<countryData>> getCountryData();


}
