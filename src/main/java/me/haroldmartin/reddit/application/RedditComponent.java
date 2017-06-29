package me.haroldmartin.reddit.application;

import javax.inject.Singleton;

import dagger.Component;
import me.haroldmartin.reddit.intent.home.HomeInteractor;
import me.haroldmartin.reddit.view.home.MainActivity;
import me.haroldmartin.reddit.intent.PagingFeedLoader;
import me.haroldmartin.reddit.intent.details.DetailsInteractor;
import me.haroldmartin.reddit.view.detail.DetailsPresenter;
import me.haroldmartin.reddit.view.home.HomePresenter;

@Singleton
@Component(modules={RedditModule.class})
public interface RedditComponent {
    void inject(MainActivity activity);

    void inject(HomePresenter homePresenter);

    void inject(PagingFeedLoader pagingFeedLoader);

    void inject(DetailsInteractor detailsInteractor);

    void inject(DetailsPresenter detailsPresenter);

    void inject(HomeInteractor homeInteractor);
}