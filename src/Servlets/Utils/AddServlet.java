package Servlets.Utils;


import Messages.NewMessage;
import Rooms.Room;
import Rooms.RoomList;
import Users.User;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AddServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private RoomList roomList = RoomList.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        User user = roomList.getDefaultRoom().getUserBySession(session.getId());
        String status = req.getParameter("status");
        String roomName = req.getParameter("room");
        String action = req.getParameter("a");
        if ((roomName != null) && (action != null)) {
            if (action.equals("add")) {
                Room room;
                if (!roomList.isContains(roomName)) {
                    room = new Room(roomName);
                } else {
                    room = roomList.getRoomByName(roomName);
                }
                if (!room.isContain(user)) {
                    room.addUser(user);
                }
            } else if (action.equals("exit")) {
                Room room = roomList.getRoomByName(roomName);
                room.deleteUser(user);
            } else if (action.equals("join")) {
                Room room = roomList.getRoomByName(roomName);
                if (!room.isContain(user)) {
                    room.addUser(user);
                }
            }
        }
        if (status != null) {
            try {
                int statusID = Integer.parseInt(status);
                user.setStatus(statusID);
            } catch (Exception e) {
                resp.setStatus(400);
            }
        }

    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        User user = roomList.getDefaultRoom().getUserBySession(session.getId());
        String cType = (String) session.getAttribute("contentType");
        NewMessage.receive(req.getReader(), user, cType);

    }
}
