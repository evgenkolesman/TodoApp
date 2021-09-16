package todolist.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import todolist.data.StoreData;
import todolist.model.Item;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class IndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StoreData data = StoreData.getInstance();
        List<Item> itemList = data.findAll();
        ObjectMapper mapper = new ObjectMapper();
        String itemsAsString = mapper.writeValueAsString(itemList);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(itemsAsString);
    }
}
