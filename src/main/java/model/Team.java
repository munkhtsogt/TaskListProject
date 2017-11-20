package model;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private long id;
    private String name;
    private List<User> users;

    public Team(long id, String name) {
        this.id = id;
        this.name = name;
        users = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user){
        this.users.add(user);
    }
}
