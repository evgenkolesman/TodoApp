package todolist.servlet;

import todolist.data.StoreData;
import todolist.model.Item;
import org.apache.log4j.Logger;
import todolist.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class AddItemServlet extends HttpServlet {

    Logger logger = Logger.getLogger(AddItemServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StoreData store = StoreData.getInstance();
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        String description = req.getParameter("description");
        User user = (User) req.getSession().getAttribute("user");
        Item item = new Item(description, user);
        try {
            store.add(item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }
}

