package fi.trafi.tpt.migrate.exception;

public class MigrateException extends RuntimeException {

    public MigrateException(String message, Throwable e) {
        super(message, e);
    }
}
