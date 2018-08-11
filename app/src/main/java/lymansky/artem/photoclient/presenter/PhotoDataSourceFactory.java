package lymansky.artem.photoclient.presenter;


import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import lymansky.artem.photoclient.model.Photo;

public class PhotoDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Photo>> photoLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<Integer, Photo> create() {
        PhotoDataSource photoDataSource = new PhotoDataSource();
        photoLiveDataSource.postValue(photoDataSource);
        return photoDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Photo>> getPhotoLiveDataSource() {
        return photoLiveDataSource;
    }
}
