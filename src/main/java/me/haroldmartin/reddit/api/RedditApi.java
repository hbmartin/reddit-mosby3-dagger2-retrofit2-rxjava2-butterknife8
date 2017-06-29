package me.haroldmartin.reddit.api;

import java.util.HashMap;

import io.reactivex.Observable;
import me.haroldmartin.reddit.api.model.reddit.Item;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RedditApi {
    private final RedditService api;
    private final ItemCache cache;

    public RedditApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.reddit.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(RedditService.class);

        cache = new ItemCache();
    }

    public Observable<Item> getItems(String after) {
        return api.getItems(after)
                .flatMap(top -> Observable.fromIterable(top.getData().getChildren()))
                .map(child -> {
                    Item data = child.getData();
                    cache.put(data.getId(), data);
                    return data;
                });
    }

    public Observable<Item> getItems() {
        return getItems(null);
    }

    public Observable<Item> getItem(String itemId) {
        return Observable.just(cache.get(itemId));
    }

    class ItemCache extends HashMap<String, Item> {
    }
}
