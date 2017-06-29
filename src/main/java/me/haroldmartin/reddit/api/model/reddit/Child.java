
package me.haroldmartin.reddit.api.model.reddit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Child {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("data")
    @Expose
    private Item data;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Item getData() {
        return data;
    }

    public void setData(Item data) {
        this.data = data;
    }

}
