package me.haroldmartin.reddit.intent;

import android.support.annotation.NonNull;


public class SectionHeader {
    private final String name;

    public SectionHeader(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SectionHeader that = (SectionHeader) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "SectionHeader{" +
                "name='" + name + '\'' +
                '}';
    }
}
