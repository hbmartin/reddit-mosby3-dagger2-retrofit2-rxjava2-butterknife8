
package me.haroldmartin.reddit.api.model.reddit;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gif {

    @SerializedName("source")
    @Expose
    private Source source;
    @SerializedName("resolutions")
    @Expose
    private List<Resolution> resolutions = null;

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public List<Resolution> getResolutions() {
        return resolutions;
    }

    public void setResolutions(List<Resolution> resolutions) {
        this.resolutions = resolutions;
    }

}
