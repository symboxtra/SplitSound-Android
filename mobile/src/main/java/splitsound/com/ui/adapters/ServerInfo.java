package splitsound.com.ui.adapters;

public class ServerInfo {

    public ServerInfo(String name, String IP, int peopleListening, boolean hasPassword) {
        this.name = name;
        this.IP = IP;
        this.peopleListening = peopleListening;
        this.hasPassword = hasPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPeopleListening() {
        return peopleListening;
    }

    public void setPeopleListening(int peopleListening) {
        this.peopleListening = peopleListening;
    }

    public boolean isHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    private String IP;
    private String name;
    private int peopleListening;
    private boolean hasPassword;
}
