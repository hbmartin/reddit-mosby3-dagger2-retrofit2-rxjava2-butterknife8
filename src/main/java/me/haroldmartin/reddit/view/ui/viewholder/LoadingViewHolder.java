package me.haroldmartin.reddit.view.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import me.haroldmartin.reddit.R;


public class LoadingViewHolder extends RecyclerView.ViewHolder {

    public static LoadingViewHolder create(LayoutInflater inflater) {
        return new LoadingViewHolder(inflater.inflate(R.layout.item_loading, null, false));
    }

    private LoadingViewHolder(View itemView) {
        super(itemView);
    }
}
