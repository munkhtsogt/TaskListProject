package controller;

import com.google.gson.Gson;
import model.Task;
import model.Team;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utility.HibernateUtil;
import utility.MockData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/TaskServlet")
public class TaskServlet extends HttpServlet {

    @Override
    public void init(){
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try {
            Session session = factory.openSession();
            User user1 = new User();
            user1.setUsername("Mogi");
            user1.setEmail("mtsogbadrakh@mum.edu");

            User user2 = new User();
            user2.setUsername("Gunde");
            user2.setEmail("gbadam@mum.edu");

            User user3 = new User();
            user3.setUsername("Shreeram");
            user3.setEmail("shreeramchaulagain@gmail.com");

            session.save(user1);
            session.save(user2);
            session.save(user3);
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String JSONtasks;
        List<Task> taskList = new MockData().retrieveTaskList();
        JSONtasks = new Gson().toJson(taskList);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.write(JSONtasks);
    }
}
