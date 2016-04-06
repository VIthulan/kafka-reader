import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class KafkaConsumer implements Runnable{
    private KafkaStream stream;
    private int threadNumber;
    private TCPClient tcpClient;
    private static final Log log = LogFactory.getLog(KafkaConsumer.class);
    public KafkaConsumer (KafkaStream stream, int threadNumber, TCPClient tcpClient){
        this.stream = stream;
        this.threadNumber = threadNumber;
        this.tcpClient = tcpClient;
    }
    @Override
    public void run() {
        //System.out.println("Thread "+threadNumber+" in");
        ConsumerIterator<byte [], byte []> consumerIterator = stream.iterator();
       // System.out.println("ConsumerIterator Size: "+ consumerIterator.size());
        while(consumerIterator.hasNext()){
            String message = new String(consumerIterator.next().message());
            log.info("Message received in thread "+threadNumber+" : "+message);
            tcpClient.sendMessage(message);

        }
        log.debug("Shutting down thread "+threadNumber);
        //System.out.println("Shutting down thread "+threadNumber);
    }
}
