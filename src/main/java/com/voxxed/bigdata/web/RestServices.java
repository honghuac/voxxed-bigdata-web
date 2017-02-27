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

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.stereotype.Component;


@Component
public class RestServices extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // Full URL: http://localhost:8080/api/...

        rest().post("/movies/{movieId}/ratings/{num}")
                .route()
                .log("Movie ${header.movieId} rated with ${header.num} stars")
                .setHeader(KafkaConstants.KEY).header("movieId")
                .setHeader(KafkaConstants.PARTITION_KEY).constant(0)
                .setBody().header("num")
                .to("kafka:kafka:9092?topic=stars");

    }

}
