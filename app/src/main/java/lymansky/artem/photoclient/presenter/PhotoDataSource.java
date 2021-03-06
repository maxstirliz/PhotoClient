package lymansky.artem.photoclient.presenter;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import java.util.List;

import lymansky.artem.photoclient.model.KeyHolder;
import lymansky.artem.photoclient.model.Photo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoDataSource extends PageKeyedDataSource<Integer, Photo> {

    private static DataSourceCallback sDataSourceCallback;

    public static void setDataSourceCallback(DataSourceCallback callback) {
        sDataSourceCallback = callback;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Photo> callback) {

        Client.getService(Service.class)
                .getPhotos(KeyHolder.CLIENT_ID, KeyHolder.PAGE, KeyHolder.PER_PAGE)
                .enqueue(new Callback<List<Photo>>() {
                    @Override
                    public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                        if (response.body() != null) {
                            callback.onResult(response.body(), null, KeyHolder.PAGE + 1);
                            if(response.body().size() < 1) sDataSourceCallback.onEmptyResponse();
                        } else {
                            sDataSourceCallback.onEmptyResponse();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Photo>> call, Throwable t) {
                        sDataSourceCallback.onConnectionFailure();
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Photo> callback) {

        Client.getService(Service.class)
                .getPhotos(KeyHolder.CLIENT_ID, params.key, KeyHolder.PER_PAGE)
                .enqueue(new Callback<List<Photo>>() {
                    @Override
                    public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                        if (response.body() != null) {
                            callback.onResult(response.body(), adjacentKey);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Photo>> call, Throwable t) {
                        sDataSourceCallback.onConnectionFailure();
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Photo> callback) {

        Client.getService(Service.class)
                .getPhotos(KeyHolder.CLIENT_ID, params.key, KeyHolder.PER_PAGE)
                .enqueue(new Callback<List<Photo>>() {
                    @Override
                    public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                        if (response.body() != null) {
                            Integer key = params.key + 1;
                            callback.onResult(response.body(), key);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Photo>> call, Throwable t) {
                        sDataSourceCallback.onConnectionFailure();
                    }
                });
    }
}
