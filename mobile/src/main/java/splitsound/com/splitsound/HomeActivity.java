package splitsound.com.splitsound;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import splitsound.com.net.AppPacket;
import splitsound.com.net.OpusAudioThread;
import splitsound.com.net.RTPNetworking;
import splitsound.com.ui.adapters.UserListAdapter;

/**
 * Home Play Activity
 *
 * @version 0.0.1
 * @author Emanuel, Neel
 */
public class HomeActivity extends Fragment
{
    private static final String TAG = "HomeActivity";

    private RecyclerView userRV;

    private SlidingUpPanelLayout slideUp;

    private boolean isActionBarHidden;

    /**
     * Executed when the view is created from the MainActivity
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_home, container, false);
    }

    /**
     * Executed after the view is created
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        isActionBarHidden = false;

        // Set the recycler view of the user list
        userRV = getView().findViewById(R.id.user_list_recycler_view);
        userRV.setHasFixedSize(true);
        userRV.setLayoutManager(new LinearLayoutManager(getContext()));
        userRV.setAdapter(new UserListAdapter());

        // Setup sliding panel action listeners
        slideUp = (SlidingUpPanelLayout)getView().findViewById(R.id.sliding_layout);
        slideUp.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener(){

            @Override
            public void onPanelSlide(View panel, float slideOffset) throws NullPointerException
            {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);

                // Collapse/Uncollapse the toolbar based on panel location/offset
                if(slideOffset >= .9 && !isActionBarHidden)
                    isActionBarHidden = ((DrawerActivityTest)getActivity()).collapseBar();
                else if(slideOffset < .9 && isActionBarHidden)
                    isActionBarHidden = ((DrawerActivityTest)getActivity()).unCollapseBar();
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState)
            {
                Log.i(TAG, "onPanelStateChanged " + newState);

                // Changes image and text of the info bar based on panel position
                ImageView image = (ImageView)getView().findViewById(R.id.usr_control);

                // Change arrow image
                ImageView arrowIcon = (ImageView)getView().findViewById(R.id.arrow_icon);
                TextView swipe = (TextView)getView().findViewById(R.id.swipe);

                if(newState == SlidingUpPanelLayout.PanelState.EXPANDED)
                {
                    arrowIcon.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    swipe.setText(R.string.close_drawer);
                }
                else if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED)
                {
                    arrowIcon.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    swipe.setText(R.string.open_drawer);
                }
            }
        });

        // RTCP transmission testing
        //TODO: Remove after basic transmission is setup
        ImageButton button = (ImageButton)getView().findViewById(R.id.connect);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                RTPNetworking.requestQ.add(AppPacket.LIST_ALL);

                new Thread(new OpusAudioThread()).start();
            }
        });

        getActivity().setTitle("Home");
        super.onViewCreated(view, savedInstanceState);

    }
}
