import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient {

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

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage (String message){
        try {
            OutputStream outputStream = socket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(message);
            bufferedWriter.flush();

            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String serverMessage = bufferedReader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
