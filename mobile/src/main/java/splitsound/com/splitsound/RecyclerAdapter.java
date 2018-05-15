package splitsound.com.splitsound;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    //TODO have actual data, this is just sample data
    private ServerInfo[] servers = {
            new ServerInfo("My server", "80.108.12.11", 3, true),
            new ServerInfo("Jack's Server", "52.235.91.65", 69, true),
            new ServerInfo("Char *'s Server", "232.246.24.132", 420, false),
            new ServerInfo("Neel's Server", "173.9.192.172", 96, false),
            new ServerInfo("Neel's Server", "1.204.221.235", 240, true),
    };

    //provides the views we need to change
    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView lockImage;
        public TextView serverName;
        public TextView serverIP;
        public TextView amountOfPeople;

        public ViewHolder(View view){
            super(view);
            lockImage = itemView.findViewById(R.id.lock_icon);
            serverName = itemView.findViewById(R.id.server_name);
            serverIP = itemView.findViewById(R.id.server_address);
            amountOfPeople = itemView.findViewById(R.id.amount_of_people_connected);
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
        if(!servers[position].isHasPassword()) {
            holder.lockImage.setImageResource(R.drawable.ic_lock_open_black_24dp);
        }
        //sets the server name
        holder.serverName.setText(servers[position].getName());
        //sets the amount of people listening
        holder.amountOfPeople.setText(servers[position].getPeopleListening());
        //sets the server IP address
        holder.serverIP.setText(servers[position].getIP());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return servers.length;
    }
}
