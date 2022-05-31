package com.sora.gcdr.ui.share;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ShareViewModel extends ViewModel {
    private MutableLiveData<List<LCObject>> shareLiveData;
    private static int skip = 0;
    LCQuery<LCObject> query;
    public ShareViewModel() {
        this.shareLiveData =new MutableLiveData<>();
        shareLiveData.setValue(new ArrayList<>());
        query = new LCQuery<>("share");

        getMoreShare();
    }

    public MutableLiveData<List<LCObject>> getShareLiveData() {
        return shareLiveData;
    }

    public void getMoreShare() {


        query
//                .skip(skip)
//                .limit(5)
                .findInBackground()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LCObject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<LCObject> lcObjects) {
                        skip += 5;
//                        shareLiveData.getValue().addAll(lcObjects);
                        shareLiveData.setValue(lcObjects);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}