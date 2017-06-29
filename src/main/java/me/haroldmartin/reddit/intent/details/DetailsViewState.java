package me.haroldmartin.reddit.intent.details;

import me.haroldmartin.reddit.api.model.reddit.Item;

public interface DetailsViewState {

    final class LoadingState implements DetailsViewState { }

    final class ErrorState implements DetailsViewState {
        private final Throwable error;

        public ErrorState(Throwable error) {
            this.error = error;
        }

        public Throwable getError() {
            return error;
        }

        @Override
        public String toString() {
            return "ErrorState{" +
                    "error=" + error +
                    '}';
        }
    }

    /**
     * Data has been loaded successfully and can now be displayed
     */
    final class DataState implements DetailsViewState {
        private final Item detail;

        public DataState(Item detail) {
            this.detail = detail;
        }

        public Item getDetail() {
            return detail;
        }

        @Override
        public String toString() {
            return "DataState{" +
                    "detail=" + detail +
                    '}';
        }
    }
}
