package com.company.tochka.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.company.tochka.model.CustomAlertDialog;
import com.company.tochka.model.MyViewModel;
import com.company.tochka.model.PaginationScrollListener;
import com.company.tochka.R;
import com.company.tochka.model.RecyclerViewAdapter;
import com.company.tochka.model.RecyclerViewStatus;
import com.company.tochka.model.RetrofitClientInstance;
import com.company.tochka.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.internal.EverythingIsNonNull;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.RecyclerViewAdapterCallback {

    private RecyclerViewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;

    GetDataService service;

    MyViewModel model;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private boolean isSearch = false;

    private String currentUserId;

    private String currentUserName;

    private int currentPageNumber, currentCount, currentTotalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        model = new ViewModelProvider(this).get(MyViewModel.class);

        adapter = new RecyclerViewAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        progressBar = findViewById(R.id.progressBar);

        Toolbar toolbar = findViewById(R.id.tool);
        toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        RecyclerViewStatus recyclerViewStatus = model.getCurrentStatus();

        if(recyclerViewStatus != null){
            progressBar.setVisibility(View.INVISIBLE);

            isLoading = recyclerViewStatus.isLoading();
            isLastPage = recyclerViewStatus.isLastPage();
            isSearch = recyclerViewStatus.isSearch();

            currentUserId = recyclerViewStatus.getCurrentUserId();

            currentUserName = recyclerViewStatus.getCurrentUserName();

            currentPageNumber = recyclerViewStatus.getCurrentPageNumber();
            currentCount = recyclerViewStatus.getCurrentCount();
            currentTotalCount = recyclerViewStatus.getCurrentTotalCount();

            ArrayList<User> arrayList = recyclerViewStatus.getArrayList();

            boolean isLoadedAdded = recyclerViewStatus.isLoadingAdded();

            adapter.setLoadingAdded(isLoadedAdded);
            adapter.addAll(arrayList);

        } else {
            loadFirstPage();
        }

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {

                isLoading = true;

                if (!isSearch)
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(getComponentName()));

        RecyclerViewStatus recyclerViewStatus = model.getCurrentStatus();

        if(isSearch){
            searchView.onActionViewExpanded();
            searchView.setQuery(recyclerViewStatus.getCurrentUserName(), true);
            searchView.clearFocus();
        }

        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);

        closeButton.setOnClickListener(v -> {

            if (isSearch) {

                progressBar.setVisibility(View.VISIBLE);

                isSearch = false;
                isLastPage = false;
                adapter.removeAll();
                loadFirstPage();
            }

            searchView.setQuery("", false);
            searchView.onActionViewCollapsed();
            searchItem.collapseActionView();
        });

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

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {

        RecyclerViewStatus status = new RecyclerViewStatus(isLoading,
                isLastPage,
                isSearch,currentUserId,
                currentUserName,
                currentPageNumber,
                currentCount,
                currentTotalCount,
                adapter.getArrayList(),
                adapter.isLoadingAdded());

        model.setCurrentStatus(status);
        super.onPause();
    }

    @EverythingIsNonNull
    private void loadFirstPage() {

        currentUserId = "0";

        Call<ArrayList<User>> call = service.getAllUsers(currentUserId);

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                progressBar.setVisibility(View.INVISIBLE);

                ArrayList<User> arrayList = response.body();

                if (arrayList != null) {

                    adapter.addAll(arrayList);

                    adapter.addLoadingFooter();
                    setCurrentUserId(arrayList);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

                final CustomAlertDialog customAlertDialogInfo = new CustomAlertDialog(MainActivity.this,
                        R.string.alert_dialog_error);

                customAlertDialogInfo.setTitle(R.string.something_went_wrong);
                customAlertDialogInfo.setMessage(R.string.please_try_again);
                customAlertDialogInfo.show();

                customAlertDialogInfo.setButtonClickListener(v -> {

                    loadFirstPage();
                    customAlertDialogInfo.dismiss();
                });
            }
        });
    }

    @EverythingIsNonNull
    private void loadNextPage() {

        Call<ArrayList<User>> call = service.getAllUsers(currentUserId);

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {

                ArrayList<User> arrayList = response.body();

                if (arrayList != null) {

                    adapter.removeLoadingFooter();
                    adapter.addAll(arrayList);

                    isLoading = false;

                    setCurrentUserId(arrayList);

                    adapter.addLoadingFooter();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

                final CustomAlertDialog customAlertDialogInfo = new CustomAlertDialog(MainActivity.this,
                        R.string.alert_dialog_error);

                customAlertDialogInfo.setTitle(R.string.something_went_wrong);
                customAlertDialogInfo.setMessage(R.string.please_try_again);
                customAlertDialogInfo.show();

                customAlertDialogInfo.setButtonClickListener(v -> {

                    loadNextPage();
                    customAlertDialogInfo.dismiss();
                });
            }
        });
    }

    @EverythingIsNonNull
    private void search() {

        Call<ItemsList> call = service.getUsersWithPageParam(currentUserName, currentPageNumber);

        call.enqueue(new Callback<ItemsList>() {
            @Override
            public void onResponse(Call<ItemsList> call, Response<ItemsList> response) {

                ArrayList<User> arrayList;

                if (response.body() != null) {

                    arrayList = response.body().getItems();

                    progressBar.setVisibility(View.INVISIBLE);

                    isSearch = true;

                    currentTotalCount = response.body().getTotalCount();

                    if (currentTotalCount == 0) {
                        Toast.makeText(MainActivity.this, R.string.nothing_was_found_for_your_search, Toast.LENGTH_SHORT).show();
                    }

                    currentCount = arrayList.size();

                    adapter.addAll(arrayList);

                    if (currentCount < currentTotalCount) {
                        currentPageNumber++;
                        adapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<ItemsList> call, Throwable t) {

                final CustomAlertDialog customAlertDialogInfo = new CustomAlertDialog(MainActivity.this,
                        R.string.alert_dialog_error);

                customAlertDialogInfo.setTitle(R.string.something_went_wrong);
                customAlertDialogInfo.setMessage(R.string.please_try_again);
                customAlertDialogInfo.show();

                customAlertDialogInfo.setButtonClickListener(v -> {

                    search();
                    customAlertDialogInfo.dismiss();
                });
            }
        });
    }

    @EverythingIsNonNull
    private void searchNextPage() {

        Call<ItemsList> call = service.getUsersWithPageParam(currentUserName, currentPageNumber);

        call.enqueue(new Callback<ItemsList>() {
            @Override
            public void onResponse(Call<ItemsList> call, Response<ItemsList> response) {

                ArrayList<User> arrayList;

                if (response.body() != null) {

                    adapter.removeLoadingFooter();

                    arrayList = response.body().getItems();

                    currentCount += arrayList.size();

                    isLoading = false;

                    adapter.addAll(arrayList);

                    if (currentCount < currentTotalCount) {
                        currentPageNumber++;
                        adapter.addLoadingFooter();
                    } else {
                        adapter.removeLoadingFooter();
                        isLastPage = true;

                    }
                }
            }

            @Override
            public void onFailure(Call<ItemsList> call, Throwable t) {
                final CustomAlertDialog customAlertDialogInfo = new CustomAlertDialog(MainActivity.this,
                        R.string.alert_dialog_error);

                customAlertDialogInfo.setTitle(R.string.something_went_wrong);
                customAlertDialogInfo.setMessage(R.string.please_try_again);
                customAlertDialogInfo.show();

                customAlertDialogInfo.setButtonClickListener(v -> {

                    searchNextPage();
                    customAlertDialogInfo.dismiss();
                });
            }
        });
    }

    private void setCurrentUserId(ArrayList<User> arrayList) {
        User user = arrayList.get(arrayList.size() - 1);
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
        Call<ArrayList<User>> getAllUsers(@Query("since") String id);

        @GET("search/users")
        Call<ItemsList> getUsersWithPageParam(@Query("q") String userName, @Query("page") long pageNum);
    }

    private static class ItemsList {

        @SerializedName("items")
        @Expose
        private ArrayList<User> items;

        @SerializedName("total_count")
        private Integer totalCount;

        public ItemsList(ArrayList<User> items, Integer totalCount) {
            this.items = items;
            this.totalCount = totalCount;
        }

        ArrayList<User> getItems() {
            return items;
        }

        Integer getTotalCount() {
            return totalCount;
        }
    }
}