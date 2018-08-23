package lymansky.artem.photoclient.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    private String username;

    public String getUsername() {
        return username;
    }
}
