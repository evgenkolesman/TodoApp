package todolist.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import todolist.data.StoreCategory;

import todolist.model.Category;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categoryList = StoreCategory.getInstance().findAll();
        ObjectMapper mapper = new ObjectMapper();
        String serializedCategory = mapper.writeValueAsString(categoryList);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(serializedCategory);
    }
}
