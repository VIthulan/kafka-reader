import kafka.consumer.ConsumerIterator;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.List;

public abstract class AbstractKafkaMessageListner {

    protected int threadCount;
    protected List<String> topics;
    protected ConsumerConnector consumerConnector;
    protected List<ConsumerIterator<byte[], byte[]>> consumerIteraror;

    public abstract void init(String zookeeper_host, String group_id_name, int threadsCount, List<String> topics);
    public abstract boolean createKafkaConnector (int threadsCount) throws Exception;

    public abstract void start(int threadCount) throws Exception;

    public void destroy(){}

    public abstract boolean hasNext();

    public boolean hasMultipleTopicsToConsume(){
        return false;
    }

    public void consumeMultipleTopics (String name){

    }
}
