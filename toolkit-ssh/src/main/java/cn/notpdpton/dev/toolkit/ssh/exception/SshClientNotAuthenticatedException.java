package cn.notpdpton.dev.toolkit.ssh.exception;

/**
 * @author notpdpton
 * @date 2020/6/22 10:53
 */
public class SshClientNotAuthenticatedException extends Exception {
    public SshClientNotAuthenticatedException(String message) {
        super(message);
    }

    public SshClientNotAuthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SshClientNotAuthenticatedException(Throwable cause) {
        super(cause);
    }

    protected SshClientNotAuthenticatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
