package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtracker.api.ApiUtilities;
import com.example.covidtracker.api.countryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView Confirm,Active,Recovered,Death,Tests,countryName,updatedAt;
    private PieChart pieChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<countryData>  list = new ArrayList<>();

        countryName = findViewById(R.id.countryName);
        updatedAt = findViewById(R.id.updateAt);

        pieChart = findViewById(R.id.pieChart);

        Confirm = findViewById(R.id.totalConfirm);
        Active = findViewById(R.id.totalActive);
        Recovered = findViewById(R.id.totalRecovered);
        Death = findViewById(R.id.totalDeath);
        Tests = findViewById(R.id.totalTests);


        findViewById(R.id.countryName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CountryActivity.class);
                startActivity(intent);
            }
        });


        ApiUtilities.getApiInterface().getCountryData().enqueue(new Callback<List<countryData>>() {
            @Override
            public void onResponse(Call<List<countryData>> call, Response<List<countryData>> response) {

                list.addAll(response.body());

                for(int i=0;i<list.size();i++)
                {
                    if(list.get(i).getCountry().equals("India"))
                    {
                        int confirm=Integer.parseInt(list.get(i).getCases());
                        int active=Integer.parseInt(list.get(i).getActive());
                        int recovered=Integer.parseInt(list.get(i).getRecovered());
                        int death=Integer.parseInt(list.get(i).getDeaths());
                        int tests=Integer.parseInt(list.get(i).getTests());

                        Confirm.setText(NumberFormat.getInstance().format(confirm));
                        Active.setText(NumberFormat.getInstance().format(active));
                        Recovered.setText(NumberFormat.getInstance().format(recovered));
                        Death.setText(NumberFormat.getInstance().format(death));
                        Tests.setText(NumberFormat.getInstance().format(tests));

                       // updatedAt.setText(list.get(i).getUpdated());

                        setTextFun(list.get(i).getUpdated());


                        pieChart.addPieSlice(new PieModel("Confirm", confirm, Color.parseColor("#FBC02D")));
                        pieChart.addPieSlice(new PieModel("Active", active, Color.parseColor("#1976D2")));
                        pieChart.addPieSlice(new PieModel("Recovered", recovered, Color.parseColor("#689F38")));
                        pieChart.addPieSlice(new PieModel("Death", death, Color.parseColor("#F10404")));

                    }
                }


            }

            @Override
            public void onFailure(Call<List<countryData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setTextFun(String updated) {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");

        long miliSec = Long.parseLong(updated);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(miliSec);

        updatedAt.setText("-Updated At "+dateFormat.format(calendar.getTime()));


    }
}