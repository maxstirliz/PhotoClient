package lymansky.artem.photoclient.model;

import com.google.gson.annotations.SerializedName;

public class Urls {

    public Urls(String raw, String thumb) {
        this.regular = raw;
        this.thumb = thumb;
    }

    @SerializedName("regular")
    private String regular;
    @SerializedName("thumb")
    private String thumb;

    public String getRegular() {
        return regular;
    }

    public String getThumb() {
        return thumb;
    }
}
