Use sbt assembly to create a single jar with all dependency

Scala akka, Kafka

For Kafka - demo
To have a local Kafka setup use docker compose to run both kafka and zoo keeper images

# To start the images zookeeper and Kafka
docker-compose -f kafka-single-node.yml up -d

docker ps

#To shutdown them
docker-compose -f kafka-single-node.yml down

# login to container
docker exec -it kafka-broker /bin/bash


## creating new topics

./kafka-topics.sh \
--zookeeper zookeeper:2181 \
--create \
--topic kfaka.demo \
--partion 1 \
--replication-factor 1


	./kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic kfaka.demo
	./kafka-console-producer.sh  --topic kafka.demo --bootstrap-server localhost:9092
	kafka-console-producer.sh --topic quickstart-events --bootstrap-server localhost:9092
	kafka-console-consumer.sh --topic quickstart-events --from-beginning --bootstrap-server localhost:9092
	docker exec -it zookeeper /bin/bash
	cd /opt/bitnami/zookeeper/bin
	zkCli.sh
	./kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic kfaka-demo
	
	kafka-console-producer.sh --topic kfaka-demo --bootstrap-server localhost:9092 --property "parse.key=true" --property "key.separator=:"
	
	kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic kfaka-demo  --from-beginning --group c1
	
	/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --all-groups
	
	
	