package splitsound.com.splitsound;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import splitsound.com.net.AppPacket;
import splitsound.com.net.RTPNetworking;
import splitsound.com.ui.adapters.RecyclerAdapter;
import splitsound.com.ui.adapters.ServerInfo;

/**
 * Created by Neel on 6/1/2018.
 */

public class SessionsActivity extends Fragment
{
    public String temp = "SessionsActivity";

    private RecyclerView sessRV;
    private GifImageView gifImage;

    private SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayout.OnRefreshListener swipeListener;

    private Thread requestThread;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_sessions, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.sessions_option, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_refresh:
                RTPNetworking.requestQ.add(AppPacket.LIST_ALL);
                if(refreshLayout != null)
                {
                    refreshLayout.setRefreshing(true);
                    swipeListener.onRefresh();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Available Sessions");

        // Play loading animation uaing GIF
        gifImage = (GifImageView)getView().findViewById(R.id.splash_loading);
        try{
            InputStream is = getContext().getAssets().open("load_trans.gif");
            byte[] bytes = IOUtils.toByteArray(is);
            gifImage.setBytes(bytes);
            gifImage.startAnimation();
        }catch (IOException e) {
            e.printStackTrace();
        }

        // Send list_all RTCP packets while loading
        createRequestThread();
        requestThread.start();

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

                    // Stop sending list all packets
                    requestThread.interrupt();
                }catch (NullPointerException npe){
                }
            }
        }, new Random().nextInt(5000) + 2000);



        // Create Android pull-down refresh action
        refreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
        swipeListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                RTPNetworking.requestQ.add(AppPacket.LIST_ALL);

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        refreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
                        refreshLayout.setRefreshing(false);
                    }
                }, new Random().nextInt(5000) + 200);
            }
        };
        refreshLayout.setOnRefreshListener(swipeListener);

        RecyclerAdapter.addServer(new ServerInfo("My server", "80.108.12.11", 3, true));
    }

    public void createRequestThread()
    {
        requestThread = new Thread(){
            public void run()
            {
                while(!Thread.currentThread().isInterrupted())
                {
                    try {
                        RTPNetworking.requestQ.add(AppPacket.LIST_ALL);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };
    }

}