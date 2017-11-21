package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Task;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utility.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet("/TaskServlet")
public class TaskServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = DbSession.INSTANCE.getInstance();

        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = gb.create();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        String method = request.getParameter("method");
        if(method.equals("init")){
            try{
                List<User> users = session.createQuery("from User").list();
                List<Task> tasks = session.createQuery("from Task").list();
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
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("tasks", tasks);
                map.put("users", users);
                out.write(gson.toJson(map));
            } catch (Exception e){
                out.write(gson.toJson("error"));
                e.printStackTrace();
            }
        }
        else if(method.equals("create")){

            String taskId = request.getParameter("taskId");
            String name = request.getParameter("task");
            Long userId = Long.valueOf(request.getParameter("userId"));
            String category = request.getParameter("category");
            String requiredBy = request.getParameter("requiredBy");
            String priority = request.getParameter("priority");
            Task task;
            if(taskId != null && !taskId.equals("")){
                task = session.load(Task.class, Long.valueOf(taskId));
            }
            else {
                task = new Task();
            }
            task.setTask(name);
            task.setRequiredBy(requiredBy);
            task.setCategory(category);
            task.setPriority(priority);
            try{

                User user = session.load(User.class, userId);
                task.setUser(user);

                session.beginTransaction();
                session.save(task);
                session.getTransaction().commit();
                out.write(gson.toJson(task));

            }catch(Exception e) {
                out.write(gson.toJson("error"));
                e.printStackTrace();
            }
        }
        else if(method.equals("delete")){
            Long taskId = Long.valueOf(request.getParameter("taskId"));
            try{
                Task task = session.load(Task.class, taskId);
                session.beginTransaction();
                session.delete(task);
                session.getTransaction().commit();
                out.write(gson.toJson("success"));

            } catch(Exception e){
                out.write(gson.toJson("error"));
                e.printStackTrace();

            }
        }
        else if(method.equals("complete")){
            Long taskId = Long.valueOf(request.getParameter("taskId"));
            try{
                Task task = session.load(Task.class, taskId);
                task.setComplete(true);
                session.beginTransaction();
                session.update(task);
                session.getTransaction().commit();
                out.write(gson.toJson(task));

            } catch(Exception e){
                out.write(gson.toJson("error"));
                e.printStackTrace();
            }
        }
        else if(method.equals("findById")){
            Long taskId = Long.valueOf(request.getParameter("taskId"));
            try{
                Task task = session.load(Task.class, taskId);
                out.write(gson.toJson(task));

            } catch(Exception e){
                out.write(gson.toJson("error"));
                e.printStackTrace();
            }
        }
        else if(method.equals("loadTasks")){
            String attribute = request.getParameter("attribute");
            int priority = Integer.valueOf(request.getParameter("priority"));
            int user = Integer.valueOf(request.getParameter("user"));
            int requiredBy = Integer.valueOf(request.getParameter("requiredBy"));

            List<Task> tasks = session.createQuery("from Task").list();

            if(priority != 0){
                if(priority % 2 == 0){
                    Collections.sort(tasks, (Task a, Task b) -> a.getPriority().compareTo(b.getPriority()));
                }
                else {
                    Collections.sort(tasks, (Task a, Task b) -> b.getPriority().compareTo(a.getPriority()));
                }
            }

            if(user != 0){
                if(user % 2 == 0){
                    Collections.sort(tasks, (Task a, Task b) -> a.getUser().getUsername().compareTo(b.getUser().getUsername()));
                }
                else {
                    Collections.sort(tasks, (Task a, Task b) -> b.getUser().getUsername().compareTo(a.getUser().getUsername()));
                }
            }
            if(requiredBy != 0){
                if(requiredBy % 2 == 0){
                    Collections.sort(tasks, (a, b) -> a.getRequiredBy().compareTo(b.getRequiredBy()));
                }else{
                    Collections.sort(tasks, (a, b) -> b.getRequiredBy().compareTo(a.getRequiredBy()));
                }
            }

            out.write(gson.toJson(tasks));
        }
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
