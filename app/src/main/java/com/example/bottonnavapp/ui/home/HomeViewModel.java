package com.example.bottonnavapp.ui.home;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bottonnavapp.MainActivity;
import com.example.bottonnavapp.Modelo.Medicamento;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private ArrayList<Medicamento> listamedicamentos = new ArrayList<Medicamento>();

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }



}