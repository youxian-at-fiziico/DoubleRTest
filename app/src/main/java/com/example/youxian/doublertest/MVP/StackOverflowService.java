package com.example.youxian.doublertest.MVP;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;


/**
 * Created by youxian on 12/31/15.
 */
public class StackOverflowService {
    private static final String BASE_URL = "https://api.stackexchange.com";
    private StackOverflowApi mStackOverflowApi;

    public StackOverflowService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mStackOverflowApi = retrofit.create(StackOverflowApi.class);
    }

    public StackOverflowApi getStackOverflowApi() {
        return mStackOverflowApi;
    }
}
