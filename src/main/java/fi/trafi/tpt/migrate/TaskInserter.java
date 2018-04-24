package fi.trafi.tpt.migrate;

import fi.trafi.tpt.migrate.domain.Task;
import fi.trafi.tpt.migrate.servise.SchedulerClient;
import fi.trafi.tpt.migrate.servise.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.List;

@SpringBootApplication
public class TaskInserter implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TaskInserter.class, args);
    }

    @Autowired
    TaskService service;

    @Autowired
    SchedulerClient schedulerClient;

    @Override
    public void run(String... args) {
        File file = new File(args[0]);
        List<Task> tasks = service.readFile(file);

        tasks.stream()
                .forEach((task) -> schedulerClient.createTask(task));

        System.exit(0);
    }
}
