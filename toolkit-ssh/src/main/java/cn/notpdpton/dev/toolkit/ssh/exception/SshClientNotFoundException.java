package cn.notpdpton.dev.toolkit.ssh.exception;

/**
 * @author notpdpton
 * @date 2020/6/22 12:47
 */
public class SshClientNotFoundException extends Exception {
    public SshClientNotFoundException(String message) {
        super(message);
    }

    public SshClientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SshClientNotFoundException(Throwable cause) {
        super(cause);
    }

    protected SshClientNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
