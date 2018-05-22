package splitsound.com.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import splitsound.com.main.R;

/**
 * Created by Neel on 5/15/2018.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    //TODO have actual data, this is just sample data
    private UserInfo[] users = {
            new UserInfo("Me", "Nexus 6P", true),
            new UserInfo("Emanuel", "OnePlus69", false),
            new UserInfo("Char*", "Test", false),
            new UserInfo("Jack", "Some phone", true),
            new UserInfo("Aneesh", "iPhone", false)
    };

    //provides the views we need to change
    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView muteImage;
        public TextView userName;
        public TextView userDevice;

        public ViewHolder(View view){
            super(view);
            muteImage = itemView.findViewById(R.id.mute_icon);
            userName = itemView.findViewById(R.id.user_name);
            userDevice = itemView.findViewById(R.id.device_name);
        }

    }

    // Create new views (invoked by the layout manager)
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //sets the lockimage to correspond to if there is a password
        if(users[position].isMuted()) {
            holder.muteImage.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }
        //sets the server name
        holder.userName.setText(users[position].getName());
        //sets the server IP address
        holder.userDevice.setText("Listening on " + users[position].getDeviceName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return users.length;
    }
}
