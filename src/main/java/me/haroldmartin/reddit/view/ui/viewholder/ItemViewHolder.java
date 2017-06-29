package me.haroldmartin.reddit.view.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;

import me.haroldmartin.reddit.R;
import me.haroldmartin.reddit.api.model.reddit.Item;


public class ItemViewHolder extends RecyclerView.ViewHolder {

    private final MetaViewHolder metaViewHolder;

    public interface ItemClickedListener {
        void onItemClicked(Item item, View itemView);
    }

    public static ItemViewHolder create(LayoutInflater inflater, ItemClickedListener listener, ViewGroup parent) {
        return new ItemViewHolder(inflater.inflate(R.layout.item_detail, parent, false), listener);
    }

    @BindView(R.id.itemImage) ImageView image;
    @BindView(R.id.itemMeta) LinearLayout itemMeta;

    private Item item;

    private ItemViewHolder(View itemView, ItemClickedListener clickedListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> clickedListener.onItemClicked(item, itemView));
        metaViewHolder = new MetaViewHolder(itemMeta);
    }

    public void bind(Item item) {
        this.item = item;
        Glide.with(itemView.getContext())
                .load(item.getThumbnail())
                .placeholder(R.drawable.link)
                .error(R.drawable.link)
                .centerCrop()
                .into(image);
        metaViewHolder.bind(item);
    }
}
