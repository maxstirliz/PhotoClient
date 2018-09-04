package lymansky.artem.photoclient.presenter;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import java.util.List;

import lymansky.artem.photoclient.model.Filter;
import lymansky.artem.photoclient.model.KeyHolder;
import lymansky.artem.photoclient.model.Photo;
import lymansky.artem.photoclient.model.SearchResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoDataSourceFiltered extends PageKeyedDataSource<Integer, Photo> {

    private Filter mFilter;

    public PhotoDataSourceFiltered(Filter filter) {
        this.mFilter = filter;
    }

    //Callback fields and setters
    private static DataSourceCallback sDataSourceCallback;

    public static void setDataSourceCallback(DataSourceCallback callback) {
        sDataSourceCallback = callback;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Photo> callback) {
        Client.getService(Service.class)
                .getSearchResults(KeyHolder.CLIENT_ID, mFilter.getSearchQuery(), KeyHolder.PAGE, KeyHolder.PER_PAGE)
                .enqueue(new Callback<SearchResults>() {
                    @Override
                    public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                        if (response.body().getPhotos() != null) {
                            List<Photo> photos = response.body().getPhotos();
                            callback.onResult(photos, null, KeyHolder.PAGE + 1);
                            if (photos.size() < 1) sDataSourceCallback.onEmptyResponse();
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResults> call, Throwable t) {
                        sDataSourceCallback.onConnectionFailure();
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Photo> callback) {
        Client.getService(Service.class)
                .getSearchResults(KeyHolder.CLIENT_ID, mFilter.getSearchQuery(), params.key, KeyHolder.PER_PAGE)
                .enqueue(new Callback<SearchResults>() {
                    @Override
                    public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                        if (response.body() != null) {
                            List<Photo> photos = response.body().getPhotos();
                            callback.onResult(photos, adjacentKey);
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResults> call, Throwable t) {
                        sDataSourceCallback.onConnectionFailure();
                    }
                });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Photo> callback) {
        Client.getService(Service.class)
                .getSearchResults(KeyHolder.CLIENT_ID, mFilter.getSearchQuery(), params.key, KeyHolder.PER_PAGE)
                .enqueue(new Callback<SearchResults>() {
                    @Override
                    public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                        if (response.body() != null) {
                            List<Photo> photos = response.body().getPhotos();
                            Integer key = params.key + 1;
                            callback.onResult(photos, key);
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResults> call, Throwable t) {
                        sDataSourceCallback.onConnectionFailure();
                    }
                });
    }
}
