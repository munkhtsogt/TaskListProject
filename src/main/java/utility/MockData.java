package utility;

import model.PRIORITY;
import model.Task;
import model.User;

import java.util.ArrayList;

/** utility class to return mock data for testing
 *  @since 11/19/2017
 *  @author kl */
public class MockData {

    public ArrayList<Task> taskList = new ArrayList<>();


    public ArrayList<Task> retrieveTaskList() {

        User user1 = new User(1, "Mogi", "mtsogbadrakh@mum.edu");
        User user2 = new User(2, "Gunde", "mtsogbadrakh@mum.edu");
        User user3 = new User(3, "Shree", "mtsogbadrakh@mum.edu");
        taskList.add(new Task(101, user1,"first task", "2017-11-19", "Personal", PRIORITY.LOW));
        taskList.add(new Task(102, user2,"second task", "2017-11-23", "Work", PRIORITY.LOW));
        taskList.add(new Task(103, user3,"third task", "2017-12-19", "Work", PRIORITY.LOW));

        return taskList;
    }

}


