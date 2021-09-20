package todolist.servlet;

import todolist.data.StoreUser;
import todolist.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public class AuthServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        StoreUser store = StoreUser.getInstance();
        Optional<User> userOptional = Optional.ofNullable(store.findByEmail(email));
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            User user = userOptional.get();
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            req.getRequestDispatcher("index.html").forward(req, resp);
        } else {
            throw new ServletException("Wrong email or password!");
        }
    }
}
