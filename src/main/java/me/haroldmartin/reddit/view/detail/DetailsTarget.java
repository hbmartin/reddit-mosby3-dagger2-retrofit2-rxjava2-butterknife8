package me.haroldmartin.reddit.view.detail;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import me.haroldmartin.reddit.intent.details.DetailsViewState;
import io.reactivex.Observable;

public interface DetailsTarget extends MvpView {
  Observable<String> loadDetailsIntent();

  void render(DetailsViewState state);
}
