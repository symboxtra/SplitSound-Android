package splitsound.com.splitsound;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import splitsound.com.ui.adapters.RecyclerAdapter;

/**
 * Created by Neel on 6/1/2018.
 */

public class SessionsActivity extends Fragment
{
    private RecyclerView sessRV;
    private GifImageView gifImage;
    private SwipeRefreshLayout refreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_sessions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Available Sessions");

        gifImage = (GifImageView)getView().findViewById(R.id.splash_loading);

        try{
            InputStream is = getContext().getAssets().open("load_trans.gif");
            byte[] bytes = IOUtils.toByteArray(is);
            gifImage.setBytes(bytes);
            gifImage.startAnimation();
        }catch (IOException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Stop and remove animation and show the generated list
                gifImage.stopAnimation();
                gifImage.setVisibility(View.GONE);
                try {
                    sessRV = (RecyclerView) getView().findViewById(R.id.server_list_recycler_view);
                    sessRV.setHasFixedSize(true);
                    sessRV.setLayoutManager(new LinearLayoutManager(getContext()));
                    sessRV.setAdapter(new RecyclerAdapter());
                }catch (NullPointerException npe){
                }
            }
        }, new Random().nextInt(5000) + 2000);
        refreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
    }
    void refreshItems() {
        // Load items
        // ...

        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                refreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
                refreshLayout.setRefreshing(false);
            }
        }, 5000);
        // Stop refresh animation

    }
}
