package me.haroldmartin.reddit.intent.home;

import java.util.List;

import me.haroldmartin.reddit.intent.FeedItem;


public interface PartialStateChanges {
    final class FirstPageLoading implements PartialStateChanges { }

    final class FirstPageError implements PartialStateChanges {
        private final Throwable error;

        public FirstPageError(Throwable error) {
            this.error = error;
        }

        public Throwable getError() {
            return error;
        }

        @Override
        public String toString() {
            return "FirstPageErrorState{" +
                    "error=" + error +
                    '}';
        }
    }

    final class FirstPageLoaded implements PartialStateChanges {
        private final List<FeedItem> data;

        public FirstPageLoaded(List<FeedItem> data) {
            this.data = data;
        }

        public List<FeedItem> getData() {
            return data;
        }
    }

    final class NextPageLoaded implements PartialStateChanges {
        private final List<FeedItem> data;

        public NextPageLoaded(List<FeedItem> data) {
            this.data = data;
        }

        public List<FeedItem> getData() {
            return data;
        }
    }

    final class NexPageLoadingError implements PartialStateChanges {
        private final Throwable error;

        public NexPageLoadingError(Throwable error) {
            this.error = error;
        }

        public Throwable getError() {
            return error;
        }
    }

    final class NextPageLoading implements PartialStateChanges { }

    final class PullToRefreshLoading implements PartialStateChanges { }

    final class PullToRefeshLoadingError implements PartialStateChanges {
        private final Throwable error;

        public PullToRefeshLoadingError(Throwable error) {
            this.error = error;
        }

        public Throwable getError() {
            return error;
        }
    }

    final class PullToRefreshLoaded implements PartialStateChanges {
        private final List<FeedItem> data;

        public PullToRefreshLoaded(List<FeedItem> data) {
            this.data = data;
        }

        public List<FeedItem> getData() {
            return data;
        }
    }
}
