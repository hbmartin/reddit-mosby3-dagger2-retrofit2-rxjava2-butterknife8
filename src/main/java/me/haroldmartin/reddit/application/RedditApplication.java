package me.haroldmartin.reddit.application;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;

public class RedditApplication extends Application {

    private static RedditComponent sComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        Timber.plant(new Timber.DebugTree());

        sComponent = DaggerRedditComponent.builder()
                .redditModule(new RedditModule(this))
                .build();
    }

    public static RedditComponent getComponent() {
        return sComponent;
    }
}
