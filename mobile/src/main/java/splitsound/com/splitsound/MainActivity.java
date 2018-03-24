package splitsound.com.splitsound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.net.DatagramSocket;

import jlibrtp.*;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
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

        /*for(int i = 0;i < 10;i++)
        {
            String str = "Test number " + i;
            testReceive.session.sendData(str.getBytes());
    }*/

        
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
}
