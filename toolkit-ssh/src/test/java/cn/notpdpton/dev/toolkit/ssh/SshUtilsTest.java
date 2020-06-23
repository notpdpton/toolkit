package cn.notpdpton.dev.toolkit.ssh;

import cn.notpdpton.dev.toolkit.ssh.exception.*;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.shell.ProcessShellCommandFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

class SshUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SshUtilsTest.class);
    private static final String HOST = "localhost";
    private static final int PORT = 22;
    private static final String USERNAME = "unit-test";
    private static final String PASSWORD = "unit-test";
    private static SshServer SSH_SERVER;

    @BeforeAll
    static void beforeAll() throws IOException {
        SSH_SERVER = SshServer.setUpDefaultServer();
        SSH_SERVER.setPort(22);
//        SSH_SERVER.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));
//        SSH_SERVER.setFileSystemFactory(new VirtualFileSystemFactory(new File(System.getProperty("user.home")).toPath()));
        SSH_SERVER.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
        SSH_SERVER.setCommandFactory(new ProcessShellCommandFactory());
        SSH_SERVER.setPasswordAuthenticator((username, password, session) -> USERNAME.equals(username) && PASSWORD.equals(password));
        SSH_SERVER.start();
    }

    @AfterAll
    static void afterAll() throws IOException {
        SSH_SERVER.stop();
    }

    @Test
    void login() throws SshClientNotConnectedException, SshClientNotAuthenticatedException, SshClientNotFoundException, SshClientNotDisconnectedException {
        UUID uuid = SshUtils.login(HOST, PORT, USERNAME, PASSWORD, new PromiscuousVerifier());
        Assertions.assertNotNull(uuid);
        SshUtils.logout(uuid);
    }

    @Test
    void execute() throws SshClientNotAuthenticatedException, SshClientNotConnectedException,
            SshClientNotFoundException, SshClientNotTransportedException, SshClientNotDisconnectedException {
        UUID uuid = SshUtils.login(HOST, PORT, USERNAME, PASSWORD, new PromiscuousVerifier());
        final String command = "ping bing.com";
        String message = SshUtils.execute(uuid, command, 30, TimeUnit.SECONDS);
        Assertions.assertNotEquals("", message);
        LOGGER.debug("Response message for \"{}\":\n{}", command, message);
        SshUtils.logout(uuid);
    }
}