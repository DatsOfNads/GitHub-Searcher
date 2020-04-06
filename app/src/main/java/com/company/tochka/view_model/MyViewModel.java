package com.company.tochka.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.company.tochka.model.FullUser;
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
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.internal.EverythingIsNonNull;

import static com.company.tochka.model.Exceptions.LOAD_FIRST_PAGE_EXCEPTION;
import static com.company.tochka.model.Exceptions.LOAD_NEXT_PAGE_EXCEPTION;
import static com.company.tochka.model.Exceptions.NO_EXCEPTIONS;
import static com.company.tochka.model.Exceptions.OPEN_FULL_USER_INFORMATION_EXCEPTION;
import static com.company.tochka.model.Exceptions.SEARCH_EXCEPTION;
import static com.company.tochka.model.Exceptions.SEARCH_NEXT_PAGE_EXCEPTION;

public class MyViewModel extends ViewModel {

    private String currentUserId;

    private String currentUserName;

    private String currentFullUserLogin;

    private int currentPageNumber, currentCount, currentTotalCount;

    private GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

    private MutableLiveData<ArrayList<User>> currentArrayList = new MutableLiveData<>();
    private MutableLiveData<RecyclerViewStatus> recyclerStatus = new MutableLiveData<>();
    private MutableLiveData<FullUser> fullUser = new MutableLiveData<>();
    private MutableLiveData<Integer> exceptions = new MutableLiveData<>();

    public MutableLiveData<ArrayList<User>> subscribeCurrentArrayList(){
        return currentArrayList;
    }

    public MutableLiveData<RecyclerViewStatus> subscribeCurrentRecyclerStatus(){
        return recyclerStatus;
    }

    public MutableLiveData<FullUser> subscribeFullUser(){
        return fullUser;
    }

    public MutableLiveData<Integer> subscribeOnExceptions(){
        return exceptions;
    }

    @EverythingIsNonNull
    public void loadFirstPage() {

        currentUserId = "0";

        Call<ArrayList<User>> call = service.getAllUsers(currentUserId);

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {

                ArrayList<User> arrayList = response.body();
                setIsSearch(false);

                if (arrayList != null) {
                    setIsLastPage(false);
                    setCurrentUserId(arrayList);
                    currentArrayList.postValue(arrayList);
                } else {
                    setIsLoading(false);
                    exceptions.setValue(LOAD_FIRST_PAGE_EXCEPTION);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                setIsLoading(false);
                exceptions.setValue(LOAD_FIRST_PAGE_EXCEPTION);
            }
        });
    }

    public void loadFirstPageAfterException(){
        exceptions.setValue(NO_EXCEPTIONS);
        loadFirstPage();
    }

    @EverythingIsNonNull
    public void loadNextPage() {

        Call<ArrayList<User>> call = service.getAllUsers(currentUserId);

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {

                ArrayList<User> arrayList = response.body();

                if (arrayList != null) {

                    updateArrayList(arrayList);

                    setIsLoading(false);

                    setCurrentUserId(arrayList);
                } else {
                    setIsLoading(false);
                    exceptions.setValue(LOAD_NEXT_PAGE_EXCEPTION);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                setIsLoading(false);
                exceptions.setValue(LOAD_NEXT_PAGE_EXCEPTION);
            }
        });
    }

    public void loadNextPageAfterException(){
        exceptions.setValue(NO_EXCEPTIONS);
        loadNextPage();
    }

    @EverythingIsNonNull
    public void search(String currentUserName) {

        this.currentPageNumber = 1;

        this.currentUserName = currentUserName;

        Call<ItemsList> call = service.getUsersWithPageParam(currentUserName, currentPageNumber);

        call.enqueue(new Callback<ItemsList>() {
            @Override
            public void onResponse(Call<ItemsList> call, Response<ItemsList> response) {

                ArrayList<User> arrayList;

                setIsSearch(true);

                if (response.body() != null) {

                    arrayList = response.body().getItems();

                    currentTotalCount = response.body().getTotalCount();

                    currentCount = arrayList.size();

                    currentArrayList.postValue(arrayList);

                    if (currentCount < currentTotalCount) {
                        currentPageNumber++;
                        setIsLastPage(false);
                    } else {
                        setIsLastPage(true);
                    }
                } else {
                    setIsLoading(false);
                    exceptions.setValue(SEARCH_EXCEPTION);
                }
            }

            @Override
            public void onFailure(Call<ItemsList> call, Throwable t) {
                setIsLoading(false);
                exceptions.setValue(SEARCH_EXCEPTION);
            }
        });
    }

    public void searchAfterException(){
        exceptions.setValue(NO_EXCEPTIONS);
        search(currentUserName);
    }

    @EverythingIsNonNull
    public void searchNextPage() {

        Call<ItemsList> call = service.getUsersWithPageParam(currentUserName, currentPageNumber);

        call.enqueue(new Callback<ItemsList>() {
            @Override
            public void onResponse(Call<ItemsList> call, Response<ItemsList> response) {

                ArrayList<User> arrayList;

                if (response.body() != null) {

                    arrayList = response.body().getItems();

                    currentCount += arrayList.size();

                    setIsLoading(false);

                    updateArrayList(arrayList);

                    if (currentCount < currentTotalCount) {
                        currentPageNumber++;
                    } else {
                        setIsLastPage(true);

                    }
                } else {
                    setIsLoading(false);
                    exceptions.setValue(SEARCH_NEXT_PAGE_EXCEPTION);
                }
            }

            @Override
            public void onFailure(Call<ItemsList> call, Throwable t) {
                setIsLoading(false);
                exceptions.setValue(SEARCH_NEXT_PAGE_EXCEPTION);
            }
        });
    }

    public void searchNextPageAfterException(){
        exceptions.setValue(NO_EXCEPTIONS);
        searchNextPage();
    }

    @EverythingIsNonNull
    public void searchFullUserInformation(String login){

        currentFullUserLogin = login;

        Call<FullUser> call = service.getFullUserInformation(login);

        call.enqueue(new Callback<FullUser>() {
            @Override
            public void onResponse(Call<FullUser> call, Response<FullUser> response) {

                if(response.body() != null)
                    fullUser.setValue(response.body());
                else
                    exceptions.setValue(OPEN_FULL_USER_INFORMATION_EXCEPTION);
            }

            @Override
            public void onFailure(Call<FullUser> call, Throwable t) {
                exceptions.setValue(OPEN_FULL_USER_INFORMATION_EXCEPTION);
            }
        });
    }

    public void searchFullUserInformationAfterException(){
        exceptions.setValue(NO_EXCEPTIONS);
        searchFullUserInformation(currentFullUserLogin);
    }

    private void setCurrentUserId(ArrayList<User> arrayList) {
        User user = arrayList.get(arrayList.size() - 1);
        currentUserId = user.getId();
    }

    public void setIsLoading(boolean isLoading){
        RecyclerViewStatus status = recyclerStatus.getValue();

        if(status == null){
            status = new RecyclerViewStatus();
        }
        Objects.requireNonNull(status).setLoading(isLoading);
        recyclerStatus.setValue(status);
    }

    private void setIsLastPage(boolean isLastPage){
        RecyclerViewStatus status = recyclerStatus.getValue();

        if(status == null){
            status = new RecyclerViewStatus();
        }

        Objects.requireNonNull(status).setLastPage(isLastPage);
        recyclerStatus.setValue(status);
    }

    private void setIsSearch(boolean isSearch){
        RecyclerViewStatus status = recyclerStatus.getValue();

        if(status == null){
            status = new RecyclerViewStatus();
        }

        Objects.requireNonNull(status).setSearch(isSearch);
        recyclerStatus.setValue(status);
    }

    private void updateArrayList(ArrayList<User> newArrayList){
        ArrayList<User> arrayList = currentArrayList.getValue();
        Objects.requireNonNull(arrayList).addAll(newArrayList);
        currentArrayList.postValue(arrayList);
    }

    public void getData(){
        if(currentArrayList.getValue() == null){
            loadFirstPage();
        }
    }

    public String getCurrentUserName(){
        return currentUserName;
    }

    private interface GetDataService {
        @GET("/users")
        Call<ArrayList<User>> getAllUsers(@Query("since") String id);

        @GET("search/users")
        Call<ItemsList> getUsersWithPageParam(@Query("q") String userName, @Query("page") long pageNum);

        @GET("/users/{login}")
        Call<FullUser> getFullUserInformation(@Path("login") String login);
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