package lymansky.artem.photoclient.model;

import com.google.gson.annotations.SerializedName;

public class Links {

    public Links(String download) {
        this.download = download;
    }

    @SerializedName("download")
    private String download;

    public String getDownload() {
        return download;
    }

}
