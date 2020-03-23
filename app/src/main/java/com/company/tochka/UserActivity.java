package com.company.tochka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.company.tochka.databinding.ActivityUserBinding;


import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class UserActivity extends AppCompatActivity {

    GetDataService service;
    ActivityUserBinding binding;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        toolbar = findViewById(R.id.tool);

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);

        binding.tool.setTitleTextColor(getColor(R.color.colorWhite));

        setSupportActionBar(binding.tool);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        String login = intent.getStringExtra("extra_login");

        Call<FullUserModel> call = service.getAllUsers(login);

        call.enqueue(new Callback<FullUserModel>() {
            @Override
            public void onResponse(Call<FullUserModel> call, Response<FullUserModel> response) {
               setView(response.body());
            }

            @Override
            public void onFailure(Call<FullUserModel> call, Throwable t) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void setView(FullUserModel user){

        binding.setUser(user);
    }

    private interface GetDataService {

        @GET("/users/{login}")
        Call<FullUserModel> getAllUsers(@Path("login") String login);
    }
}