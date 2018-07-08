package splitsound.com.splitsound;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;

public class IntroActivity extends AppIntro2{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            getSupportActionBar().hide();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest
        int backgroundColor = Color.DKGRAY;
        addSlide(AppIntro2Fragment.newInstance("Welcome!", "This is how to use the app", R.drawable.download, backgroundColor));
        // Turn vibration on and set intensity
        // NOTE: you will need to ask VIBRATE permission in Manifest if you haven't already
        setVibrate(true);
        setVibrateIntensity(30);

        // Animations -- use only one of the below. Using both could cause errors.
        setFadeAnimation(); // OR

        // Permissions -- takes a permission and slide number
        askForPermissions(new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.VIBRATE}, 1);

    }
    /*
    public void onDonePressed(Fragment currentFragment) {
        System.out.println("reached");
        Intent intent = new Intent(this, DrawerActivityTest.class); // Call the AppIntro java class
        startActivity(intent);
    }*/
    @Override
    public void onDonePressed(Fragment curentFragment) {
        super.onDonePressed(curentFragment);
        // Do something when users tap on Done button.
        finish();
    }
}
/*
HOW TO USE: https://github.com/apl-devs/AppIntro/wiki/How-to-Use#show-the-intro-once
 */