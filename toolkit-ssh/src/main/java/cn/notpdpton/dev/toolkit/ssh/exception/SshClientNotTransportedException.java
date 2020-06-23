package cn.notpdpton.dev.toolkit.ssh.exception;

/**
 * @author notpdpton
 * @date 2020/6/22 14:42
 */
public class SshClientNotTransportedException extends Exception {
    public SshClientNotTransportedException(String message) {
        super(message);
    }

    public SshClientNotTransportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SshClientNotTransportedException(Throwable cause) {
        super(cause);
    }

    protected SshClientNotTransportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
