package todolist.servlet;

import org.apache.log4j.Logger;
import todolist.data.StoreUser;
import todolist.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class RegServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RegServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        int id = 0;
        User user = new User();
        user.setPassword(password);
        user.setEmail(email);
        user.setName(username);
        user.setId(id);
        try {
            StoreUser store = StoreUser.instOf();
            store.add(user);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        req.getRequestDispatcher("login.html").forward(req, resp);
    }
}
