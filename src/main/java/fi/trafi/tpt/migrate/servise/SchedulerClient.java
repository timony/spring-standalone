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
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class SchedulerClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ExecutionLogService logService;

    @Value("${scheduler.addTaskUri}")
    String addTaskUri;

    @Value("${eUrl}")
    String schedulerUrl;

    public void createTask(Task task) {
        log.info("Creating {}", task);

        if (logService.alreadyAdded(task)) {
            log.info("{} alrady added to scheduler, skipping", task);
            return;
        }

        UriComponents uri =UriComponentsBuilder.fromHttpUrl(schedulerUrl)
                .path(addTaskUri)
                .build();

        HttpEntity<Task> request = new HttpEntity<>(task);
        ResponseEntity<Task> response =
                restTemplate.postForEntity(uri.toUriString(), request, Task.class);

        if (response.getStatusCode().equals(HttpStatus.CREATED)) {
            logService.logSuccess(task, response.getStatusCode().toString());
        } else {
            logService.logFailure(task, response.toString());
        }
        log.info("Done");
    }
}
