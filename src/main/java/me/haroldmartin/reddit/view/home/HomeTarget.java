package me.haroldmartin.reddit.view.home;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import io.reactivex.Observable;
import me.haroldmartin.reddit.intent.home.HomeViewState;

public interface HomeTarget extends MvpView {
    Observable<Boolean> loadFirstPageIntent();

    Observable<Boolean> loadNextPageIntent();

    Observable<Boolean> pullToRefreshIntent();

    void render(HomeViewState viewState);
}
