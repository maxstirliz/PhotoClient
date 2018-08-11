package lymansky.artem.photoclient.model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import lymansky.artem.photoclient.presenter.Client;
import lymansky.artem.photoclient.presenter.Service;

public class PhotoKeyedDataSource extends ItemKeyedDataSource<String, Photo> {

    Service service;
    LoadInitialParams<String> initialParams;
    LoadParams<String> afterParams;
    private MutableLiveData networkState;
    private MutableLiveData initialLoading;
    private Executor retryExecutor;

    public PhotoKeyedDataSource(Executor retryExecutor) {
        service = Client.getService(Service.class);
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
        this.retryExecutor = retryExecutor;
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<Photo> callback) {
        List<Photo> photoList = new ArrayList<>();
        initialParams = params;
        initialLoading.postValue("Loading");
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<Photo> callback) {

    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<Photo> callback) {

    }

    @NonNull
    @Override
    public String getKey(@NonNull Photo item) {
        return null;
    }
}
