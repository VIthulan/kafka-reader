import kafka.consumer.Consumer;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class KafkaMessageListner extends AbstractKafkaMessageListner {

    private KafkaProperties kafkaProperties;
    private ExecutorService executor;


    private final String zookeeper_session_time_out = "400";
    private final String zookeeper_sync_time_out = "200";
    private final String commit_interval = "1000";


    @Override
    public void init(String zookeeper_host, String group_id_name, int threadsCount, List<String> topic) {
        kafkaProperties = new KafkaProperties(zookeeper_host,group_id_name,zookeeper_session_time_out,
                zookeeper_sync_time_out,commit_interval);
        this.topics=topic;
        try {
            createKafkaConnector(threadsCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean createKafkaConnector(int threadsCount) throws Exception {
       // consumerConnector = null;
        boolean isCreated = false;
        try {
            if (consumerConnector == null) {
                consumerConnector = Consumer.createJavaConsumerConnector(kafkaProperties.getConsumerConfig());
                System.out.println("Consumer connector created");
                start(threadsCount);
            }
            isCreated = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return isCreated;
    }

    @Override
    public void start(int threadsCount) throws Exception {
        this.threadCount = threadsCount;
        try{
            Map<String,Integer> topicCount = new HashMap<String,Integer>();
            if(topics!=null && topics.size()>0) {
                System.out.println("topics count "+topics.size());
                for(String topic : topics){
                    topicCount.put(topic,threadCount);
                }
                Map<String,List<KafkaStream<byte[], byte[]>>> consumerStreams = consumerConnector
                        .createMessageStreams(topicCount);
                consumerIteraror = new ArrayList<ConsumerIterator<byte[], byte[]>>();
                List<KafkaStream<byte[], byte[]>> streams = consumerStreams
                        .get(topics.get(0));
                System.out.println("topic "+topics.get(0));
                executor = Executors.newFixedThreadPool(threadCount);

                /*for(String topic : topics) {
                    List<KafkaStream<byte[], byte[]>> streams = consumerStreams
                            .get(topic);
                    startConsumers(streams);
                }*/
                int threadNumber = 0;
                for (final KafkaStream stream : streams) {
                    System.out.println("Thread number "+threadNumber);
                    executor.submit(new KafkaConsumer(stream, threadNumber));
                    threadNumber++;
                }
            }
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    protected void startConsumers(List<KafkaStream<byte[], byte[]>> streams) {
        if (streams.size() >= 1) {
            consumerIteraror.add(streams.get(0).iterator());
        }
    }



}
