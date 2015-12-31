package com.example.youxian.doublertest.MVP;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by youxian on 12/31/15.
 */
public class StackOverflowPresenter {

    private StackOverflowActivity mView;
    private StackOverflowService mModel;

    public StackOverflowPresenter(StackOverflowActivity view) {
        mView = view;
        mModel = new StackOverflowService();
    }

    public void fetchData() {
        mModel.getStackOverflowApi()
                .fetchNewQuestions("android")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StackOverflowData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(StackOverflowData stackOverflowData) {
                        mView.displayData(stackOverflowData);
                    }
                });
    }
}
