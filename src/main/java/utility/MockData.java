package utility;

import model.Task;

import java.util.ArrayList;

/** utility class to return mock data for testing
 *  @since 11/19/2017
 *  @author kl */
public class MockData {

    public ArrayList<Task> taskList = new ArrayList<>();


    public ArrayList<Task> retrieveTaskList() {

        taskList.add(new Task("first task", "2017-11-19", "Personal"));
        taskList.add(new Task("second task", "2017-11-23", "Work"));
        taskList.add(new Task("third task", "2017-12-19", "Work"));

        return taskList;
    }

}


