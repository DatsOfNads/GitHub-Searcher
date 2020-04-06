package com.company.tochka.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.MenuItem;

import com.company.tochka.model.FullUser;
import com.company.tochka.R;
import com.company.tochka.databinding.ActivityUserBinding;

import java.util.Objects;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ActivityUserBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user);

        binding.tool.setTitleTextColor(getColor(R.color.colorWhite));

        setSupportActionBar(binding.tool);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();

        String imageTransitionName = Objects.requireNonNull(extras).getString("sharedImageView");
        binding.imageView.setTransitionName(imageTransitionName);

        String textViewTransitionName = Objects.requireNonNull(extras).getString("sharedTextView");
        binding.textViewLogin.setTransitionName(textViewTransitionName);

        FullUser fullUser = extras.getParcelable("user");

        binding.setUser(fullUser);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}