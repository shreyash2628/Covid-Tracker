package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.covidtracker.api.ApiUtilities;
import com.example.covidtracker.api.countryAdapter;
import com.example.covidtracker.api.countryData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<countryData> list;
    private ProgressDialog dialog;
    private EditText searchBar;

    private countryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        searchBar = findViewById(R.id.searchBar);
        recyclerView = findViewById(R.id.countries);
        list = new ArrayList<>();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
         adapter = new countryAdapter(this,list);

        recyclerView.setAdapter(adapter);


        ApiUtilities.getApiInterface().getCountryData().enqueue(new Callback<List<countryData>>() {
            @Override
            public void onResponse(Call<List<countryData>> call, Response<List<countryData>> response) {

                list.addAll(response.body());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<countryData>> call, Throwable t) {
                Toast.makeText(CountryActivity.this, "Error"+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });


        dialog = new ProgressDialog(this);

        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void filter(String text) {
        List<countryData> filterList = new ArrayList<>();

        for(countryData item:list)
        {
            if(item.getCountry().toLowerCase().contains(text.toLowerCase()))
            {
                filterList.add(item);

            }
        }

            adapter.filterList(filterList);
    }
}