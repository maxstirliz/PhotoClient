package lymansky.artem.photoclient.model;

import com.google.gson.annotations.SerializedName;

public class Urls {

    public Urls(String raw, String thumb) {
        this.raw = raw;
        this.thumb = thumb;
    }

    @SerializedName("raw")
    private String raw;
    @SerializedName("thumb")
    private String thumb;

    public String getRaw() {
        return raw;
    }

    public String getThumb() {
        return thumb;
    }
}
