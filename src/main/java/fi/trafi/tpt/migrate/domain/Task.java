package fi.trafi.tpt.migrate.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Task {

    String groupName;
    String jobName;
    List<String> cronExpressions;
    String urlToExecute;

}
