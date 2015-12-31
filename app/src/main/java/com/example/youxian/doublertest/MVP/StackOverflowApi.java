package com.example.youxian.doublertest.MVP;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;


/**
 * Created by youxian on 12/31/15.
 */
public interface StackOverflowApi {
    @GET("/2.2/questions?order=desc&sort=creation&site=stackoverflow")
    Observable<StackOverflowData> fetchNewQuestions(@Query("tagged") String tags);
}
