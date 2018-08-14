package lymansky.artem.photoclient.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;

import lymansky.artem.photoclient.presenter.KeyHolder;
import lymansky.artem.photoclient.presenter.PhotoDataSource;
import lymansky.artem.photoclient.presenter.PhotoDataSourceFactory;

public class PhotoViewModel extends ViewModel {
    public LiveData<PagedList<Photo>> photoPagedList;
    public LiveData<PageKeyedDataSource<Integer, Photo>> liveDataSource;

    public PhotoViewModel() {
        PhotoDataSourceFactory photoDataSourceFactory = new PhotoDataSourceFactory();
        liveDataSource = photoDataSourceFactory.getPhotoLiveDataSource();

        PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(KeyHolder.PER_PAGE).build();

        photoPagedList = (new LivePagedListBuilder(photoDataSourceFactory, pagedListConfig))
                .build();
    }
}
