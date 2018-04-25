package fi.trafi.tpt.migrate.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskExecutionLog {

    String group;
    String task;
    boolean successful;
    String message;
}
