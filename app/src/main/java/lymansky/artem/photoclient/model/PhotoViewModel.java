package lymansky.artem.photoclient.model;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;

import lymansky.artem.photoclient.presenter.KeyHolder;
import lymansky.artem.photoclient.presenter.PhotoDataSource;
import lymansky.artem.photoclient.presenter.PhotoDataSourceFactory;

public class PhotoViewModel extends ViewModel {
    private LiveData<PagedList<Photo>> photoPagedList;

    public PhotoViewModel() {
        createLiveData(new PhotoDataSource());
    }

    public PhotoViewModel(PageKeyedDataSource dataSource) {
        createLiveData(dataSource);
    }

    public LiveData<PagedList<Photo>> getPhotoPagedList() {
        return photoPagedList;
    }

    public void updateData(LifecycleOwner lifecycleOwner, PageKeyedDataSource dataSource) {
        photoPagedList.removeObservers(lifecycleOwner);
        photoPagedList = createLiveData(dataSource);
    }

    private LiveData<PagedList<Photo>> createLiveData(PageKeyedDataSource dataSource) {
        PhotoDataSourceFactory photoDataSourceFactory = new PhotoDataSourceFactory(dataSource);

        PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(KeyHolder.PER_PAGE).build();

        photoPagedList = (new LivePagedListBuilder(photoDataSourceFactory, pagedListConfig))
                .build();

        return photoPagedList;
    }
}
