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
    private String name;
    private String dueDate;
    private String category;
    private PRIORITY priority;

    public Task(){
        this.user = null;
    }

    public Task(String name, String dueDate, String category){
        this.name = name;
        this.dueDate = dueDate;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public PRIORITY getPriority() {
        return priority;
    }

    public void setPriority(PRIORITY priority) {
        this.priority = priority;
    }
}
