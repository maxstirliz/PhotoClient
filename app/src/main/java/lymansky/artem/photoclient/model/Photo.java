package lymansky.artem.photoclient.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photo {

    public Photo(String id, List<Urls> urls, List<Links> links) {
        this.id = id;
        this.urls = urls;
        this.links = links;
    }

    @SerializedName("id")
    private String id;
    @SerializedName("urls")
    private List<Urls> urls;
    @SerializedName("links")
    private List<Links> links;

    public String getId() {
        return id;
    }

    public List<Urls> getUrls() {
        return urls;
    }

    public List<Links> getLinks() {
        return links;
    }
}
