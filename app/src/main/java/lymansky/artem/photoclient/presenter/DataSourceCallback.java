package lymansky.artem.photoclient.presenter;

public interface DataSourceCallback {
    void onConnectionFailure();
    void onEmptyResponse();
}
