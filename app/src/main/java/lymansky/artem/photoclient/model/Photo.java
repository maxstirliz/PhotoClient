package lymansky.artem.photoclient.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photo {

//    public Photo(String id, List<Urls> urls, List<Links> links) {
//        this.id = id;
//        this.urls = urls;
//        this.links = links;
//    }

    public Photo(String id) {
        this.id = id;
    }

    @SerializedName("id")
    private String id;
    @SerializedName("raw")
    private String raw;
    @SerializedName("urls")
    private Urls urls;
//    @SerializedName("links")
//    private List<Links> links;

    public String getId() {
        return id;
    }

    public String getRaw() {
        return raw;
    }

    public Urls getUrls() {
        return urls;
    }

//    public List<Links> getLinks() {
//        return links;
//    }
}
