package lymansky.artem.photoclient.presenter;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import lymansky.artem.photoclient.model.Photo;
import lymansky.artem.photoclient.model.SearchResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoDataSource extends PageKeyedDataSource<Integer, Photo> {

    public interface SearchQueryListener {
        String getSearchQuery();
    }

    private static SearchQueryListener queryListener;

    public static void setSearchQueryListener(SearchQueryListener listener) {
        queryListener = listener;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Photo> callback) {
        if (queryListener.getSearchQuery() == null || queryListener.getSearchQuery().equals("")) {
            Client.getService(Service.class)
                    .getPhotos(KeyHolder.CLIENT_ID, KeyHolder.PAGE, KeyHolder.PER_PAGE)
                    .enqueue(new Callback<List<Photo>>() {
                        @Override
                        public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                            if (response.body() != null) {
                                callback.onResult(response.body(), null, KeyHolder.PAGE + 1);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Photo>> call, Throwable t) {

                        }
                    });
        } else {
            Log.e("PhotoDataSource", "loadInitial, else statement was loaded with query = " + queryListener.getSearchQuery());
            Client.getService(Service.class)
                    .getSearchResults(KeyHolder.CLIENT_ID, queryListener.getSearchQuery(), KeyHolder.PAGE, KeyHolder.PER_PAGE)
                    .enqueue(new Callback<SearchResults>() {
                        @Override
                        public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                            if (response.body().getPhotos() != null) {
                                List<Photo> photos = response.body().getPhotos();
                                callback.onResult(photos, null, KeyHolder.PAGE + 1);
                            }
                        }

                        @Override
                        public void onFailure(Call<SearchResults> call, Throwable t) {

                        }
                    });
        }
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Photo> callback) {
        if (queryListener.getSearchQuery() == null || queryListener.getSearchQuery().equals("")) {
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

                        }
                    });
        } else {
            Client.getService(Service.class)
                    .getSearchResults(KeyHolder.CLIENT_ID, queryListener.getSearchQuery(), params.key, KeyHolder.PER_PAGE)
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

                        }
                    });
        }
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Photo> callback) {
        if (queryListener.getSearchQuery() == null || queryListener.getSearchQuery().equals("")) {
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

                        }
                    });
        } else {
            Client.getService(Service.class)
                    .getSearchResults(KeyHolder.CLIENT_ID, queryListener.getSearchQuery(), params.key, KeyHolder.PER_PAGE)
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

                        }
                    });
        }
    }
}
