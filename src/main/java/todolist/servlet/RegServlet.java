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
import java.util.Optional;

public class RegServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RegServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StoreUser store = StoreUser.getInstance();
        String email = req.getParameter("email");
        Optional<User> userOptional = Optional.ofNullable(store.findByEmail(email));
        if (userOptional.isEmpty()) {
            try {
                int id = 0;
                User user = new User();
                String username = req.getParameter("username");
                String password = req.getParameter("password");
                user.setPassword(password);
                user.setEmail(email);
                user.setName(username);
                user.setId(id);
                store.add(user);
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                req.setAttribute("505", "BAD REQUEST REGISTRATION");
            }

            req.getRequestDispatcher("login.html").forward(req, resp);
        } else {
            throw new ServletException("Wrong email or password!");
        }
    }
}
