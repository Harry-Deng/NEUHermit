package com.sora.gcdr.ui.share;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sora.gcdr.MyApplication;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import io.reactivex.functions.Consumer;

public class ShareViewModel extends ViewModel {
    private MutableLiveData<List<LCObject>> shareLiveData;
    private static final int PAGE_SIZE = 5;
    private static int skip = 0;
    LCQuery<LCObject> query;

    public ShareViewModel() {
        this.shareLiveData = new MutableLiveData<>();
        shareLiveData.setValue(new ArrayList<>());
        query = new LCQuery<>("share");
    }

    public MutableLiveData<List<LCObject>> getShareLiveData() {
        return shareLiveData;
    }


    @SuppressLint("CheckResult")
    public void updateShare() {
        query.orderByDescending(LCObject.KEY_UPDATED_AT)
                .skip(0)
                .limit(PAGE_SIZE)
                .findInBackground()
                .subscribe(new Consumer<List<LCObject>>() {
                    @Override
                    public void accept(List<LCObject> lcObjects) throws Exception {
                        skip = 0;
                        if (lcObjects != null)
                            shareLiveData.setValue(lcObjects);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getMyShare() {
        query.orderByDescending(LCObject.KEY_UPDATED_AT)
                .whereEqualTo("username", MyApplication.getInstance().getUser().getUsername())
                .findInBackground()
                .subscribe(new Consumer<List<LCObject>>() {
                    @Override
                    public void accept(List<LCObject> lcObjects) throws Exception {
                        skip = 0;
                        if (lcObjects != null)
                            shareLiveData.setValue(lcObjects);
                    }
                });
    }


    @SuppressLint("CheckResult")
    public void getMoreShare() {
        query.orderByDescending(LCObject.KEY_UPDATED_AT)
                .skip(skip)
                .limit(PAGE_SIZE)
                .findInBackground()
                .subscribe(new Consumer<List<LCObject>>() {
                    @Override
                    public void accept(List<LCObject> lcObjects) throws Exception {
                        skip += PAGE_SIZE;
                        if (lcObjects != null)
                            shareLiveData.getValue().addAll(lcObjects);
                    }
                });
    }
}