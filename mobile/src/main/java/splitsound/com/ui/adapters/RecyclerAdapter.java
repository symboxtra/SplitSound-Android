package splitsound.com.ui.adapters;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{
    //TODO: have actual data, this is just sample data
    private ServerInfo[] servers = {
            new ServerInfo("My server", "80.108.12.11", 3, true),
            new ServerInfo("Jack's Server", "52.235.91.65", 69, true),
            new ServerInfo("Char *'s Server", "232.246.24.132", 420, false),
            new ServerInfo("Neel's Server", "173.9.192.172", 96, false),
            new ServerInfo("Neel's Server", "1.204.221.235", 240, true),
    };
  
    private int selectedPosition = 0;

    // Structure that contains view contents
    private static ArrayList<ServerInfo> serversA = new ArrayList<ServerInfo>();

    /**
     * Add new Server to server list
     *
     * @param serv ServerInfo instance
     */
    public static void addServer(ServerInfo serv)
    {
        serversA.add(serv);
    }

    /**
     * Get Server at position s from serverlist
     *
     * @param s Position of Object
     * @return ServerInfo instance
     */
    public static ServerInfo getServer(int s)
    {
        return serversA.get(s);
    }

    /**
     * Provides the sub-views we need to change
     */
    class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView lockImage;
        public TextView serverName;
        public TextView serverIP;
        public TextView amountOfPeople;
        public boolean locked;

        private EditText passwordInput;

        public ViewHolder(View view)
        {
            super(view);

            // Set resources to the variables for later use
            locked = true;
            lockImage = itemView.findViewById(R.id.lock_icon);
            serverName = itemView.findViewById(R.id.server_name);
            serverIP = itemView.findViewById(R.id.server_address);
            amountOfPeople = itemView.findViewById(R.id.amount_of_people_connected);

            // Click listener for each server card
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(final View v)
                {
                    if(locked)
                    {
                        // Display dialog to store password into shared preferences
                        MaterialDialog builder = new MaterialDialog.Builder(v.getContext())
                                .title("Session Password")
                                .customView(R.layout.pass_dialog, true)
                                .positiveText("CONNECT")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        PreferenceManager.getDefaultSharedPreferences(SplitSoundApplication.getAppContext()).edit().putString("password", passwordInput.getText().toString()).apply();
                                        RTPNetworking.requestQ.add(AppPacket.LOGIN);
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
                    }
                }
            });
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
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // Create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.server_card, parent, false);
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
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        // Sets the lockimage to correspond to if there is a password
        if(!serversA.get(position).isHasPassword()) {
            holder.lockImage.setImageResource(R.drawable.ic_lock_open_black_24dp);
            holder.locked = false;
        }
        // Sets the server name
        holder.serverName.setText(serversA.get(position).getName());
        // Sets the amount of people listening
        holder.amountOfPeople.setText(String.valueOf(serversA.get(position).getPeopleListening()));
        // Sets the server IP address
        holder.serverIP.setText(serversA.get(position).getIP());
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     *
     * @return Size of dataset
     */
    @Override
    public int getItemCount() {
        return serversA.size();
    }
}
