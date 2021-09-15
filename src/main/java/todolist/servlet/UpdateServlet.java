package todolist.servlet;


import todolist.data.StoreData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StoreData store = StoreData.instOf();
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        Boolean done = !Boolean.valueOf(req.getParameter("done"));
        store.update(Integer.valueOf(id), done);
    }

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        StoreData store = new StoreData();
//        resp.setContentType("text/plain");
//        resp.setCharacterEncoding("UTF-8");
//        String id = req.getParameter("id");
//        Boolean done = Boolean.valueOf(req.getParameter("done"));
//        store.update(Integer.valueOf(id), done);;
//    }
}