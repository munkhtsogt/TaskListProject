package model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    private long id;
    @OneToOne
    private User user;
    private String task;
    private String requiredBy;
    private String category;
    private Boolean complete;
    private String priority;

    public Task(){
        this.user = null;
        this.complete = false;
    }

    public Task(String task, String requiredBy, String category){
        this.task = task;
        this.requiredBy = requiredBy;
        this.category = category;
        this.user = null;
        this.complete = false;
    }

    public long getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getRequiredBy() {
        return requiredBy;
    }

    public void setRequiredBy(String dueDate) {
        this.requiredBy = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean completed) {
        complete = completed;
    }
}
