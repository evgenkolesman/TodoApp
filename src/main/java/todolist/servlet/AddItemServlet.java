package todolist.servlet;

import todolist.data.StoreData;
import todolist.model.Item;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class AddItemServlet extends HttpServlet {

    Logger logger = Logger.getLogger(AddItemServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StoreData store = new StoreData();
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        String description = req.getParameter("description");
        Item item = new Item(description);
        try {
            store.add(item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }
}

