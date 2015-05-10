package Servlets.Utils;

import Rooms.Room;
import Rooms.RoomList;
import Users.User;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GetListServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private RoomList roomList = RoomList.getInstance();

	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws IOException 
	{
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		OutputStream os = resp.getOutputStream();
		HttpSession session=req.getSession(false);
		String cType=(String)session.getAttribute("contentType");

		String[] whatToDo=req.getParameter("p").split(",");
		if (whatToDo!=null){
			for (int i = 0; i <whatToDo.length ; i++) {
				if (whatToDo[i].equals("rooms")) roomList.writeToStream(os,cType);
				if (whatToDo[i].equals("users")) {
					String roomName=req.getParameter("room");
					if (roomName!=null){
						Room room=roomList.getRoomByName(roomName);
						room.writeToStreamUsersList(os, cType);
					}

				}
				if (whatToDo[i].equals("message")){
					int fromN=0;
					if (req.getParameter("fromN")!=null) {
						try {
							fromN = Integer.parseInt(req.getParameter("fromN"));
						} catch (Exception e) {
							resp.setStatus(400);
						}
					}
					String roomName=req.getParameter("room");
					if (roomName!=null) {
						Room room=roomList.getRoomByName(roomName);
						User user=room.getUserBySession(session.getId());
						if (user==null){
							resp.setStatus(400);
							return;
						}
						room.writeToStreamMessageList(os,cType,user,fromN);
					}
				}
			}
		}

	}
}
