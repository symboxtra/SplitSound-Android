package splitsound.com.ui.adapters;

public class UserInfo {

    public UserInfo(String name, String deviceName, boolean muted) {
        this.name = name;
        this.deviceName = deviceName;
        this.muted = muted;
    }

    private String deviceName;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    private String name;
    private boolean muted;
}
