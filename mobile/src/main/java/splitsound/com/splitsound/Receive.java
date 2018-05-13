package splitsound.com.splitsound;


import android.app.Activity;
import android.widget.TextView;

import jlibrtp.*;

/**
 * Created by Neel on 2/28/2018.
 */

public class Receive implements RTPAppIntf{

    RTPSession session = null;
    public Activity activity;
    public int frameSize(int payloadType)
    {
        return 1;
    }

    public void receiveData(DataFrame frame, Participant p)
    {
        //TextView tv = (TextView) activity.findViewById(R.id.sample_text);
        //tv.append("Got myself: " + new String(frame.getConcatenatedData())+"\n");
    }

    public Receive(Activity active)
    {
        activity = active;
    }

    public void userEvent(int type, Participant[] participants)
    {

    }
}
