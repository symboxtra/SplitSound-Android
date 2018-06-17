package splitsound.com.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import splitsound.com.splitsound.R;

/**
 * Adapter to store Server Recycler View
 *
 * @version 0.0.1
 * @author Emanuel
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>
{

    //TODO have actual data, this is just sample data
    private UserInfo[] users = {
            new UserInfo("Me", "Nexus 6P", true),
            new UserInfo("Emanuel", "OnePlus69", false),
            new UserInfo("Char*", "Test", false),
            new UserInfo("Jack", "Some phone", true),
            new UserInfo("Aneesh", "iPhone", false)
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

        public ViewHolder(View view)
        {
            super(view);

            // Set resources to the variables for later use
            muteImage = itemView.findViewById(R.id.mute_icon);
            userName = itemView.findViewById(R.id.user_name);
            userDevice = itemView.findViewById(R.id.device_name);
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
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        // Sets the lockimage to correspond to if there is a password
        if(users[position].isMuted())
            holder.muteImage.setImageResource(R.drawable.ic_volume_off_black_24dp);
        // Sets the server name
        holder.userName.setText(users[position].getName());
        // Sets the server IP address
        holder.userDevice.setText("Listening on " + users[position].getDeviceName());
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
