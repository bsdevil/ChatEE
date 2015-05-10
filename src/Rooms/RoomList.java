package Rooms;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;
import com.google.appengine.repackaged.com.google.gson.annotations.Expose;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

//@TODO добавить удаление пустых комнат (если нет пользователей)

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class RoomList {
    private final static RoomList roomList=new RoomList();
    @Expose
    @XmlElement
    private List<Room> rooms=new ArrayList<Room>();
    @Expose
    @XmlElement
    private Room defaultRoom;

    public static RoomList getInstance(){return roomList;}
    public synchronized void add(Room room){
        rooms.add(room);
    }
    public synchronized void delete(Room room){
        rooms.remove(room);
    }

    public Boolean isContains(String roomName){
        if (this.getRoomByName(roomName)==null) {
            return false;
        }
        return true;
    }

    public Room getRoomByName(String name){
        for (Room r:rooms){
            if (r.getName().equals(name)){
                return r;
            }
        }
        return null;
    }
    public Room getDefaultRoom() {
        return defaultRoom;
    }

    private RoomList() {
        defaultRoom=new Room("global",this);
        //@TODO remove
        new Room("test1",this);
        new Room("test2",this);
    }

    public void writeToStream(OutputStream os,String contentType){
        if (contentType.equals("JSON")) {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String json = gson.toJson(RoomList.getInstance());

            try {
                OutputStreamWriter osw = new OutputStreamWriter(os,"UTF-8");
                osw.write(json);
                osw.flush();
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {

            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(RoomList.class);
                Marshaller marshaller=jaxbContext.createMarshaller();
                marshaller.marshal(this,os);

            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }

    }

}
