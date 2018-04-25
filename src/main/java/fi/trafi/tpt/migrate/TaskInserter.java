package fi.trafi.tpt.migrate;

import fi.trafi.tpt.migrate.domain.Task;
import fi.trafi.tpt.migrate.servise.SchedulerClient;
import fi.trafi.tpt.migrate.servise.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import java.io.File;
import java.util.List;

@SpringBootApplication
@Slf4j
public class TaskInserter implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TaskInserter.class, args);
    }

    @Autowired
    TaskService service;

    @Autowired
    SchedulerClient schedulerClient;

    @Autowired
    AnnotationConfigApplicationContext context;

    @Autowired
    Environment environment;

    private File file;

    @Override
    public void run(String... args) {
        log.info("..............STARTING...............");

        PropertySource propertySource = new SimpleCommandLinePropertySource(args);
        context.getEnvironment().getPropertySources().addFirst(propertySource);

        file = new File(environment.getProperty("eFile"));
        List<Task> tasks = service.readFile(file);

        tasks.stream()
                .forEach((task) -> schedulerClient.createTask(task));

        log.info("..............FINISHING...............");
        System.exit(0);
    }

    public File getFile() {
        return file;
    }
}
