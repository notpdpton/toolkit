package cn.notpdpton.dev.toolkit.ssh;

import cn.notpdpton.dev.toolkit.ssh.exception.*;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * The utilities to execute command in server by SSH protocol.
 *
 * @author notpdpton
 * @date 2020/6/20 11:34
 */
public class SshUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(SshUtils.class);
    private static final Map<UUID, SSHClient> SSH_CLIENT_MAP = new Hashtable<>();

    /**
     * Login SSH server and cache the SSH Client instance.<br>
     * This method will return an UUID that mapped to the cached SSH Client instance.
     *
     * @param host     The host name
     * @param port     The host port
     * @param username The username to login
     * @param password The password of user
     * @return The UUID that mapped to the cached SSH client instance
     * @throws SshClientNotConnectedException     If connect to SSH server failed
     * @throws SshClientNotAuthenticatedException If authenticate by SSH server failed
     */
    public static @NotNull UUID login(String host, int port, String username, String password, HostKeyVerifier hostKeyVerifier) throws SshClientNotConnectedException, SshClientNotAuthenticatedException {
        SSHClient sshClient = new SSHClient();
        sshClient.addHostKeyVerifier(hostKeyVerifier);
        connect(sshClient, host, port);
        authenticate(sshClient, username, password);
        UUID uuid = UUID.randomUUID();
        SSH_CLIENT_MAP.put(uuid, sshClient);
        return uuid;
    }

    /**
     * Logout SSH server and remove the cached SSH Client instance.<br>
     *
     * @param uuid The UUID that mapped to the cached SSH client instance
     * @throws SshClientNotFoundException        If find the cached SSH client by UUID failed
     * @throws SshClientNotDisconnectedException If disconnect from SSH server failed
     */
    public static void logout(UUID uuid) throws SshClientNotFoundException, SshClientNotDisconnectedException {
        SSHClient sshClient = findCachedSshClient(uuid);
        try {
            sshClient.disconnect();
        } catch (IOException e) {
            String message = String.format("The cached SSH client(UUID: %s) disconnect from SSH server failed", uuid);
            throw new SshClientNotDisconnectedException(message);
        }
    }

    /**
     * Send and execute the command in SSH server.
     *
     * @param uuid    The UUID that mapped to the cached SSH client instance
     * @param command The command to execute
     * @return The response message of executed command
     * @throws SshClientNotFoundException       If find the cached SSH client by UUID failed
     * @throws SshClientNotConnectedException   If connect to SSH server failed
     * @throws SshClientNotTransportedException If transport with SSH server failed
     */
    public static @NotNull String execute(UUID uuid, String command, long timeOut, TimeUnit timeUnit) throws SshClientNotFoundException,
            SshClientNotConnectedException, SshClientNotTransportedException {
        SSHClient sshClient = findCachedSshClient(uuid);

        try (Session session = sshClient.startSession()) {
            final Session.Command cmd = session.exec(command);
            cmd.join(timeOut, timeUnit);
            return IOUtils.readFully(cmd.getInputStream()).toString();
        } catch (ConnectionException e) {
            String message = String.format("The cached SSH client(UUID: %s) connect to SSH server failed", uuid);
            throw new SshClientNotConnectedException(message);
        } catch (TransportException e) {
            String message = String.format("The cached SSH client(UUID: %s) transport with SSH server failed", uuid);
            throw new SshClientNotTransportedException(message);
        } catch (IOException e) {
            String message = String.format("The cached SSH client(UUID: %s) read response message failed", uuid);
            throw new SshClientNotTransportedException(message);
        }
    }

    /**
     * Using SSH client to connect target SSH server.
     *
     * @param sshClient The SSH client
     * @param host      The host name
     * @param port      The host port
     * @throws SshClientNotConnectedException If connect to SSH server failed
     */
    private static void connect(@NotNull SSHClient sshClient, String host, int port) throws SshClientNotConnectedException {
        try {
            sshClient.connect(host, port);
            LOGGER.debug("Connect to {}:{} succeed", host, port);
        } catch (IOException e) {
            String message = String.format("Connection target: %s:%d", host, port);
            throw new SshClientNotConnectedException(message, e);
        }
    }

    /**
     * Using SSH client to connect target SSH server.
     *
     * @param sshClient The SSH client
     * @param username  The username
     * @param password  The password
     * @throws SshClientNotAuthenticatedException If authenticate by SSH server failed
     */
    private static void authenticate(@NotNull SSHClient sshClient, String username, String password) throws SshClientNotAuthenticatedException {
        try {
            sshClient.authPassword(username, password);
            LOGGER.debug("Authenticate by password with {}:{} succeed", username, password);
        } catch (IOException e) {
            String message = String.format("Authentication information: %s:%s", username, password);
            throw new SshClientNotAuthenticatedException(message, e);
        }
    }

    /**
     * Find cached SSH client by UUID.
     *
     * @param uuid The UUID that mapped to the cached SSH client instance
     * @return The cached SSH client
     * @throws SshClientNotFoundException If find the cached SSH client by UUID failed
     */
    private static SSHClient findCachedSshClient(UUID uuid) throws SshClientNotFoundException {
        return Optional.ofNullable(SSH_CLIENT_MAP.get(uuid)).orElseThrow(() -> {
            String message = String.format("The cached SSH client is not found by UUID: %s", uuid);
            return new SshClientNotFoundException(message);
        });
    }
}
