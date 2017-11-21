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

        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        //taskList.add(new Task("first task", "2017-11-19", "Personal", PRIORITY.LOW));
        //taskList.add(new Task("second task", "2017-11-23", "Work", PRIORITY.LOW));
        //taskList.add(new Task("third task", "2017-12-19", "Work", PRIORITY.LOW));

        return taskList;
    }

}


