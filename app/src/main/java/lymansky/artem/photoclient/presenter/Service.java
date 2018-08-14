package lymansky.artem.photoclient.presenter;

import java.util.List;

import lymansky.artem.photoclient.model.Photo;
import lymansky.artem.photoclient.model.SearchResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    @GET("/photos")
    Call<List<Photo>> getPhotos(@Query("client_id") String clientId,
                                @Query("page") int page,
                                @Query("per_page") int perPage);

    @GET("/search/photos")
    Call<SearchResults> getSearchResults(@Query("client_id") String clientId,
                                               @Query("query") String query,
                                               @Query("page") int page,
                                               @Query("per_page") int perPage);
}
