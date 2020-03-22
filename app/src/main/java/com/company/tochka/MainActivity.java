package com.company.tochka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        Call<ArrayList<UserModel>> call = service.getAllUsers(30,4);

        call.enqueue(new Callback<ArrayList<UserModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserModel>> call, Response<ArrayList<UserModel>> response) {
                System.err.println("Погнали " + response.body());
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<UserModel>> call, Throwable t) {

            }
        });

    }

    private void generateDataList(ArrayList<UserModel> arrayList) {
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(getApplicationContext(),arrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private interface GetDataService {

        @GET("/users")
        Call<ArrayList<UserModel>> getAllUsers(@Query("per_page") long perPage, @Query("p") long pageNumber);

        @GET("search/users")
        Call<ArrayList<UserModel>> getUsersWithPageParam(@Query("q") String user, @Query("page") long pageNumber);
    }
}