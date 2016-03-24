package org.mp.es.util;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by MP on 24/3/16.
 */
public class ESUtils {
    private Client client = null;

    public ESUtils(String ipAddress) throws UnknownHostException {
        client = new TransportClient.Builder().build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ipAddress), 9300));
    }

    public Client getClient() {
        return client;
    }

    public IndexResponse index(String indexName, String typeName, String data) {
        IndexResponse response = client.prepareIndex(indexName, typeName)
                .setSource(data).execute().actionGet();
        return response;
    }

    public void close() {
        client.close();
    }
}
