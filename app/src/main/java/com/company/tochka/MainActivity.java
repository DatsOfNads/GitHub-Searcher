package com.company.tochka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.RecyclerViewAdapterCallback {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private ArrayList<UserModel> arrayList;

    GetDataService service;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentUserId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        adapter = new RecyclerViewAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());//todo я не знаю что ета такое)))0)
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                System.err.println("Это конец");
                loadNextPage();

                //adapter.removeLoadingFooter();

            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        loadFirstPage();
    }

    private void loadFirstPage() {

        currentUserId = 0;

        Call<ArrayList<UserModel>> call = service.getAllUsers(currentUserId);

        call.enqueue(new Callback<ArrayList<UserModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserModel>> call, Response<ArrayList<UserModel>> response) {

                ArrayList<UserModel> arrayList = response.body();
                adapter.addAll(arrayList);

                adapter.addLoadingFooter();
                getLastElementId(arrayList);

//                if (currentPage <= 10) adapter.addLoadingFooter();
//                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<ArrayList<UserModel>> call, Throwable t) {

            }
        });

    }

    private void getLastElementId(ArrayList<UserModel> arrayList){
        UserModel user = arrayList.get(arrayList.size() - 1);
        currentUserId = user.getId();
    }

    private void loadNextPage(){

        Call<ArrayList<UserModel>> call = service.getAllUsers(currentUserId);

        call.enqueue(new Callback<ArrayList<UserModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserModel>> call, Response<ArrayList<UserModel>> response) {
                adapter.removeLoadingFooter();

                ArrayList<UserModel> arrayList = response.body();
                adapter.addAll(arrayList);

                isLoading = false;

                getLastElementId(arrayList);

                adapter.addLoadingFooter();
            }

            @Override
            public void onFailure(Call<ArrayList<UserModel>> call, Throwable t) {

            }
        });
    }

    @Override
    public void retryPageLoad() {

    }

    private interface GetDataService {

        @GET("/users")
        Call<ArrayList<UserModel>> getAllUsers(@Query("since") long id);

        @GET("search/users")
        Call<ArrayList<UserModel>> getUsersWithPageParam(@Query("q") String user, @Query("page") long pageNumber);
    }
}