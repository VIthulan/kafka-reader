import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class KafkaMessageListner extends AbstractKafkaMessageListner {
    private static final Log log = LogFactory.getLog(KafkaMessageListner.class);
    private Properties properties;
    private ExecutorService executor;

    /**
     * These are default values, it can be configured in main method
     */
    private String zookeeper_session_time_out = "400";
    private String zookeeper_sync_time_out = "200";
    private String commit_interval = "1000";

    private TCPClient tcpClient;


    @Override
    public void init(Properties properties, List<String> topic) {
        this.properties = properties;
        log.info("Kafka consumer properties are set successfully");
        this.topics = topic;
    }

    /**
     * Setting up zookeeper configurations
     *
     * @param zookeeper_session_time_out Zookeeper session timeout. If the consumer fails to heartbeat to zookeeper
     *                                   for this period of time it is considered dead and a rebalance will occur.
     * @param zookeeper_sync_time_out    How far a ZK follower can be behind a ZK leader
     * @param commit_interval            The frequency in ms that the consumer offsets are committed to zookeeper.
     */
    public void initZooKeeper(String zookeeper_session_time_out, String zookeeper_sync_time_out,
                              String commit_interval) {
        this.zookeeper_session_time_out = zookeeper_session_time_out;
        this.zookeeper_sync_time_out = zookeeper_sync_time_out;
        this.commit_interval = commit_interval;
    }

    /**
     * Initializing TCP client
     *
     * @param host Host that TCP listener is running
     * @param port Port number
     */
    public void initTCPClient(String host, int port) {
        tcpClient = new TCPClient(host, port);
    }

    /**
     * Creating consumer connector
     *
     * @param threadsCount number of threads to run
     * @return true if connector created successfully
     * @throws Exception
     */
    @Override
    public boolean createKafkaConnector(int threadsCount) throws Exception {
        boolean isCreated = false;
        try {
            if (consumerConnector == null) {
                consumerConnector = Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
                log.info("Consumer connector created successfully");
                start(threadsCount);
            }
            isCreated = true;
        } catch (Exception e) {
            log.error("Error while creating consumer connector " + e.getMessage());
        }
        return isCreated;
    }

    /**
     * Start the consumer threads
     *
     * @param threadsCount number of threads to run
     * @throws Exception
     */
    @Override
    public void start(int threadsCount) throws Exception {
        try {
            createKafkaConnector(threadsCount);
        } catch (Exception e) {
            log.error("Error while creating consumer connector " + e.getMessage());
        }
        this.threadCount = threadsCount;
        try {
            Map<String, Integer> topicCount = new HashMap<String, Integer>();
            if (topics != null && topics.size() > 0) {
                System.out.println("topics count " + topics.size());
                for (String topic : topics) {
                    topicCount.put(topic, threadCount);
                }
                Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumerConnector
                        .createMessageStreams(topicCount);
                consumerIteraror = new ArrayList<ConsumerIterator<byte[], byte[]>>();
                List<KafkaStream<byte[], byte[]>> streams = consumerStreams
                        .get(topics.get(0));
                executor = Executors.newFixedThreadPool(threadCount);
                log.info("Thread pool with " + threadsCount + " thread/s is initiated");
                int threadNumber = 0;
                for (final KafkaStream stream : streams) {
                    System.out.println("Thread number " + threadNumber);
                    executor.submit(new KafkaConsumer(stream, threadNumber, tcpClient));
                    threadNumber++;
                }
            }
        } catch (Exception e) {
            log.error("Error while starting the consumer " + e.getMessage());
        }
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    /**
     * This implementation is for multiple topics
     *
     * @param streams
     */
    protected void startConsumers(List<KafkaStream<byte[], byte[]>> streams) {
        if (streams.size() >= 1) {
            consumerIteraror.add(streams.get(0).iterator());
        }
    }


}
