package splitsound.com.ui.adapters;

/**
 * Objectifies each User
 *
 * @version 0.0.1
 * @author Emanuel
 */
public class UserInfo
{

    /**
     * Constructor to create a User object
     *
     * @param name Name of user
     * @param deviceName Name of device used
     * @param muted User's mute status
     */
    public UserInfo(String name, String deviceName, boolean muted)
    {
        this.name = name;
        this.deviceName = deviceName;
        this.muted = muted;
    }

    /**
     * Gets the name of the device usedfrom the User object
     *
     * @return name of the device
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * Sets the name of the device used by the User object
     *
     * @param deviceName
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
     * Gets the name of the device used from the User object
     *
     * @return name of user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user to the User object
     *
     * @param name name of user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Checks if the user is muted
     *
     * @return User mute status
     */
    public boolean isMuted() {
        return muted;
    }

    /**
     * Sets the mute status of the User object
     *
     * @param muted mute status
     */
    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    // User properties
    private String deviceName;
    private String name;
    private boolean muted;
}
