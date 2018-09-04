package lymansky.artem.photoclient.model;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;

import lymansky.artem.photoclient.presenter.PhotoDataSource;
import lymansky.artem.photoclient.presenter.PhotoDataSourceFactory;

public class PhotoViewModel extends ViewModel {
    private LiveData<PagedList<Photo>> photoPagedList;

    private static Filter sFilter;

    public PhotoViewModel() {
        createLiveData(sFilter);
    }

    public LiveData<PagedList<Photo>> getPhotoPagedList() {
        return photoPagedList;
    }

    public void updateData(LifecycleOwner lifecycleOwner, Filter filter) {
        photoPagedList.removeObservers(lifecycleOwner);
        photoPagedList = createLiveData(filter);
    }

    public static Filter getFilter() {
        return sFilter;
    }

    public static void setFilter(Filter filter) {
        sFilter = filter;
    }

    private LiveData<PagedList<Photo>> createLiveData(Filter filter) {
        PhotoDataSourceFactory photoDataSourceFactory = new PhotoDataSourceFactory(filter);

        PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(KeyHolder.PER_PAGE)
                .build();

        photoPagedList = (new LivePagedListBuilder(photoDataSourceFactory, pagedListConfig))
                .build();

        return photoPagedList;
    }
}
