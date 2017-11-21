package controller;

import com.google.gson.Gson;
import model.Task;
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
import java.util.List;

@WebServlet("/TaskServlet")
public class TaskServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = null;
        PrintWriter out = response.getWriter();
        String method = request.getParameter("method");
        if(method.equals("create")){
            String name = request.getParameter("task");
            Long userId = Long.valueOf(request.getParameter("userId"));
            String category = request.getParameter("category");
            String requiredBy = request.getParameter("requiredBy");
            String priority = request.getParameter("priority");

            Task task = new Task(name, requiredBy, category);
            task.setPriority(priority);
            try{
                SessionFactory factory = HibernateUtil.getSessionFactory();
                session = factory.openSession();

                User user = session.load(User.class, userId);
                task.setUser(user);

                session.save(task);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.write(new Gson().toJson(task));

            }catch(Exception e){
                e.printStackTrace();

            } finally {
                session.close();
            }
        }
        else if(method.equals("findUsers")){
            try{
                SessionFactory factory = HibernateUtil.getSessionFactory();
                session = factory.openSession();

                List<User> users = session.createQuery("from User").list();
                if(users.isEmpty()){
                    User user = new User();
                    user.setUsername("Mogi");
                    user.setEmail("mtsogbadrakh@mum.edu");

                    User user2 = new User();
                    user2.setUsername("Shreeram");
                    user2.setEmail("shchaulagain@mum.edu");

                    User user3 = new User();
                    user3.setUsername("Gunde");
                    user3.setEmail("gbadam@mum.edu");

                    users.add(user);
                    users.add(user2);
                    users.add(user3);

                    session.save(user);
                    session.save(user2);
                    session.save(user3);
                }

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.write(new Gson().toJson(users));
            }catch(Exception e){
                e.printStackTrace();

            } finally {
                session.close();
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = new User();
        user.setUsername("Mogi");
        user.setEmail("munkhuu48@gmail.com");

        PrintWriter out = response.getWriter();
        String JSONtasks;
        List<Task> taskList = new MockData().retrieveTaskList();
        JSONtasks = new Gson().toJson(taskList);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.write(JSONtasks);
    }
}
