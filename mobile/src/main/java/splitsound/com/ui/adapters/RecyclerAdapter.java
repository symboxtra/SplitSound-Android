package splitsound.com.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import splitsound.com.net.AppPacket;
import splitsound.com.net.RTPNetworking;
import splitsound.com.splitsound.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public SharedPreferences shrdPref;
    public SharedPreferences.Editor spEdit;

    private static MaterialDialog builder;

    //TODO have actual data, this is just sample data
    private ServerInfo[] servers = {
            new ServerInfo("My server", "80.108.12.11", 3, true),
            new ServerInfo("Jack's Server", "52.235.91.65", 69, true),
            new ServerInfo("Char *'s Server", "232.246.24.132", 420, false),
            new ServerInfo("Neel's Server", "173.9.192.172", 96, false),
            new ServerInfo("Neel's Server", "1.204.221.235", 240, true),
    };
  
    private int selectedPosition = 0;

    private static ArrayList<ServerInfo> serversA = new ArrayList<ServerInfo>();

    public static void addServer(ServerInfo serv)
    {
        serversA.add(serv);
    }

    public static void getServer(int s)
    {
        serversA.get(0);
    }
  
    //provides the views we need to change
    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView lockImage;
        public TextView serverName;
        public TextView serverIP;
        public TextView amountOfPeople;
        public boolean locked;

        public ViewHolder(View view){
            super(view);
            locked = true;
            lockImage = itemView.findViewById(R.id.lock_icon);
            serverName = itemView.findViewById(R.id.server_name);
            serverIP = itemView.findViewById(R.id.server_address);
            amountOfPeople = itemView.findViewById(R.id.amount_of_people_connected);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(final View v)
                {
                    if(locked)
                    {
                        builder = new MaterialDialog.Builder(v.getContext())
                                .title("Session Password")
                                .content("Enter the session password:")
                                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                                .input("password", "", new MaterialDialog.InputCallback() {

                                    @Override
                                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                        //spEdit.putString(v.getContext().getString(R.string.username), input.toString());
                                        //spEdit.apply();

                                        RTPNetworking.requestQ.add(AppPacket.LOGIN);
                                    }
                                })
                                .positiveText("OK")
                                .negativeText("Cancel")
                                .show();
                    }
                }
            });
        }

    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.server_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //sets the lockimage to correspond to if there is a password
        if(!serversA.get(position).isHasPassword()) {
            holder.lockImage.setImageResource(R.drawable.ic_lock_open_black_24dp);
            holder.locked = false;
        }
        //sets the server name
        holder.serverName.setText(serversA.get(position).getName());
        //sets the amount of people listening
        holder.amountOfPeople.setText(String.valueOf(serversA.get(position).getPeopleListening()));
        //sets the server IP address
        holder.serverIP.setText(serversA.get(position).getIP());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return serversA.size();
    }
}
