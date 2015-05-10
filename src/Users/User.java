package Users;

import Rooms.RoomList;
import com.google.appengine.repackaged.com.google.gson.annotations.Expose;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class User {
    @Expose
    @XmlElement
    private String userName;
    @Expose
    @XmlElement
    private int status;
    @Expose
    @XmlElement
    private String info;
    private String sessionID;

    private User() {
    }

    public User(String userName,String sessionID) {
            this.userName = userName;
            this.sessionID = sessionID;
            status = UserStatus.ONLINE;
            RoomList.getInstance().getDefaultRoom().addUser(this);
    }

    public void logout(){
        sessionID="";
        status=UserStatus.DISCONECTED;
    }

    public String getName() {
        return userName;
    }

    public int getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
