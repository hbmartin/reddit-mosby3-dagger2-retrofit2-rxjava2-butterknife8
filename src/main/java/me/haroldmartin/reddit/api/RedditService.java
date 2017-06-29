package me.haroldmartin.reddit.api;

import io.reactivex.Observable;
import me.haroldmartin.reddit.api.model.reddit.Top;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RedditService {

  @GET("/top.json")
  Observable<Top> getItems(@Query("after") String after);
}
