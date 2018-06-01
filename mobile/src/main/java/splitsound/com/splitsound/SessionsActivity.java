package splitsound.com.splitsound;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import splitsound.com.ui.adapters.RecyclerAdapter;

/**
 * Created by Neel on 6/1/2018.
 */

public class SessionsActivity extends Fragment
{
    private RecyclerView sessRV;

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


        sessRV = (RecyclerView) getView().findViewById(R.id.server_list_recycler_view);
        sessRV.setHasFixedSize(true);
        sessRV.setLayoutManager(new LinearLayoutManager(getContext()));
        sessRV.setAdapter(new RecyclerAdapter());
    }
}
