package lymansky.artem.photoclient.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photo {

    @SerializedName("id")
    private String id;
    @SerializedName("raw")
    private String raw;
    @SerializedName("urls")
    private Urls urls;
    @SerializedName("links")
    private Links links;

    public String getId() {
        return id;
    }

    public String getRaw() {
        return raw;
    }

    public Urls getUrls() {
        return urls;
    }

    public Links getLinks() {
        return links;
    }
}
