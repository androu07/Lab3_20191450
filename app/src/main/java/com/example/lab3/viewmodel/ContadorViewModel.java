package com.example.lab3.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContadorViewModel extends ViewModel {

    private final MutableLiveData<String> contador = new MutableLiveData<>();

    public MutableLiveData<String> getContador() {
        return contador;
    }
}
