package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private long id;
    private String username;
    private String email;
    private List<Team> teams;

    public User(long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.teams = new ArrayList<>();
    }

    public void addTeam(Team team){
        this.teams.add(team);
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
