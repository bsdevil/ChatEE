package Servlets.Auth;

import Rooms.RoomList;
import Users.User;
import Users.UserStatus;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLDecoder;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
//@TODO переделать аутентификацию, брать с БД логин/пароль
        String contentType=req.getHeader("dataType").toUpperCase();
        if (!contentType.equals("XML")) contentType="JSON";

        String login=req.getHeader("login");
        login=URLDecoder.decode(login,"UTF-8");
//        if (login.equals("Login")){
        if ((login!=null)&&(!login.equals(""))){
            HttpSession session=req.getSession(true);

            session.setAttribute("UserName",login);
            session.setAttribute("contentType",contentType);
            if (RoomList.getInstance().getDefaultRoom().isContain(login)){

                User user=RoomList.getInstance().getDefaultRoom().getUserByName(login);
                if ((!user.getSessionID().equals(""))&&(!user.getSessionID().equals(session.getId()))){
                    session.invalidate();
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                user.setStatus(UserStatus.ONLINE);
                user.setSessionID(session.getId());
            }
            else{
                new User(login,session.getId());
            }
        }
        else {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
