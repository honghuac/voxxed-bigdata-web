/*
 * Copyright 2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.voxxed.bigdata.web;


import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cache.CacheConstants;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;


@Component
public class RouteConfig extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration().bindingMode(RestBindingMode.json);

        // Full URL: http://localhost:8080/api/ratings
        rest().post("/ratings")
                .type(Event.class)
                .route()
                .log("Received rating: ${body}")
                .setHeader(KafkaConstants.KEY).simple("body.itemId", String.class)
                .setHeader(KafkaConstants.PARTITION_KEY).constant(0)
                .marshal().json(JsonLibrary.Jackson).convertBodyTo(String.class)
                .to("kafka:kafka:9092?topic=stars");

        rest().get("/recommendations/{userId}")
                .route()
//                .log("Requested recommendation for user ${header.userId}")
                .setHeader(CacheConstants.CACHE_KEY, header("userId"))
                .doTry()
                    .to("cache:recommendations?operation=get")
                    .unmarshal().json(JsonLibrary.Jackson, Recommendation.class)
                .endDoTry()
                .doCatch(Exception.class)
                    .setBody().constant(null)
                    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                .end();


        from("kafka:kafka:9092?topic=recommendations&groupId=web")
                .log("Received recommendation ${body}")
                .setHeader(CacheConstants.CACHE_KEY, header(KafkaConstants.KEY))
                .to("cache://recommendations?operation=add");


//        from("timer:tick")
//                .setHeader(CacheConstants.CACHE_KEY, constant("1"))
//                .setBody().exchange(e -> new Recommendation(1L, Collections.singletonList((long) new Random().nextInt(10))))
//                .marshal().json(JsonLibrary.Jackson)
//                .to("cache://recommendations?operation=add");

    }

}
