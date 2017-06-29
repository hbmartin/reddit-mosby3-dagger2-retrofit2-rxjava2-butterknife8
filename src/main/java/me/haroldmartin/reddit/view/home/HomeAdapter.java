package me.haroldmartin.reddit.view.home;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.haroldmartin.reddit.intent.AdditionalItemsLoadable;
import me.haroldmartin.reddit.intent.FeedItem;
import me.haroldmartin.reddit.intent.SectionHeader;
import me.haroldmartin.reddit.api.model.reddit.Item;
import me.haroldmartin.reddit.view.ui.viewholder.LoadingViewHolder;
import me.haroldmartin.reddit.view.ui.viewholder.MoreItemsViewHolder;
import me.haroldmartin.reddit.view.ui.viewholder.ItemViewHolder;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter {

    static final int VIEW_TYPE_ITEM = 0;
    static final int VIEW_TYPE_LOADING_MORE_NEXT_PAGE = 1;
    static final int VIEW_TYPE_MORE_ITEMS_AVAILABLE = 2;

    private boolean isLoadingNextPage = false;
    private List<FeedItem> items;
    private final LayoutInflater layoutInflater;
    private final ItemViewHolder.ItemClickedListener itemClickedListener;

    public HomeAdapter(LayoutInflater layoutInflater,
                       ItemViewHolder.ItemClickedListener itemClickedListener) {
        this.layoutInflater = layoutInflater;
        this.itemClickedListener = itemClickedListener;
    }

    public List<FeedItem> getItems() {
        return items;
    }

    /**
     * @return true if value has changed since last invocation
     */
    public boolean setLoadingNextPage(boolean loadingNextPage) {
        boolean hasLoadingMoreChanged = loadingNextPage != isLoadingNextPage;

        boolean notifyInserted = loadingNextPage && hasLoadingMoreChanged;
        boolean notifyRemoved = !loadingNextPage && hasLoadingMoreChanged;
        isLoadingNextPage = loadingNextPage;

        if (notifyInserted) {
            notifyItemInserted(items.size());
        } else if (notifyRemoved) {
            notifyItemRemoved(items.size());
        }

        return hasLoadingMoreChanged;
    }

    public boolean isLoadingNextPage() {
        return isLoadingNextPage;
    }

    public void setItems(List<FeedItem> newItems) {
        List<FeedItem> oldItems = this.items;
        this.items = newItems;

        if (oldItems == null) {
            notifyDataSetChanged();
        } else {
            // Use Diff utils
            DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return oldItems.size();
                }

                @Override
                public int getNewListSize() {
                    return newItems.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    Object oldItem = oldItems.get(oldItemPosition);
                    Object newItem = newItems.get(newItemPosition);

                    if (oldItem instanceof Item
                            && newItem instanceof Item
                            && ((Item) oldItem).getId() == ((Item) newItem).getId()) {
                        return true;
                    }

                    if (oldItem instanceof SectionHeader
                            && newItem instanceof SectionHeader
                            && ((SectionHeader) oldItem).getName().equals(((SectionHeader) newItem).getName())) {
                        return true;
                    }

                    if (oldItem instanceof AdditionalItemsLoadable
                            && newItem instanceof AdditionalItemsLoadable) {
                        return true;
                    }

                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Object oldItem = oldItems.get(oldItemPosition);
                    Object newItem = newItems.get(newItemPosition);

                    return oldItem.equals(newItem);
                }
            }, true).dispatchUpdatesTo(this);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (isLoadingNextPage && position == items.size()) {
            return VIEW_TYPE_LOADING_MORE_NEXT_PAGE;
        }

        FeedItem item = items.get(position);

        if (item instanceof Item) {
            return VIEW_TYPE_ITEM;
        } else if (item instanceof AdditionalItemsLoadable) {
            return VIEW_TYPE_MORE_ITEMS_AVAILABLE;
        }

        throw new IllegalArgumentException("Not found view type for item at position "
                + position
                + ". Item is: "
                + item);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                return ItemViewHolder.create(layoutInflater, itemClickedListener, parent);
            case VIEW_TYPE_LOADING_MORE_NEXT_PAGE:
                return LoadingViewHolder.create(layoutInflater);
            case VIEW_TYPE_MORE_ITEMS_AVAILABLE:
                return MoreItemsViewHolder.create(layoutInflater);
        }

        throw new IllegalArgumentException("Couldn't create a ViewHolder for viewType  = " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof LoadingViewHolder) {
            return;
        }

        FeedItem item = items.get(position);
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).bind((Item) item);
        } else if (holder instanceof MoreItemsViewHolder) {
            ((MoreItemsViewHolder) holder).bind((AdditionalItemsLoadable) item);
        } else {
            throw new IllegalArgumentException("couldn't accept  ViewHolder " + holder);
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : (items.size() + (isLoadingNextPage ? 1 : 0));
    }
}
