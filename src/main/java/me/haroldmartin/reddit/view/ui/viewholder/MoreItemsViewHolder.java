package me.haroldmartin.reddit.view.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.haroldmartin.reddit.R;
import me.haroldmartin.reddit.intent.AdditionalItemsLoadable;

public class MoreItemsViewHolder extends RecyclerView.ViewHolder {

    public static MoreItemsViewHolder create(LayoutInflater layoutInflater) {
        return new MoreItemsViewHolder(
                layoutInflater.inflate(R.layout.item_more_available, null, false));
    }

    @BindView(R.id.moreItemsCount) TextView moreItemsCount;
    @BindView(R.id.loadingView) View loadingView;
    @BindView(R.id.loadMoreButtton) View loadMoreButton;
    @BindView(R.id.errorRetryButton) Button errorRetry;

    private MoreItemsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(AdditionalItemsLoadable item) {
        if (item.isLoading()) {
            // TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            moreItemsCount.setVisibility(View.GONE);
            loadMoreButton.setVisibility(View.GONE);
            loadingView.setVisibility(View.VISIBLE);
            errorRetry.setVisibility(View.GONE);
            itemView.setClickable(false);
        } else if (item.getLoadingError() != null) {
            //TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            moreItemsCount.setVisibility(View.GONE);
            loadMoreButton.setVisibility(View.GONE);
            loadingView.setVisibility(View.GONE);
            errorRetry.setVisibility(View.VISIBLE);
            itemView.setClickable(true);
        } else {
            moreItemsCount.setText("+" + item.getMoreItemsCount());
            moreItemsCount.setVisibility(View.VISIBLE);
            loadMoreButton.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.GONE);
            errorRetry.setVisibility(View.GONE);
            itemView.setClickable(true);
        }
    }
}
