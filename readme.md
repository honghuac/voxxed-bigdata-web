# Voxxed BigData Web
 
This repository contains the code of the web application used as demo at Voxxed Bucharest 2017.
 
## Running

1) Start [Openshift Origin Development Environment](https://github.com/openshift/origin/blob/master/docs/cluster_up_down.md). 

2) Deploy the [Kafka service](https://github.com/nicolaferraro/voxxed-bigdata-kafka).

3) Execute: `mvn clean fabric8:deploy`