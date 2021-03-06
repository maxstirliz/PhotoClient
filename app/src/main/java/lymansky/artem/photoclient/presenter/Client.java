package lymansky.artem.photoclient.presenter;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static final String BASE_URL = "https://api.unsplash.com";
    private static final String BASE_DOWNLOAD_URL = "https://unsplash.com";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static Retrofit retrofitDownload = new Retrofit.Builder()
            .baseUrl("https://unsplash.com")
            .build();

    public static <S> S getService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public static <S> S getDownloadService(Class<S> serviceClass) {
        return retrofitDownload.create(serviceClass);
    }
}
