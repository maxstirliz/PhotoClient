package lymansky.artem.photoclient.presenter;


import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import lymansky.artem.photoclient.model.Filter;
import lymansky.artem.photoclient.model.Photo;

public class PhotoDataSourceFactory extends DataSource.Factory<Integer, Photo> {

    private MutableLiveData<PageKeyedDataSource<Integer, Photo>> photoLiveDataSource = new MutableLiveData<>();
    private PageKeyedDataSource<Integer, Photo> photoDataSource;
    private Filter mFilter;

    public PhotoDataSourceFactory(Filter filter) {
        this.mFilter = filter;
        if (mFilter.isNullContent()) {
            photoDataSource = new PhotoDataSource();
        } else {
            photoDataSource = new PhotoDataSourceFiltered(mFilter);
        }
    }

    @Override
    public DataSource<Integer, Photo> create() {
        photoLiveDataSource.postValue(photoDataSource);
        return photoDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Photo>> getPhotoLiveDataSource() {
        return photoLiveDataSource;
    }
}
