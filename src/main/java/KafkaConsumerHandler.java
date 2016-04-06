import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class KafkaConsumerHandler {

    /**
     * These values are configurable
     */
    private static final String zookeeper_host = "localhost:2181";
    private static final String group_id_name = "groupid11";
    private static final int threads_number = 1;
    private static final String topic = "testtopic";

    private static final String zookeeper_session_time_out = "400";
    private static final String zookeeper_sync_time_out = "200";
    private static final String commit_interval = "1000";

    private static final String tcp_host = "localhost";
    private static final int tcp_port = 5000;

    public static void main(String[] args) {
        List<String> topics = new ArrayList<String>();
        topics.add(topic);
        //Setting up properties
        KafkaProperties kafkaProperties = new KafkaProperties(zookeeper_host, group_id_name, zookeeper_session_time_out,
                zookeeper_sync_time_out, commit_interval);

        //Initiated properties file, Can add other properties if you want
        Properties properties = kafkaProperties.getProperties();

        KafkaMessageListner kafkaMessageListner = new KafkaMessageListner();

        //Initializing Zookeeper configurations
        kafkaMessageListner.initZooKeeper(zookeeper_session_time_out, zookeeper_sync_time_out, commit_interval);

        //Initializing TCP client
        //Make sure host:tcp_port is open and listening
        kafkaMessageListner.initTCPClient(tcp_host, tcp_port);

        //Initializing kafka consumer
        kafkaMessageListner.init(properties, topics);

        //start consuming
        try {
            kafkaMessageListner.start(threads_number);
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
