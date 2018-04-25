package fi.trafi.tpt.migrate.servise;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.trafi.tpt.migrate.domain.Task;
import fi.trafi.tpt.migrate.exception.MigrateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class TaskService {

    @Autowired
    ObjectMapper objectMapper;

    public List<Task> readFile(File file) {
        log.info("Reading file {}", file);
        try {
            List<Task> tasks = objectMapper.readValue(file, new TypeReference<List<Task>>() {});
            log.info("File {} reading finished, {} tasks found", file, tasks.size());
            return tasks;
        } catch (IOException e) {
            throw new MigrateException("Cannot read file", e);
        }
    }


}
