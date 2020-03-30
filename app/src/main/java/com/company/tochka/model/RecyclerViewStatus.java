package com.company.tochka.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class RecyclerViewStatus {

    private boolean isLoading;
    private boolean isLastPage;
    private boolean isSearch;

    private boolean isLoadingAdded;

    private String currentUserId;

    private String currentUserName;

    private int currentPageNumber, currentCount, currentTotalCount;

    private ArrayList<User> arrayList;

    public RecyclerViewStatus(boolean isLoading, boolean isLastPage, boolean isSearch, String currentUserId, String currentUserName, int currentPageNumber, int currentCount, int currentTotalCount, ArrayList<User> arrayList, boolean isLoadingAdded) {
        this.isLoading = isLoading;
        this.isLastPage = isLastPage;
        this.isSearch = isSearch;
        this.isLoadingAdded = isLoadingAdded;
        this.currentUserId = currentUserId;
        this.currentUserName = currentUserName;
        this.currentPageNumber = currentPageNumber;
        this.currentCount = currentCount;
        this.currentTotalCount = currentTotalCount;
        this.arrayList = arrayList;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public boolean isSearch() {
        return isSearch;
    }

    public void setSearch(boolean search) {
        isSearch = search;
    }

    public boolean isLoadingAdded() {
        return isLoadingAdded;
    }

    public void setLoadingAdded(boolean loadingAdded) {
        isLoadingAdded = loadingAdded;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void setCurrentPageNumber(int currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public int getCurrentTotalCount() {
        return currentTotalCount;
    }

    public void setCurrentTotalCount(int currentTotalCount) {
        this.currentTotalCount = currentTotalCount;
    }

    public ArrayList<User> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<User> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public String toString() {
        return currentUserName + " " + arrayList.toString();
    }
}
