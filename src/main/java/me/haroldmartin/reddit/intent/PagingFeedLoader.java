package me.haroldmartin.reddit.intent;

import io.reactivex.Single;
import me.haroldmartin.reddit.api.RedditApi;
import me.haroldmartin.reddit.application.RedditApplication;
import me.haroldmartin.reddit.api.model.reddit.Item;
import timber.log.Timber;

import java.util.List;

import javax.inject.Inject;

public class PagingFeedLoader {

    @Inject RedditApi backend;

    private String currentPage;

    public PagingFeedLoader() {
        RedditApplication.getComponent().inject(this);
    }

    public Single<List<Item>> loadPage(String page) {
        return backend.getItems(page)
                .doOnNext(item -> currentPage = item.getName())
                .toList();
    }

    public Single<List<Item>> loadNewestPage() {
        return loadPage(null);
    }

    public Single<List<Item>> loadNextPage() {
        return loadPage(currentPage);
    }
}
