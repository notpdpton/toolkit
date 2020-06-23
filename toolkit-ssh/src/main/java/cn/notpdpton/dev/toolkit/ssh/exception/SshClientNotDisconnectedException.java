package cn.notpdpton.dev.toolkit.ssh.exception;

/**
 * @author notpdpton
 * @date 2020/6/22 14:42
 */
public class SshClientNotDisconnectedException extends Exception {
    public SshClientNotDisconnectedException(String message) {
        super(message);
    }

    public SshClientNotDisconnectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SshClientNotDisconnectedException(Throwable cause) {
        super(cause);
    }

    protected SshClientNotDisconnectedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
