import java.util.ArrayList;
import java.util.List;

public class KafkaConsumerHandler {

    private static final String zookeeper_host= "localhost:2181";
    private static final String group_id_name = "groupid11";
    private static final int threads_number = 1;

    public static void main(String[] args) {
        List<String> topics = new ArrayList<String>();
        topics.add("testtopic");
        KafkaMessageListner kafkaMessageListner = new KafkaMessageListner();
        kafkaMessageListner.init(zookeeper_host,group_id_name,threads_number,topics);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
