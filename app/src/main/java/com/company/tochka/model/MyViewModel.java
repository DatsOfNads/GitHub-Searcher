package com.company.tochka.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {

    private MutableLiveData<RecyclerViewStatus> currentStatus = new MutableLiveData<>();

    public RecyclerViewStatus getCurrentStatus() {
        return currentStatus.getValue();
    }

    public void setCurrentStatus(RecyclerViewStatus status) {
       currentStatus.setValue(status);
    }
}