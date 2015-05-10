package Messages;

import Rooms.Room;
import Rooms.RoomList;
import Users.User;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class NewMessage {
    private String body;
    private Date date;
    private String roomName;
    private ArrayList<String> recipientsName;

    private NewMessage() {
    }

    public synchronized static void receive(BufferedReader br, User sender, String cType) throws IOException {
        NewMessage m=null;
        if (cType.equals("JSON")) {
            String s,json="";
            while ((s=br.readLine())!=null){
                json=json+s;
            }
            Gson gson = new GsonBuilder().create();
            m = gson.fromJson(json, NewMessage.class);
        } else {
            try {
                JAXBContext jaxbContext=JAXBContext.newInstance(NewMessage.class);
                Unmarshaller unmarshaller=jaxbContext.createUnmarshaller();
                m=(NewMessage)unmarshaller.unmarshal(br);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
        Room room=RoomList.getInstance().getRoomByName(m.roomName);
        Room defaultRoom=RoomList.getInstance().getDefaultRoom();
        ArrayList<User> userArrayList=new ArrayList<User>();
        if (m.recipientsName!=null) {
            for (String rec : m.recipientsName) {
                userArrayList.add(defaultRoom.getUserByName(rec));
            }
        }
        Message message=new Message(m.body,sender,userArrayList);
        room.addMessage(message);
    }

}
