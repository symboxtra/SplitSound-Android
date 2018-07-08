package splitsound.com.splitsound;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro2{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest
        int backgroundColor = Color.DKGRAY;
        addSlide(AppIntroFragment.newInstance("Welcome!", "This is how to use the app", R.drawable.download, backgroundColor));

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
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something when slide is changed
    }
}

/*
HOW TO USE: https://github.com/apl-devs/AppIntro/wiki/How-to-Use#show-the-intro-once
 */