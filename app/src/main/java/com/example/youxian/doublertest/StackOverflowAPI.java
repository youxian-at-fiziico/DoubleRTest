package com.example.youxian.doublertest;




import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by youxian on 12/7/15.
 */
public interface StackOverflowAPI {
    @GET("/2.2/questions?order=desc&sort=creation&site=stackoverflow")
    Observable<StackOverflowQuestions> loadQuestions(@Query("tagged") String tags);
}
