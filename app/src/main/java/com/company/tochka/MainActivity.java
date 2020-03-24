package com.company.tochka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    private ProgressBar progressBar;

    GetDataService service;

    private boolean isLoading = false;
    private boolean isLastPage = false;

    private boolean isSearch = false;

    private String currentUserId = "1";

    private String currentUserName;

    private int currentPageNumber, currentCount, currentTotalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        Toolbar toolbar = findViewById(R.id.tool);
        toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        toolbar.setTitle(R.string.app_name);

        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);

        progressBar = findViewById(R.id.progressBar);

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
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        if(!isSearch)
        loadFirstPage();
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

                progressBar.setVisibility(View.VISIBLE);

                adapter.removeAll();

                isLastPage = false;

                currentUserName = query;

                currentPageNumber = 1;

                search();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {

            if(isSearch){

                progressBar.setVisibility(View.VISIBLE);

                isSearch = false;
                isLastPage = false;
                adapter.removeAll();
                loadFirstPage();
            }

            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void search(){

        System.err.println("Ищем " + currentUserName + currentPageNumber);

        Call<ItemModel> call = service.getUsersWithPageParam(currentUserName,currentPageNumber);

        call.enqueue(new Callback<ItemModel>() {
            @Override
            public void onResponse(Call<ItemModel> call, Response<ItemModel> response) {

                ArrayList<UserModel> arrayList = null;

                if (response.body() != null) {
                    arrayList = response.body().getItems();

                    progressBar.setVisibility(View.INVISIBLE);

                    isSearch = true;

                    currentTotalCount = response.body().getTotalCount();

                    if(currentTotalCount == 0){
                        Toast.makeText(MainActivity.this, R.string.nothing_was_found_for_your_search, Toast.LENGTH_SHORT).show();
                    }

                    currentCount = arrayList.size();

                    adapter.addAll(arrayList);

                    if(currentCount < currentTotalCount){
                        currentPageNumber++;
                        adapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<ItemModel> call, Throwable t) {

                final CustomAlertDialog customAlertDialogInfo = new CustomAlertDialog(MainActivity.this,
                        R.string.alert_dialog_error);

                customAlertDialogInfo.setTitle("Что-то пошло не так(");
                customAlertDialogInfo.setMessage("Скорее всего это не наша вина. Попробуйте ещё раз.");
                customAlertDialogInfo.show();

                customAlertDialogInfo.setButtonClickListener(v -> {

                    search();
                    customAlertDialogInfo.dismiss();
                });
            }
        });
    }

    private void searchNextPage(){

        Call<ItemModel> call = service.getUsersWithPageParam(currentUserName, currentPageNumber);

        call.enqueue(new Callback<ItemModel>() {
            @Override
            public void onResponse(Call<ItemModel> call, Response<ItemModel> response) {

                ArrayList<UserModel> arrayList;

                if (response.body() != null) {

                    adapter.removeLoadingFooter();

                    arrayList = response.body().getItems();

                    currentCount += arrayList.size();

                    isLoading = false;

                    adapter.addAll(arrayList);

                    if(currentCount < currentTotalCount){
                        currentPageNumber++;
                        adapter.addLoadingFooter();
                    } else {
                        adapter.removeLoadingFooter();
                        isLastPage = true;

                    }
                }

            }

            @Override
            public void onFailure(Call<ItemModel> call, Throwable t) {
                final CustomAlertDialog customAlertDialogInfo = new CustomAlertDialog(MainActivity.this,
                        R.string.alert_dialog_error);

                customAlertDialogInfo.setTitle("Что-то пошло не так(");
                customAlertDialogInfo.setMessage("Скорее всего это не наша вина. Попробуйте ещё раз.");
                customAlertDialogInfo.show();

                customAlertDialogInfo.setButtonClickListener(v -> {

                    searchNextPage();
                    customAlertDialogInfo.dismiss();
                });
            }
        });
    }


    private void loadFirstPage() {

        currentUserId = "0";

        Call<ArrayList<UserModel>> call = service.getAllUsers(currentUserId);

        call.enqueue(new Callback<ArrayList<UserModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserModel>> call, Response<ArrayList<UserModel>> response) {
                progressBar.setVisibility(View.INVISIBLE);

                ArrayList<UserModel> arrayList = response.body();

                if (arrayList != null) {

                    adapter.addAll(arrayList);

                    adapter.addLoadingFooter();
                    setLastElementId(arrayList);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserModel>> call, Throwable t) {

                final CustomAlertDialog customAlertDialogInfo = new CustomAlertDialog(MainActivity.this,
                        R.string.alert_dialog_error);

                customAlertDialogInfo.setTitle("Что-то пошло не так(");
                customAlertDialogInfo.setMessage("Скорее всего это не наша вина. Попробуйте ещё раз.");
                customAlertDialogInfo.show();

                customAlertDialogInfo.setButtonClickListener(v -> {

                    loadFirstPage();
                    customAlertDialogInfo.dismiss();
                });
            }
        });

    }

    private void loadNextPage(){

        Call<ArrayList<UserModel>> call = service.getAllUsers(currentUserId);

        call.enqueue(new Callback<ArrayList<UserModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserModel>> call, Response<ArrayList<UserModel>> response) {

                ArrayList<UserModel> arrayList = response.body();

                if (arrayList != null) {

                    adapter.removeLoadingFooter();
                    adapter.addAll(arrayList);

                    isLoading = false;

                    setLastElementId(arrayList);

                    adapter.addLoadingFooter();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserModel>> call, Throwable t) {

                final CustomAlertDialog customAlertDialogInfo = new CustomAlertDialog(MainActivity.this,
                        R.string.alert_dialog_error);

                customAlertDialogInfo.setTitle("Что-то пошло не так(");
                customAlertDialogInfo.setMessage("Скорее всего это не наша вина. Попробуйте ещё раз.");
                customAlertDialogInfo.show();

                customAlertDialogInfo.setButtonClickListener(v -> {

                    loadNextPage();
                    customAlertDialogInfo.dismiss();
                });
            }
        });
    }

    private void setLastElementId(ArrayList<UserModel> arrayList){
        UserModel user = arrayList.get(arrayList.size() - 1);
        currentUserId = user.getId();
    }

    @Override
    public void openFullUserInformation(String login) {
        Intent intent = new Intent(MainActivity.this, UserActivity.class);
        intent.putExtra("extra_login", login);
        startActivity(intent);
    }

    private interface GetDataService {

        @GET("/users")
        Call<ArrayList<UserModel>> getAllUsers(@Query("since") String id);

        @GET("search/users")
        Call<ItemModel> getUsersWithPageParam(@Query("q") String userName, @Query("page") long pageNum);
    }
}