package me.haroldmartin.reddit.view.home;

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import me.haroldmartin.reddit.application.RedditApplication;
import me.haroldmartin.reddit.intent.FeedItem;
import me.haroldmartin.reddit.intent.home.HomeInteractor;
import me.haroldmartin.reddit.intent.home.HomeViewState;
import me.haroldmartin.reddit.intent.home.PartialStateChanges;
import timber.log.Timber;

public class HomePresenter extends MviBasePresenter<HomeTarget, HomeViewState> {

    @Inject HomeInteractor interactor;

    public HomePresenter() {
        RedditApplication.getComponent().inject(this);
    }

    @Override
    protected void bindIntents() {
        Observable<PartialStateChanges> loadFirstPage = intent(HomeTarget::loadFirstPageIntent)
                .doOnNext(ignored -> Timber.d("intent: load first page"))
                .flatMap(interactor::loadFirstPage)
                .observeOn(AndroidSchedulers.mainThread());

        Observable<PartialStateChanges> nextPage =
                intent(HomeTarget::loadNextPageIntent)
                        .doOnNext(ignored -> Timber.d("intent: load next page"))
                        .flatMap(interactor::loadNextPage)
                        .observeOn(AndroidSchedulers.mainThread());

        Observable<PartialStateChanges> pullToRefresh = intent(HomeTarget::pullToRefreshIntent)
                .doOnNext(ignored -> Timber.d("intent: pull to refresh"))
                .flatMap(interactor::loadRefresh)
                .observeOn(AndroidSchedulers.mainThread());

        Observable<PartialStateChanges> allIntentsObservable =
                Observable.merge(loadFirstPage, nextPage, pullToRefresh)
                        .observeOn(AndroidSchedulers.mainThread());

        HomeViewState initialState = new HomeViewState.Builder().firstPageLoading(true).build();

        subscribeViewState(
                allIntentsObservable.scan(initialState, this::viewStateReducer).distinctUntilChanged(),
                HomeTarget::render);
    }

    private HomeViewState viewStateReducer(HomeViewState previousState,
                                           PartialStateChanges partialChanges) {

        if (partialChanges instanceof PartialStateChanges.FirstPageLoading) {
            return previousState.builder().firstPageLoading(true).firstPageError(null).build();
        }

        if (partialChanges instanceof PartialStateChanges.FirstPageError) {
            return previousState.builder()
                    .firstPageLoading(false)
                    .firstPageError(((PartialStateChanges.FirstPageError) partialChanges).getError())
                    .build();
        }

        if (partialChanges instanceof PartialStateChanges.FirstPageLoaded) {
            return previousState.builder()
                    .firstPageLoading(false)
                    .firstPageError(null)
                    .data(((PartialStateChanges.FirstPageLoaded) partialChanges).getData())
                    .build();
        }

        if (partialChanges instanceof PartialStateChanges.NextPageLoading) {
            return previousState.builder().nextPageLoading(true).nextPageError(null).build();
        }

        if (partialChanges instanceof PartialStateChanges.NexPageLoadingError) {
            return previousState.builder()
                    .nextPageLoading(false)
                    .nextPageError(((PartialStateChanges.NexPageLoadingError) partialChanges).getError())
                    .build();
        }

        if (partialChanges instanceof PartialStateChanges.NextPageLoaded) {
            List<FeedItem> data = new ArrayList<>(previousState.getData().size()
                    + ((PartialStateChanges.NextPageLoaded) partialChanges).getData().size());
            data.addAll(previousState.getData());
            data.addAll(((PartialStateChanges.NextPageLoaded) partialChanges).getData());

            return previousState.builder().nextPageLoading(false).nextPageError(null).data(data).build();
        }

        if (partialChanges instanceof PartialStateChanges.PullToRefreshLoading) {
            return previousState.builder().pullToRefreshLoading(true).pullToRefreshError(null).build();
        }

        if (partialChanges instanceof PartialStateChanges.PullToRefeshLoadingError) {
            return previousState.builder()
                    .pullToRefreshLoading(false)
                    .pullToRefreshError(
                            ((PartialStateChanges.PullToRefeshLoadingError) partialChanges).getError())
                    .build();
        }

        if (partialChanges instanceof PartialStateChanges.PullToRefreshLoaded) {
            List<FeedItem> data = new ArrayList<>(previousState.getData().size()
                    + ((PartialStateChanges.PullToRefreshLoaded) partialChanges).getData().size());
            data.addAll(((PartialStateChanges.PullToRefreshLoaded) partialChanges).getData());
            data.addAll(previousState.getData());
            return previousState.builder()
                    .pullToRefreshLoading(false)
                    .pullToRefreshError(null)
                    .data(data)
                    .build();
        }


        throw new IllegalStateException("Don't know how to reduce the partial state " + partialChanges);
    }
}
