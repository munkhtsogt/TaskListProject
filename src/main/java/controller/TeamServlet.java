package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Team;
import model.User;
import org.hibernate.Session;
import utility.DbSession;
import utility.HibernateProxyTypeAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/TeamServlet")
public class TeamServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = DbSession.INSTANCE.getInstance();

        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = gb.create();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        String method = request.getParameter("method");
        if(method.equals("create")){
            String teamId = request.getParameter("teamId");
            String name = request.getParameter("name");
            String[] userIds = request.getParameterValues("userIds[]");
            List<Long> usersIds = Arrays.stream(userIds)
                    .map(x -> Long.valueOf(x)).collect(Collectors.toList());

            try{
                Team team;
                if(teamId != null && !teamId.equals("")){
                    team = session.load(Team.class, Long.valueOf(teamId));
                }
                else {
                    team = new Team();
                }
                team.setName(name);

                List<User> users= new ArrayList<>();
                for(long userId: usersIds){
                    User user = session.load(User.class, userId);
                    users.add(user);
                }
                team.setUsers(users);
                session.beginTransaction();
                session.save(team);
                session.getTransaction().commit();
                out.write(gson.toJson(team));

            } catch(Exception e){
                out.write(gson.toJson("error"));
                e.printStackTrace();
            }
        }
        else if(method.equals("delete")){
            Long teamId = Long.valueOf(request.getParameter("teamId"));
            try{
                Team team = session.load(Team.class, teamId);
                session.beginTransaction();
                session.delete(team);
                session.getTransaction().commit();
                out.write(gson.toJson("success"));

            } catch(Exception e){
                out.write(gson.toJson("error"));
                e.printStackTrace();

            }
        }
        else if(method.equals("findById")){
            Long teamId = Long.valueOf(request.getParameter("teamId"));
            try {
                Team team = session.load(Team.class, teamId);
                out.write(gson.toJson(team));
            }  catch(Exception e){
                out.write(gson.toJson("error"));
                e.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
