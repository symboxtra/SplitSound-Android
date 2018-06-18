package splitsound.com.splitsound;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Home Play Activity
 *
 * @version 0.0.1
 * @author Emanuel, Neel
 */
public class SettingsActivity extends Activity
{

    public SharedPreferences shrdPref;
    public SharedPreferences.Editor spEdit;

    final Context context = this;

    /**
     * Executed when the application starts
     * the view is created
     *
     * @param savedInstanceState No clue what this is! ;P
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Adds back button to action bar of activity
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Open shared preferences instance for editing
        shrdPref = this.getPreferences(Context.MODE_PRIVATE);
        spEdit = shrdPref.edit();

        // Get ListView and set values and clickListener for User Settings
        ListView usListView = (ListView) findViewById(R.id.userSettings);
        String[] ary = {"Change username...", "Test"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_view_item, R.id.usrText, ary);
        usListView.setAdapter(adapter);
        setListViewClickListener(usListView);
    }

    /**
     * Executed when options on the toolbar are pressed
     *
     * @param item The item that is pressed on the toolbar
     * @return boolean based on successful actions performed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets onItemClicked listener to the provided list view
     *
     * @param userSettings View that needs to be binded with item click listener
     */
    private void setListViewClickListener(final ListView userSettings) {
        userSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);

                if (selectedItem.contains("username"))
                {
                    // Display dialog to store username into shared preferences
                    new MaterialDialog.Builder(view.getContext())
                            .title("Username")
                            .content("Enter a new username:")
                            .inputType(InputType.TYPE_CLASS_TEXT)
                            .input("username", "", new MaterialDialog.InputCallback() {

                                @Override
                                public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                    if(input.toString().matches("^[a-zA-Z0-9]*$"))
                                    {
                                        PreferenceManager.getDefaultSharedPreferences(SplitSoundApplication.getAppContext()).edit().putString("username", input.toString()).apply();
                                        Toast.makeText(context, "Username changed: " + PreferenceManager.getDefaultSharedPreferences(SplitSoundApplication.getAppContext()).getString("username", "NULL"), Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                        Toast.makeText(context, "Invalid username", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .inputRange(3, 25)
                            .positiveText("SUBMIT")
                            .show();
                }
            }
        });
    }
}
