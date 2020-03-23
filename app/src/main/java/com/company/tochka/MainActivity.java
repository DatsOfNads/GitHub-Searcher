package com.company.tochka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.RecyclerViewAdapterCallback {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    GetDataService service;

    private boolean isLoading = false;
    private boolean isLastPage = false;

    private boolean isSearch = false;

    private int currentUserId = 1;

    private String currentUserName;

    private int currentPageNumber, currentCount, currentTotalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        toolbar.setTitle(R.string.app_name);

        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);

        adapter = new RecyclerViewAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());//todo я не знаю что ета такое)))0)
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {

                System.err.println("Это конец");

                isLoading = true;

                if(!isSearch)
                    loadNextPage();
                else
                    searchNextPage();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }//todo а это походу не надо

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        //setRecyclerView();

        if(!isSearch)
        loadFirstPage();
    }

    private void setRecyclerView(){



}

    private void search(){

        System.err.println("Ищем " + currentUserName + currentPageNumber);

        Call<ItemModel> call = service.getUsersWithPageParam(currentUserName,currentPageNumber);

        call.enqueue(new Callback<ItemModel>() {
            @Override
            public void onResponse(Call<ItemModel> call, Response<ItemModel> response) {

                isSearch = true;

                ArrayList<UserModel> arrayList = response.body().getItems();

                currentTotalCount = response.body().getTotalCount();

                currentCount = arrayList.size();

                if(currentCount < currentTotalCount){
                    currentPageNumber++;
                } else {
                    isLastPage = true;
                }

                adapter.addAll(arrayList);
            }

            @Override
            public void onFailure(Call<ItemModel> call, Throwable t) {

            }
        });
    }

    private void searchNextPage(){

        System.err.println("Ищем " + currentUserName + currentPageNumber);

        Call<ItemModel> call = service.getUsersWithPageParam(currentUserName,currentPageNumber);

        call.enqueue(new Callback<ItemModel>() {
            @Override
            public void onResponse(Call<ItemModel> call, Response<ItemModel> response) {

                ArrayList<UserModel> arrayList = response.body().getItems();

                currentCount += arrayList.size();

                isLoading = false;

                if(currentCount < currentTotalCount){
                    currentPageNumber++;
                } else {
                    isLastPage = true;

                }

                adapter.addAll(arrayList);
            }

            @Override
            public void onFailure(Call<ItemModel> call, Throwable t) {

            }
        });
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

            }

            @Override
            public void onFailure(Call<ArrayList<UserModel>> call, Throwable t) {

            }
        });

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
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                adapter.removeAll();

                isLastPage = false;

                currentUserName = query;

                currentPageNumber = 1;

                setRecyclerView();

                search();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                if(isSearch){
                    isSearch = false;
                    isLastPage = false;
                    adapter.removeAll();
                    setRecyclerView();
                    loadFirstPage();
                }

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void getLastElementId(ArrayList<UserModel> arrayList){
        UserModel user = arrayList.get(arrayList.size() - 1);
        currentUserId = user.getId();
    }

    @Override
    public void retryPageLoad() {

    }

    private interface GetDataService {

        @GET("/users")
        Call<ArrayList<UserModel>> getAllUsers(@Query("since") long id);

        @GET("search/users")
        Call<ItemModel> getUsersWithPageParam(@Query("q") String userName, @Query("page") long pageNum);
    }
}