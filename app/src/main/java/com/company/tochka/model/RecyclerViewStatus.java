package com.company.tochka.model;

public class RecyclerViewStatus {

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private boolean isSearch = false;

    public boolean isLoading() {
        return isLoading;
    }

    void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public boolean isSearch() {
        return isSearch;
    }

    public void setSearch(boolean search) {
        isSearch = search;
    }
}