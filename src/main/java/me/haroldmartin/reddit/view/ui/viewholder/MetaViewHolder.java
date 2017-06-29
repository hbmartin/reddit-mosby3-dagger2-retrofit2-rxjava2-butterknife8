package me.haroldmartin.reddit.view.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.haroldmartin.reddit.R;
import me.haroldmartin.reddit.api.model.reddit.Item;

public class MetaViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.author) TextView author;
    @BindView(R.id.date) TextView itemDate;
    @BindView(R.id.comments) TextView comments;
    @BindView(R.id.itemTitle) TextView itemTitle;

    public MetaViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Item item) {
        itemTitle.setText(item.getTitle());

        author.setText(itemView.getContext().getResources().getString(
                R.string.author, item.getAuthor()
        ));

        comments.setText(itemView.getContext().getResources().getString(
                R.string.comments, item.getNumComments()
        ));

        CharSequence relativeTimeSpan = DateUtils.getRelativeTimeSpanString(
                ((long) item.getCreatedUtc()) * 1000L,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS);
        itemDate.setText(relativeTimeSpan);
    }
}
