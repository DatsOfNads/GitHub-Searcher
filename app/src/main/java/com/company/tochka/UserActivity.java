package com.company.tochka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import com.company.tochka.databinding.ActivityUserBinding;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class UserActivity extends AppCompatActivity {

    GetDataService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

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

    private void setView(FullUserModel user){
        ActivityUserBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        binding.setUser(user);
    }

    private interface GetDataService {

        @GET("/users/{login}")
        Call<FullUserModel> getAllUsers(@Path("login") String login);

    }
}