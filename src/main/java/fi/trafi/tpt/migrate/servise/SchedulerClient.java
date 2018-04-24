package fi.trafi.tpt.migrate.servise;

import fi.trafi.tpt.migrate.domain.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class SchedulerClient {

    @Autowired
    RestTemplate restTemplate;

    @Value("scheduler.addTaskUri")
    String addTaskUri;

    public void createTask(Task task) {
        log.info("Creating task group:{} name:{}", task.getGroupName(), task.getJobName());

        HttpEntity<Task> request = new HttpEntity<>(task);
        ResponseEntity<Task> response =
                restTemplate.postForEntity("http://localhost:8081/tpt-scheduler/api/v2/task", request, Task.class);

        if (response.getStatusCode().equals(HttpStatus.CREATED)) {
            log.info("Successfully created taks group:{} name:{}", task.getGroupName(), task.getJobName());
        } else {
            log.error("Cannot create created taks group:{} name:{}", task.getGroupName(), task.getJobName());
        }
    }
}
