package fi.trafi.tpt.migrate.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@ToString(exclude = {"cronExpressions", "urlToExecute"})
public class Task {

    String groupName;
    String jobName;
    List<String> cronExpressions;
    String urlToExecute;

}
