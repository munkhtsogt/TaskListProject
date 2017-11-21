package utility;

import model.Task;
import model.User;

import java.util.ArrayList;

/** utility class to return mock data for testing
 *  @since 11/19/2017
 *  @author kl */
public class MockData {

    public ArrayList<Task> taskList = new ArrayList<>();


    public ArrayList<Task> retrieveTaskList() {

        User user = new User();
        user.setId(100);
        user.setUsername("Test user");
        user.setEmail("user@mail.com");

        Task task1 = new Task("first task", "2017-11-19", "Personal");
        task1.setUser(user);
        Task task2 = new Task("second task", "2017-11-23", "Work");
        task2.setUser(user);
        Task task3 = new Task("third task", "2017-12-19", "Work");
        task3.setUser(user);
        taskList.add(task1);
        taskList.add(task2);
        taskList.add(task3);

        return taskList;
    }

}


