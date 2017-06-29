
package me.haroldmartin.reddit.api.model.reddit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Top {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("kind")
    @Expose
    private String kind;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

}
