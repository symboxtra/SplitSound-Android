package splitsound.com.splitsound;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.net.DatagramSocket;

import jlibrtp.*;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    static public String helloWorld()
    {
        return "Hello World";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //new Thread(){
          //  int i = bounce();
        //}.start();

        // Example of a call to a native method
        TextView tv = (TextView) this.findViewById(R.id.sample_text);
        Button b = (Button)this.findViewById(R.id.connect);
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        bounce.setRepeatCount(Animation.INFINITE);
        b.startAnimation(bounce);
        tv.setText("Wassup!");


        DatagramSocket rtpSocket = null;
        DatagramSocket rtcpSocket = null;

        try {
            rtpSocket = new DatagramSocket(6003);
            rtcpSocket = new DatagramSocket(6004);
        }catch(Exception e)
        {
            Log.e("Datagram Socket", "RTPSession failed to obtain port");
            Log.e("Error: ", e.toString());
            finish();
        }
        TestReceive testReceive = new TestReceive(this);
        testReceive.session = new RTPSession(rtpSocket, rtcpSocket);
        testReceive.session.naivePktReception(true);
        testReceive.session.RTPSessionRegister(testReceive, null, null);

        Participant p = new Participant("127.0.0.1", 6003, 6004);
        testReceive.session.addParticipant(p);

        for(int i = 0;i < 10;i++)
        {
            String str = "Test number " + i;
            testReceive.session.sendData(str.getBytes());
        }

        
    }

    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public int bounce()
    {
        Button b = (Button)this.findViewById(R.id.connect);

        if(b==null)
        {
            Log.e("Bounce", "Connect Button is null");
            return 0;
        }
        float x = 110;
        int flag = -1;
        while(true)
        {
            if(x == -1)
                break;
            if(x == 0)
                flag = 1;
            else if(x == 100)
                flag = -1;

            b.setTranslationZ(x+=(10*flag));


            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return 1;
    }
}
