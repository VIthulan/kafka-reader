import java.util.ArrayList;
import java.util.List;

public class KafkaConsumerHandler {

    private static final String zookeeper_host= "localhost:2181";
    private static final String group_id_name = "groupid11";
    private static final int threads_number = 1;
    private static final String topic = "testtopic";

    private static final String zookeeper_session_time_out = "400";
    private static final String zookeeper_sync_time_out = "200";
    private static final String commit_interval = "1000";

    private static final String tcp_host = "localhost";
    private static final int tcp_port = 6671;

    public static void main(String[] args) {
        List<String> topics = new ArrayList<String>();
        topics.add(topic);
        KafkaMessageListner kafkaMessageListner = new KafkaMessageListner();
        kafkaMessageListner.initZooKeeper(zookeeper_session_time_out,zookeeper_sync_time_out,commit_interval);
        kafkaMessageListner.init(zookeeper_host,group_id_name,threads_number,topics);

        kafkaMessageListner.initTCPClient(tcp_host,tcp_port);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
