package me.haroldmartin.reddit.intent.details;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import me.haroldmartin.reddit.api.RedditApi;
import me.haroldmartin.reddit.application.RedditApplication;

public class DetailsInteractor {
  @Inject RedditApi backendApi;

  public DetailsInteractor() {
    RedditApplication.getComponent().inject(this);
  }

  public Observable<DetailsViewState> getDetails(String itemId) {
    return backendApi.getItem(itemId)
        .subscribeOn(Schedulers.io())
        .map(DetailsViewState.DataState::new)
        .cast(DetailsViewState.class)
        .startWith(new DetailsViewState.LoadingState())
        .onErrorReturn(DetailsViewState.ErrorState::new);
  }
}
