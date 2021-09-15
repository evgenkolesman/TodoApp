package todolist.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import todolist.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GreetServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(user);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(userString);
    }
}
