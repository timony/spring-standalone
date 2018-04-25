package fi.trafi.tpt.migrate.servise;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.trafi.tpt.migrate.domain.Task;
import fi.trafi.tpt.migrate.domain.TaskExecutionLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Scope("singleton")
@Slf4j
public class ExecutionLogService {

    @Value("${migration.executionLog.file.postfix}")
    String logPostFix;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    Environment environment;

    private List<TaskExecutionLog> executionLogs;
    private File logFile;

    @PostConstruct
    public void postConstruct() {
        File file = new File(environment.getProperty("eFile"));
        log.info("Setting up Exection log service {}", file);
        getLogFile(file);
        readLog(logFile);
    }

    @PreDestroy
    public void preDestroy() {
        log.info("Writing logs to {}", logFile);
        try {
            objectMapper.writeValue(logFile, executionLogs);
        } catch (IOException e) {
            log.error("Cannot write to execution file", e);
        }
    }

    public boolean alreadyAdded(Task task) {
        return executionLogs.stream()
                .anyMatch(log ->
                        log.getGroup().equals(task.getGroupName())
                                &&
                                log.getTask().equals(task.getJobName())
                                &&
                                log.isSuccessful()
                );

    }

    public void logSuccess(Task task, String message) {
        Optional<TaskExecutionLog> first = find(task);
        if(first.isPresent()) {
            first.get().setSuccessful(true);
            first.get().setMessage(message);
        } else {
            TaskExecutionLog log = TaskExecutionLog.builder()
                    .group(task.getGroupName())
                    .task(task.getJobName())
                    .successful(true)
                    .message(message)
                    .build();
            executionLogs.add(log);
        }
    }

    public void logFailure(Task task, String message) {
        Optional<TaskExecutionLog> first = find(task);
        if (first.isPresent()) {
            first.get().setSuccessful(false);
            first.get().setMessage(message);
        } else {
            TaskExecutionLog log = TaskExecutionLog.builder()
                    .group(task.getGroupName())
                    .task(task.getJobName())
                    .successful(false)
                    .message(message)
                    .build();
            executionLogs.add(log);
        }
    }

    private Optional<TaskExecutionLog> find(Task task) {
        return executionLogs.stream()
                .filter(theLog ->
                        theLog.getGroup().equals(task.getGroupName())
                                &&
                                theLog.getTask().equals(task.getJobName())
                )
                .findFirst();
    }

    private void readLog(File executionLog) {
        try {
            executionLogs = objectMapper
                    .readValue(executionLog, new TypeReference<List<TaskExecutionLog>>() {
                    });
        } catch (IOException e) {
            executionLogs = new ArrayList<>();
        }
    }

    private void getLogFile(File file) {
        UriComponentsBuilder path = UriComponentsBuilder.fromPath(file.getParent())
                .path(FilenameUtils.removeExtension(file.getName()) + logPostFix + FilenameUtils.getExtension(file.getName()));

        String logFileName = String.format("%s_%s.%s",
                FilenameUtils.removeExtension(file.getName()),
                logPostFix,
                FilenameUtils.getExtension(file.getName())
                );

        logFile = new File(file.getParentFile(), logFileName);
    }

}
