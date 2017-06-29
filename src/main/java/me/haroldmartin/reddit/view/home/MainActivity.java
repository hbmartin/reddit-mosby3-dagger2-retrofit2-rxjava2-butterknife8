package me.haroldmartin.reddit.view.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvi.MviActivity;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import me.haroldmartin.reddit.R;
import me.haroldmartin.reddit.api.model.reddit.Item;
import me.haroldmartin.reddit.intent.home.HomeViewState;
import me.haroldmartin.reddit.view.detail.DetailsActivity;
import me.haroldmartin.reddit.view.ui.viewholder.ItemViewHolder;
import timber.log.Timber;

public class MainActivity extends MviActivity<HomeTarget, HomePresenter>
        implements HomeTarget {

    private Unbinder unbinder;
    private HomeAdapter adapter;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.rootView) FrameLayout rootView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.loadingView) View loadingView;
    @BindView(R.id.errorView) TextView errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        adapter = new HomeAdapter(getLayoutInflater(), new HomeItemClickedListener());
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Timber.d("Activity onSaveInstanceState()");
    }

    @NonNull
    @Override
    public HomePresenter createPresenter() {
        Timber.d("createPresenter");
        return new HomePresenter();
    }

    @Override
    public Observable<Boolean> loadFirstPageIntent() {
        return Observable.just(true).doOnComplete(() -> Timber.d("firstPage completed"));
    }

    @Override
    public Observable<Boolean> loadNextPageIntent() {
        return RxRecyclerView.scrollStateChanges(recyclerView)
                .filter(event -> !adapter.isLoadingNextPage())
                .filter(event -> event == RecyclerView.SCROLL_STATE_IDLE)
                .filter(event -> layoutManager.findLastCompletelyVisibleItemPosition()
                        == adapter.getItems().size() - 1)
                .map(integer -> true);
    }

    @Override
    public Observable<Boolean> pullToRefreshIntent() {
        return RxSwipeRefreshLayout.refreshes(swipeRefreshLayout).map(ignored -> true);
    }

    @Override
    public void render(HomeViewState viewState) {
        Timber.d("render %s", viewState);
        if (!viewState.isLoadingFirstPage() && viewState.getFirstPageError() == null) {
            renderShowData(viewState);
        } else if (viewState.isLoadingFirstPage()) {
            renderFirstPageLoading();
        } else if (viewState.getFirstPageError() != null) {
            renderFirstPageError();
        } else {
            throw new IllegalStateException("Unknown view state " + viewState);
        }
    }

    private void renderShowData(HomeViewState state) {
        TransitionManager.beginDelayedTransition((ViewGroup) getView());
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        boolean changed = adapter.setLoadingNextPage(state.isLoadingNextPage());
        if (changed && state.isLoadingNextPage()) {
            // scroll to the end of the list so that the user sees the load more progress bar
            recyclerView.smoothScrollToPosition(adapter.getItemCount());
        }
        adapter.setItems(state.getData()); // this must be done before setLoading() otherwise error will occure. see https://github.com/sockeqwe/mosby/issues/244

        boolean pullToRefreshFinished = swipeRefreshLayout.isRefreshing()
                && !state.isLoadingPullToRefresh()
                && state.getPullToRefreshError() == null;
        if (pullToRefreshFinished) {
            // Swipe to refresh finished successfully so scroll to the top of the list (to see inserted items)
            recyclerView.smoothScrollToPosition(0);
        }

        swipeRefreshLayout.setRefreshing(state.isLoadingPullToRefresh());

        if (state.getNextPageError() != null) {
            Snackbar.make(getView(), R.string.error_unknown, Snackbar.LENGTH_LONG)
                    .show(); // callback?
        }

        if (state.getPullToRefreshError() != null) {
            Snackbar.make(getView(), R.string.error_unknown, Snackbar.LENGTH_LONG)
                    .show(); // callback?
        }
    }

    private void renderFirstPageLoading() {
        TransitionManager.beginDelayedTransition((ViewGroup) getView());
        loadingView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
    }

    private void renderFirstPageError() {
        TransitionManager.beginDelayedTransition((ViewGroup) getView());
        loadingView.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    public View getView() {
        return rootView;
    }

    class HomeItemClickedListener implements ItemViewHolder.ItemClickedListener {
        @Override
        public void onItemClicked(Item item, View itemView) {
            if (shouldOpenBrowser(item)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(DetailsActivity.KEY_ITEM_ID, item.getId());
                startActivity(intent);
            }
        }

        boolean shouldOpenBrowser(Item item) {
            Uri url = Uri.parse(item.getUrl());
            String filename = url.getLastPathSegment();

            if ((filename.contains(".gif") && !filename.contains(".gifv")) ||
                    filename.contains(".jpg") || filename.contains(".png")) {
                return false;
            } else {
                return true;
            }
        }
    }
}