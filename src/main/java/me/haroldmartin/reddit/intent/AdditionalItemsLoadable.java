package me.haroldmartin.reddit.intent;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class AdditionalItemsLoadable {
    private final int moreItemsAvailableCount;
    private final String groupName;
    private final boolean loading;
    private final Throwable loadingError;

    public AdditionalItemsLoadable(int moreItemsAvailableCount, @NonNull String groupName,
                                   boolean loading, @Nullable Throwable loadingError) {
        this.moreItemsAvailableCount = moreItemsAvailableCount;
        this.groupName = groupName;
        this.loading = loading;
        this.loadingError = loadingError;
    }

    public int getMoreItemsCount() {
        return moreItemsAvailableCount;
    }

    public int getMoreItemsAvailableCount() {
        return moreItemsAvailableCount;
    }

    public boolean isLoading() {
        return loading;
    }

    public Throwable getLoadingError() {
        return loadingError;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdditionalItemsLoadable that = (AdditionalItemsLoadable) o;

        if (moreItemsAvailableCount != that.moreItemsAvailableCount) return false;
        if (loading != that.loading) return false;
        if (!groupName.equals(that.groupName)) return false;
        return loadingError != null ? loadingError.getClass().equals(that.loadingError.getClass())
                : that.loadingError == null;
    }

    @Override
    public int hashCode() {
        int result = moreItemsAvailableCount;
        result = 31 * result + groupName.hashCode();
        result = 31 * result + (loading ? 1 : 0);
        result = 31 * result + (loadingError != null ? loadingError.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AdditionalItemsLoadable{" +
                "moreItemsAvailableCount=" + moreItemsAvailableCount +
                ", groupName='" + groupName + '\'' +
                ", loading=" + loading +
                ", loadingError=" + loadingError +
                '}';
    }
}
