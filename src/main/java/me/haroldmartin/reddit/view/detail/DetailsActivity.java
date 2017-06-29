package me.haroldmartin.reddit.view.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby3.mvi.MviActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import me.haroldmartin.reddit.R;
import me.haroldmartin.reddit.api.model.reddit.Item;
import me.haroldmartin.reddit.intent.details.DetailsViewState;
import me.haroldmartin.reddit.view.ui.viewholder.MetaViewHolder;
import timber.log.Timber;

public class DetailsActivity extends MviActivity<DetailsTarget, DetailsPresenter>
        implements DetailsTarget {

    public static final String KEY_ITEM_ID = "itemId";
    private Item item;

    @BindView(R.id.errorView) View errorView;
    @BindView(R.id.loadingView) View loadingView;
    @BindView(R.id.detailsView) View detailsView;
    @BindView(R.id.itemImage) ImageView imageView;
    @BindView(R.id.root) ViewGroup rootView;
    @BindView(R.id.itemMeta) ViewGroup itemMeta;
    private MetaViewHolder metaViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        metaViewHolder = new MetaViewHolder(itemMeta);
    }

    @NonNull
    @Override
    public DetailsPresenter createPresenter() {
        return new DetailsPresenter();
    }

    @Override
    public Observable<String> loadDetailsIntent() {
        return Observable.just(getIntent().getStringExtra(KEY_ITEM_ID));
    }

    @Override
    public void render(DetailsViewState state) {
        Timber.d("render " + state);

        if (state instanceof DetailsViewState.LoadingState) {
            renderLoading();
        } else if (state instanceof DetailsViewState.DataState) {
            renderData((DetailsViewState.DataState) state);
        } else if (state instanceof DetailsViewState.ErrorState) {
            renderError();
        } else {
            throw new IllegalStateException("Unknown state " + state);
        }
    }

    private void renderError() {
        TransitionManager.beginDelayedTransition(rootView);
        errorView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
        detailsView.setVisibility(View.GONE);
    }

    private void renderData(DetailsViewState.DataState state) {
        TransitionManager.beginDelayedTransition(rootView);
        errorView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        detailsView.setVisibility(View.VISIBLE);

        item = state.getDetail();

        metaViewHolder.bind(item);

        String url = item.getUrl();

        if (url.contains(".gif")) {
            Glide.with(this)
                    .load(url)
                    .asGif()
                    .placeholder(R.drawable.waiting)
                    .fitCenter()
                    .into(imageView);
        } else {
            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.waiting)
                    .fitCenter()
                    .into(imageView);
        }
    }

    private void renderLoading() {
        TransitionManager.beginDelayedTransition(rootView);
        errorView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        detailsView.setVisibility(View.GONE);
    }

    @OnClick(R.id.itemImage)
    public void openBrowserOnImageClick() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
        startActivity(intent);
    }
}
