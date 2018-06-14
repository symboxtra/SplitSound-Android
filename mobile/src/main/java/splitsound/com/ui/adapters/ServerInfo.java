package splitsound.com.ui.adapters;

/**
 * Objectifies each Server
 *
 * @version 0.0.1
 * @author Emanuel
 */
public class ServerInfo
{

    /**
     * Constructor to create a Server object
     *
     * @param name Name of the server
     * @param IP IP address of the server
     * @param peopleListening Number of people listening in the server
     * @param hasPassword Server is password protected or not
     */
    public ServerInfo(String name, String IP, int peopleListening, boolean hasPassword)
    {
        this.name = name;
        this.IP = IP;
        this.peopleListening = peopleListening;
        this.hasPassword = hasPassword;
    }

    /**
     * Gets the name of the server from the Server object
     *
     * @return name of server
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the server to the particular Server object
     *
     * @param name name of server
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the IP address of the server from the Server object
     *
     * @return IP address of server
     */
    public String getIP() {
        return IP;
    }

    /**
     * Sets the IP address of the server to the particular Server object
     *
     * @param IP IP address of server
     */
    public void setIP(String IP) {
        this.IP = IP;
    }

    /**
     * Gets the number of people listening in the server from the Server object
     *
     * @return number of people listening
     */
    public int getPeopleListening() {
        return peopleListening;
    }

    /**
     * Sets the number of people listening in the server to the particular Server object
     *
     * @param peopleListening number of people listening
     */
    public void setPeopleListening(int peopleListening) {
        this.peopleListening = peopleListening;
    }

    /**
     * Checks if the server is password protected
     *
     * @return protection status
     */
    public boolean isHasPassword() {
        return hasPassword;
    }

    /**
     * Sets the password protection status of the server
     *
     * @param hasPassword protection status
     */
    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    // Server properties
    private String IP;
    private String name;
    private int peopleListening;
    private boolean hasPassword;
}
