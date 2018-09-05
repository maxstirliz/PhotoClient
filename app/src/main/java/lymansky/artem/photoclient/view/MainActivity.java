package lymansky.artem.photoclient.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import lymansky.artem.photoclient.R;
import lymansky.artem.photoclient.presenter.DataSourceCallback;
import lymansky.artem.photoclient.presenter.PhotoAdapter;
import lymansky.artem.photoclient.model.Filter;
import lymansky.artem.photoclient.model.Photo;
import lymansky.artem.photoclient.model.PhotoViewModel;
import lymansky.artem.photoclient.presenter.PhotoDataSource;
import lymansky.artem.photoclient.presenter.PhotoDataSourceFiltered;

public class MainActivity extends AppCompatActivity implements DataSourceCallback {

    private static final int SPAN_COUNT_PORTRAY = 2;
    private static final int SPAN_COUNT_LANDSCAPE = 3;

    private GridLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private PhotoAdapter mAdapter;
    private Filter mFilter;
    private PhotoViewModel mPhotoViewModel;
    private LinearLayout mNoInternetMessage;
    private LinearLayout mEmptyResultsMessage;
    private ImageView mNoInternetImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PhotoDataSource.setDataSourceCallback(this);
        PhotoDataSourceFiltered.setDataSourceCallback(this);

        mNoInternetMessage = findViewById(R.id.noInternetMessage);
        mEmptyResultsMessage = findViewById(R.id.emptyResultsMessage);
        mNoInternetImage = findViewById(R.id.no_internet_image);
        setMessagesToGone();

        mAdapter = new PhotoAdapter();
        mRecyclerView = findViewById(R.id.recyclerView);

        if (PhotoViewModel.getFilter() == null) {
            mFilter = new Filter();
            PhotoViewModel.setFilter(mFilter);
        } else {
            mFilter = PhotoViewModel.getFilter();
        }

        //Configuration check
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setupLayoutManager(SPAN_COUNT_PORTRAY);
        } else if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setupLayoutManager(SPAN_COUNT_LANDSCAPE);
        }


        mPhotoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        mPhotoViewModel.getPhotoPagedList().observe(this, new Observer<PagedList<Photo>>() {
            @Override
            public void onChanged(@Nullable PagedList<Photo> photos) {
                mAdapter.submitList(photos);
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mNoInternetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMessagesToGone();
                mPhotoViewModel.updateData(MainActivity.this, mFilter);
                mPhotoViewModel.getPhotoPagedList().observe(MainActivity.this, new Observer<PagedList<Photo>>() {
                    @Override
                    public void onChanged(@Nullable PagedList<Photo> photos) {
                        setMessagesToGone();
                        mAdapter.submitList(photos);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setFocusable(false);
        searchView.setQueryHint(getString(R.string.search_here));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mFilter.setSearchQuery(s);
                setMessagesToGone();
                mPhotoViewModel.updateData(MainActivity.this, mFilter);
                mPhotoViewModel.getPhotoPagedList().observe(MainActivity.this, new Observer<PagedList<Photo>>() {
                    @Override
                    public void onChanged(@Nullable PagedList<Photo> photos) {
                        mAdapter.submitList(photos);
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    //DataSourceCallback methods
    @Override
    public void onConnectionFailure() {
        mEmptyResultsMessage.setVisibility(View.GONE);
        mNoInternetMessage.setVisibility(View.VISIBLE);

    }

    @Override
    public void onEmptyResponse() {
        mNoInternetMessage.setVisibility(View.GONE);
        mEmptyResultsMessage.setVisibility(View.VISIBLE);
    }

    //Custom methods
    private void setupLayoutManager(int span) {
        mLayoutManager = new GridLayoutManager(this, span, LinearLayoutManager.VERTICAL, false);
    }

    private void setMessagesToGone() {
        mNoInternetMessage.setVisibility(View.GONE);
        mEmptyResultsMessage.setVisibility(View.GONE);
    }
}
