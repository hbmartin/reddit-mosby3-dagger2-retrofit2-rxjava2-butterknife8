package me.haroldmartin.reddit.intent.home;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import me.haroldmartin.reddit.api.model.reddit.Item;
import me.haroldmartin.reddit.application.RedditApplication;
import me.haroldmartin.reddit.intent.FeedItem;
import me.haroldmartin.reddit.intent.PagingFeedLoader;
import timber.log.Timber;

public class HomeInteractor {
    @Inject PagingFeedLoader feedLoader;

    public HomeInteractor() {
        RedditApplication.getComponent().inject(this);
    }

    public Observable<PartialStateChanges> loadFirstPage(Boolean ignored) {
        return feedLoader.loadNewestPage()
                .map(HomeInteractor::getFirstPageLoadedFromItems)
                .toObservable()
                .startWith(new PartialStateChanges.FirstPageLoading())
                .onErrorReturn(PartialStateChanges.FirstPageError::new)
                .subscribeOn(Schedulers.io());
    }

    public Observable<PartialStateChanges> loadNextPage(Boolean ignored) {
        return feedLoader.loadNextPage()
                .map(HomeInteractor::getNextPageLoadedFromItems)
                .toObservable()
                .startWith(new PartialStateChanges.NextPageLoading())
                .onErrorReturn(PartialStateChanges.NexPageLoadingError::new)
                .subscribeOn(Schedulers.io());
    }

    public Observable<PartialStateChanges> loadRefresh(Boolean ignored) {
        Timber.e("loadRefresh");
        return feedLoader.loadNewestPage()
                .subscribeOn(Schedulers.io())
                .map(HomeInteractor::getRefreshLoadedFromItems)
                .toObservable()
                .startWith(new PartialStateChanges.PullToRefreshLoading())
                .onErrorReturn(PartialStateChanges.PullToRefeshLoadingError::new);
    }

    static PartialStateChanges getFirstPageLoadedFromItems(List<Item> items) {
        ArrayList<FeedItem> data = new ArrayList<>(items.size());
        for (Item item : items) {
            data.add(item);
        }
        return new PartialStateChanges.FirstPageLoaded(data);
    }

    static PartialStateChanges getNextPageLoadedFromItems(List<Item> items) {
        ArrayList<FeedItem> data = new ArrayList<>(items.size());
        for (Item item : items) {
            data.add(item);
        }
        return new PartialStateChanges.NextPageLoaded(data);
    }

    static PartialStateChanges getRefreshLoadedFromItems(List<Item> items) {
        ArrayList<FeedItem> data = new ArrayList<>(items.size());
        for (Item item : items) {
            data.add(item);
        }
        return new PartialStateChanges.PullToRefreshLoaded(data);
    }
}
