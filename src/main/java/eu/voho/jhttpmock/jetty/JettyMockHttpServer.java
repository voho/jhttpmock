package eu.voho.jhttpmock.jetty;

import eu.voho.jhttpmock.base.AbstractMockHttpServer;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JettyMockHttpServer extends AbstractMockHttpServer {
    private final int port;
    private final Server server;

    public JettyMockHttpServer(int port) {
        this.port = port;
        this.server = new Server(port);

        this.server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
                if (handleByMock(httpServletRequest, httpServletResponse)) {
                    request.setHandled(true);
                }
            }
        });

        try {
            this.server.start();
        } catch (Exception e) {
            throw new RuntimeException("Cannot start Jetty server.", e);
        }
    }

    public int getPort() {
        return port;
    }

    public void close() throws IOException {
        try {
            this.server.stop();
        } catch (Exception e) {
            throw new IOException("Cannot stop Jetty server.");
        }
    }
}
