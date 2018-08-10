package lymansky.artem.photoclient.view;

import android.arch.paging.PagedList;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import lymansky.artem.photoclient.R;
import lymansky.artem.photoclient.adapters.PhotoAdapter;
import lymansky.artem.photoclient.model.Photo;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    PhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.recyclerView);

    }
}
