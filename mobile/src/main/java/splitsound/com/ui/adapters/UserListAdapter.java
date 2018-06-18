package splitsound.com.ui.adapters;

import android.media.Image;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import splitsound.com.net.AppPacket;
import splitsound.com.net.RTPNetworking;
import splitsound.com.splitsound.R;
import splitsound.com.splitsound.SplitSoundApplication;

/**
 * Adapter to store Server Recycler View
 *
 * @version 0.0.1
 * @author Emanuel
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>
{
    private EditText passwordInput;

    //TODO have actual data, this is just sample data
    private UserInfo[] users = {
            new UserInfo("Me", "Nexus 6P", true, 0),
            new UserInfo("Emanuel", "OnePlus69", false, 0),
            new UserInfo("Char*", "Test", false, 0),
            new UserInfo("Jack", "Some phone", true, 0),
            new UserInfo("Aneesh", "iPhone", false, 0)
    };

    // Structure that contains view contents
    private static ArrayList<UserInfo> usersA = new ArrayList<UserInfo>();

    /**
     * Add new User to user list
     *
     * @param serv UserInfo instance
     */
    public static void addUser(UserInfo serv)
    {
        usersA.add(serv);
    }

    /**
     * Get User at position s from user list
     *
     * @param s Position of Object
     * @return UserInfo instance
     */
    public static UserInfo getUser(int s)
    {
        return usersA.get(s);
    }

    /**
     * Provides the sub-views we need to change
     */
    class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView muteImage;
        public TextView userName;
        public TextView userDevice;
        public ImageView userOptions;

        public ViewHolder(View view)
        {
            super(view);

            // Set resources to the variables for later use
            muteImage = itemView.findViewById(R.id.mute_icon);
            userName = itemView.findViewById(R.id.user_name);
            userDevice = itemView.findViewById(R.id.device_name);
            userOptions = itemView.findViewById(R.id.usr_control);
        }

    }

    /**
     * Create new recycler views (invoked by the layout manager)
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // Create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        // Sets the lockimage to correspond to if there is a password
        if(users[position].isMuted())
            holder.muteImage.setImageResource(R.drawable.ic_volume_off_black_24dp);
        // Sets the server name
        holder.userName.setText(users[position].getName());
        // Sets the server IP address
        holder.userDevice.setText("Listening on " + users[position].getDeviceName());
        // Sets the listener on options
        holder.userOptions.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(holder.itemView.getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.user_option, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        int id = item.getItemId();

                        switch (id)
                        {
                            case R.id.kick_user:
                            {
                                // Display dialog to get admin password into shared preferences to kick user
                                MaterialDialog builder = new MaterialDialog.Builder(holder.itemView.getContext())
                                        .title("Admin Password")
                                        .customView(R.layout.pass_dialog, true)
                                        .positiveText("KICK")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                                PreferenceManager.getDefaultSharedPreferences(SplitSoundApplication.getAppContext()).edit().putString("adminPassword", passwordInput.getText().toString()).apply();
                                            }
                                        })
                                        .negativeText("CANCEL")
                                        .build();
                                // Add custom view for showing password on button press
                                final View positiveAction = builder.getActionButton(DialogAction.POSITIVE);
                                passwordInput = builder.getCustomView().findViewById(R.id.password);
                                passwordInput.addTextChangedListener(
                                        new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                positiveAction.setEnabled(s.toString().trim().length() > 0);
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {}
                                        });

                                // Toggling the show password CheckBox will mask or unmask the password input EditText
                                CheckBox checkbox = builder.getCustomView().findViewById(R.id.showPassword);
                                checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        passwordInput.setInputType(!isChecked ? InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                                        passwordInput.setTransformationMethod(!isChecked ? PasswordTransformationMethod.getInstance() : null);
                                    }
                                });
                                builder.show();
                                positiveAction.setEnabled(false);
                                return true;
                            }
                        }

                        return false;
                    }
                });
            }
        });
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     *
     * @return Size of dataset
     */
    @Override
    public int getItemCount() {
        return users.length;
    }
}
