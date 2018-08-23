package lymansky.artem.photoclient.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photo {

    @SerializedName("id")
    private String id;
    @SerializedName("urls")
    private Urls urls;
    @SerializedName("links")
    private Links links;
    @SerializedName("user")
    private User user;

    public String getId() {
        return id;
    }

    public Urls getUrls() {
        return urls;
    }

    public Links getLinks() {
        return links;
    }

    public User getUser() {
        return user;
    }
}
