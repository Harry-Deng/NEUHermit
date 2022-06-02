package com.sora.gcdr.ui.me;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.SavedStateHandle;

public class MyInfoViewModel extends AndroidViewModel {
    SavedStateHandle handle;


    public MyInfoViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        this.handle = handle;
    }

}