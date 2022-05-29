package com.sora.gcdr.ui.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sora.gcdr.MyApplication;
import com.sora.gcdr.R;
import com.sora.gcdr.databinding.FragmentAddShareBinding;
import com.sora.gcdr.databinding.FragmentEasyAppBinding;

import cn.leancloud.LCObject;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddShareFragment extends Fragment {
    FragmentAddShareBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddShareBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.username.setText("test");
        binding.datetime.setText("ttime");
        binding.content.setText("");

        binding.buttonShare.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LCObject tShare = new LCObject("share");
                        tShare.put("user",binding.username.getText().toString());
                        tShare.put("content",binding.content.getText().toString());
                        tShare.saveInBackground()
                                .subscribe(new Observer<LCObject>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(LCObject lcObject) {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d("shiro", "onError: "+e.getMessage());
                                    }

                                    @Override
                                    public void onComplete() {
                                        Toast.makeText(MyApplication.getInstance(), "分享成功", Toast.LENGTH_LONG);
                                    }});
                        Navigation.findNavController(v).navigate(R.id.action_addShareFragment_to_easyAppFragment);
                    }
                }
        );



    }
}