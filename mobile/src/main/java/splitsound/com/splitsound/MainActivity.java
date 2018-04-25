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
       // enableStrictMode();
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


        new RTPSessionTask().execute();
    }

    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
