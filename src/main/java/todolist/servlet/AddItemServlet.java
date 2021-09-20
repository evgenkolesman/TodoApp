package todolist.servlet;

import org.apache.log4j.Logger;
import todolist.data.StoreCategory;
import todolist.data.StoreData;
import todolist.model.Category;
import todolist.model.Item;
import todolist.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddItemServlet extends HttpServlet {

    Logger logger = Logger.getLogger(AddItemServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StoreCategory store = StoreCategory.getInstance();
        StoreData storeData = StoreData.getInstance();
//        resp.setContentType("text/plain");
//        resp.setCharacterEncoding("UTF-8");
        String description = req.getParameter("description");
        User user = (User) req.getSession().getAttribute("user");
        List<Category> list = new ArrayList<>();
        String[] categories = req.getParameterValues("category");
        List<String> catList = Arrays.asList(categories);
        catList.forEach(s -> list.add(store.findById(Integer.valueOf(s))));
        Item item = new Item(description, user, list);
        try {
            storeData.add(item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }
}

