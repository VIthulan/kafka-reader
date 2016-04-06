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

    public TCPClient(String host, int port){
        this.port = port;
        this.host = host;
        init();
    }

    private void init(){
        try {
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address,port);
            log.info("TCP client successfully initiated at "+host+":"+port);
        } catch (UnknownHostException e) {
            log.error("Error while accessing host "+e.getMessage());
            //e.printStackTrace();
        } catch (IOException e) {
            log.error("Error while initiating TCP client "+e.getMessage());
          //  e.printStackTrace();
        }
    }
    public void sendMessage (String message){
        try {
            OutputStream outputStream = socket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(message);
            log.info("Message "+message+" is sent to "+host+":"+port);
            bufferedWriter.flush();

            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String serverMessage = bufferedReader.readLine();
            log.info("Server message from "+host+":"+port+" : "+serverMessage);

        } catch (IOException e) {
            log.error("Error while sending message to server "+e.getMessage());
           // e.printStackTrace();
        }

    }
}
