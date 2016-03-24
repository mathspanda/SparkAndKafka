package org.mp.service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.mp.es.util.ESUtils;

import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by MP on 24/3/16.
 */
public class RegionService {
    public static int getActiveRegion() {
        long curTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(curTime);
        calendar.add(Calendar.SECOND, -5);
        long oldTime = calendar.getTimeInMillis();
        return getActiveRegionBetween(oldTime, curTime);
    }

    private static int getActiveRegionBetween(long oldTime, long curTime) {
        try {
            ESUtils utils = new ESUtils("127.0.0.1");
            Client client = utils.getClient();
            SearchResponse response = client.prepareSearch("moves").setTypes("region")
                    .setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                            QueryBuilders.rangeQuery("timestamp").from(oldTime).to(curTime)))
                    .addAggregation(AggregationBuilders.terms("by_id").field("id"))
                    .execute().actionGet();
            return getActiveBucketFromAgg(response.getAggregations(), "by_id");
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    private static int getActiveBucketFromAgg(Aggregations aggregations, String key) {
        Terms terms = aggregations.get(key);
        Iterator<Terms.Bucket> iterator = terms.getBuckets().iterator();
        long maxCount = 0;
        int activeRegion = 0;
        while (iterator.hasNext()) {
            Terms.Bucket bucket = iterator.next();
            long docCount = bucket.getDocCount();
            if (docCount > maxCount) {
                maxCount = docCount;
                activeRegion = bucket.getKeyAsNumber().intValue();
            }
        }
        return activeRegion;
    }
}
