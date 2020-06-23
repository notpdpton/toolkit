package cn.notpdpton.dev.toolkit.ssh.exception;

/**
 * @author notpdpton
 * @date 2020/6/22 10:53
 */
public class SshClientNotConnectedException extends Exception {
    public SshClientNotConnectedException(String message) {
        super(message);
    }

    public SshClientNotConnectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SshClientNotConnectedException(Throwable cause) {
        super(cause);
    }

    protected SshClientNotConnectedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
