import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

public class KafkaConsumer implements Runnable{
    private KafkaStream stream;
    private int threadNumber;
    private TCPClient tcpClient;

    public KafkaConsumer (KafkaStream stream, int threadNumber, TCPClient tcpClient){
        this.stream = stream;
        this.threadNumber = threadNumber;
        this.tcpClient = tcpClient;
    }
    @Override
    public void run() {
        System.out.println("Thread "+threadNumber+" in");
        ConsumerIterator<byte [], byte []> consumerIterator = stream.iterator();
        System.out.println("ConsumerIterator Size: "+ consumerIterator.size());
        while(consumerIterator.hasNext()){
            String message = new String(consumerIterator.next().message());
            tcpClient.sendMessage(message);
            System.out.println("Thread " + threadNumber + ": " + message);
        }
        System.out.println("Shutting down thread "+threadNumber);
    }
}
