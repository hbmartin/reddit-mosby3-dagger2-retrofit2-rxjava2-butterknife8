package me.haroldmartin.reddit.view.detail;

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter;

import javax.inject.Inject;

import me.haroldmartin.reddit.application.RedditApplication;
import me.haroldmartin.reddit.intent.details.DetailsInteractor;
import me.haroldmartin.reddit.intent.details.DetailsViewState;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;


public class DetailsPresenter
        extends MviBasePresenter<DetailsTarget, DetailsViewState> {
    @Inject DetailsInteractor interactor;

    public DetailsPresenter() {
        RedditApplication.getComponent().inject(this);
    }

    @Override
    protected void bindIntents() {
        Observable<DetailsViewState> loadDetails =
                intent(DetailsTarget::loadDetailsIntent)
                        .doOnNext(it -> Timber.d("intent: load details for item id = %s", it))
                        .flatMap(interactor::getDetails)
                        .observeOn(AndroidSchedulers.mainThread());

        subscribeViewState(loadDetails, DetailsTarget::render);
    }
}
