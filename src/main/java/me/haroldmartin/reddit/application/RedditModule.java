package me.haroldmartin.reddit.application;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.haroldmartin.reddit.api.RedditApi;
import me.haroldmartin.reddit.intent.PagingFeedLoader;
import me.haroldmartin.reddit.intent.details.DetailsInteractor;
import me.haroldmartin.reddit.intent.home.HomeInteractor;

@Module
public class RedditModule {
    Application mApplication;

    public RedditModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }


    @Provides
    @Singleton
    RedditApi providesRedditApi() {
        return new RedditApi();
    }

    @Provides
    PagingFeedLoader providesPagingFeedLoader() { return new PagingFeedLoader(); }

    @Provides
    DetailsInteractor providesDetailsInteractor() { return new DetailsInteractor(); }

    @Provides
    HomeInteractor providesHomeInteractor() { return new HomeInteractor(); }
}