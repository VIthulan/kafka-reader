import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

public class KafkaConsumer implements Runnable{
    private KafkaStream stream;
    private int threadNumber;

    public KafkaConsumer (KafkaStream stream, int threadNumber){
        this.stream = stream;
        this.threadNumber = threadNumber;
    }
    @Override
    public void run() {
        System.out.println("Thread "+threadNumber+" in");
        ConsumerIterator<byte [], byte []> consumerIterator = stream.iterator();
        System.out.println("ConsumerIterator Size: "+ consumerIterator.size());
        while(consumerIterator.hasNext()){
            System.out.println("Thread " + threadNumber + ": " + new String(consumerIterator.next().message()));
        }
        System.out.println("Shutting down thread "+threadNumber);
    }
}
