package splitsound.com.splitsound;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import splitsound.com.net.AppPacket;
import splitsound.com.audio.opus.OpusAudioThread;
import splitsound.com.net.RTPNetworking;
import splitsound.com.ui.adapters.UserListAdapter;

/**
 * Home Play Activity
 *
 * @author Emanuel, Neel
 * @version 0.0.1
 */
public class HomeActivity extends Fragment {
    private static final String TAG = "HomeActivity";

    private RecyclerView userRV;

    private SlidingUpPanelLayout slideUp;

    private boolean isPlay;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home, container, false);
    }

    /**
     * Executed after the view is created
     *
     * @param view
     * @param savedInstanceState
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Click listeners for the main buttons
        ImageView prev = getView().findViewById(R.id.main_skip_back_button);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ImageView next = getView().findViewById(R.id.main_skip_forward_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        isPlay = true;
        final ImageView playPause = getView().findViewById(R.id.main_play_button);
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlay) {
                    playPause.setImageResource(R.drawable.ic_pause_black_24dp);
                } else {
                    playPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }
                isPlay = !isPlay;
            }
        });

        isActionBarHidden = false;

        // Set the recycler view of the user list
        userRV = getView().findViewById(R.id.user_list_recycler_view);
        userRV.setHasFixedSize(true);
        userRV.setLayoutManager(new LinearLayoutManager(getContext()));
        userRV.setAdapter(new UserListAdapter());

        // Setup sliding panel action listeners
        slideUp = (SlidingUpPanelLayout) getView().findViewById(R.id.sliding_layout);
        slideUp.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) throws NullPointerException {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);

                // Collapse/Uncollapse the toolbar based on panel location/offset
                if (slideOffset >= .9 && !isActionBarHidden)
                    isActionBarHidden = ((DrawerActivityTest) getActivity()).collapseBar();
                else if (slideOffset < .9 && isActionBarHidden)
                    isActionBarHidden = ((DrawerActivityTest) getActivity()).unCollapseBar();
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);

                // Changes image and text of the info bar based on panel position
                ImageView image = (ImageView) getView().findViewById(R.id.usr_control);

                // Change arrow image
                ImageView arrowIcon = (ImageView) getView().findViewById(R.id.arrow_icon);
                TextView swipe = (TextView) getView().findViewById(R.id.swipe);

                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    arrowIcon.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    swipe.setText(R.string.close_drawer);
                } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    arrowIcon.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    swipe.setText(R.string.open_drawer);
                }
            }
        });

        final ImageButton button = (ImageButton) getView().findViewById(R.id.connect);
        //button.setImageDrawable();
        button.setImageResource(R.drawable.play_button); // R.drawable.play_button
        button.setOnTouchListener(new View.OnTouchListener(){

            boolean pause = false;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    if(pause)
                    {
                        button.requestLayout();
                        button.setImageResource(R.drawable.play_button_mute);
                        button.getLayoutParams().height = 550;
                        button.getLayoutParams().width = 550;
                    }
                    else
                    {
                        button.setImageResource(R.drawable.play_button);
                        button.requestLayout();
                        button.getLayoutParams().height = 500;
                        button.getLayoutParams().width = 500;
                    }

                    //TODO: Remove after basic transmission is setup
                    RTPNetworking.requestQ.add(AppPacket.LIST_ALL);
                    new Thread(new OpusAudioThread()).start();
                }
                else if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    if(pause)
                    {
                        button.requestLayout();
                        button.setImageResource(R.drawable.play_button_mute_hover);
                        button.getLayoutParams().height = 500;
                        button.getLayoutParams().width = 500;
                    }
                    else
                    {
                        button.requestLayout();
                        button.setImageResource(R.drawable.play_button_hover);
                        button.getLayoutParams().height = 500;
                        button.getLayoutParams().width = 500;
                    }

                    pause = !pause;
                }
                return true;
            }
        });

        getActivity().setTitle("Home");
        super.onViewCreated(view, savedInstanceState);

    }

}
