import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient {

    private static final Log log = LogFactory.getLog(TCPClient.class);
    private Socket socket;
    private String host;
    private int port;

    public TCPClient(String host, int port) {
        this.port = port;
        this.host = host;
        init();
    }

    /**
     * Initiating TCP socket for message transport
     */
    private void init() {
        try {
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
            log.info("TCP client successfully initiated at " + host + ":" + port);
        } catch (UnknownHostException e) {
            log.error("Error while accessing host " + e.getMessage());
        } catch (IOException e) {
            log.error("Error while initiating TCP client " + e.getMessage());
        }
    }

    /**
     * Send message to TCP server
     *
     * @param message message that wants to be sent
     */
    public void sendMessage(String message) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(message + "\n");
            log.info("Message " + message + " is sent to " + host + ":" + port);
            bufferedWriter.flush();

            /**
             * Uncomment this field only if the server is sending back any responses, otherwise it will wait
             * until a message comes.
             */
            //serverMessage();

        } catch (IOException e) {
            log.error("Error while sending message to server " + e.getMessage());
        }

    }

    /**
     * Reads the message from server
     */
    public void serverMessage() {
        try {
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String serverMessage = bufferedReader.readLine();
            log.info("Server message from " + host + ":" + port + " : " + serverMessage);
        } catch (IOException e) {
            log.error("Error while reading message from server " + e.getMessage());
        }

    }
}
