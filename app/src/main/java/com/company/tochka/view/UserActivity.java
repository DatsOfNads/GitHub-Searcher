package com.company.tochka.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.company.tochka.model.FullUser;
import com.company.tochka.R;
import com.company.tochka.model.RetrofitClientInstance;
import com.company.tochka.databinding.ActivityUserBinding;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.internal.EverythingIsNonNull;

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

        loadUserInfo(Objects.requireNonNull(login));

    }

    @EverythingIsNonNull
    private void loadUserInfo(String login){

        Call<FullUser> call = service.getAllUsers(login);

        call.enqueue(new Callback<FullUser>() {
            @Override
            public void onResponse(Call<FullUser> call, Response<FullUser> response) {
                setView(response.body());
            }

            @Override
            public void onFailure(Call<FullUser> call, Throwable t) {

                final CustomAlertDialog customAlertDialogInfo = new CustomAlertDialog(UserActivity.this,
                        R.string.alert_dialog_error);

                customAlertDialogInfo.setTitle(R.string.something_went_wrong);
                customAlertDialogInfo.setMessage(R.string.please_try_again);
                customAlertDialogInfo.show();

                customAlertDialogInfo.setButtonClickListener(v -> {
                    loadUserInfo(login);
                    customAlertDialogInfo.dismiss();
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void setView(FullUser user){

        binding.setUser(user);
    }

    private interface GetDataService {

        @GET("/users/{login}")
        Call<FullUser> getAllUsers(@Path("login") String login);
    }
}