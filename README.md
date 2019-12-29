# SpringCloudKafkaStream
Application which will use spring cloud and kafka streams to publish and consume messages
Please note before running this application you need to have kafka broker running in your machine you can download the binaries
from https://kafka.apache.org/downloads.
once you download run zookeeper and then start kafka and then the application

1)It has a kafka publisher which will publish messages to the topic
2)kafka stream listener which will receive the messages,process them and resend them to a different topic.
3)Binding config which will actually bind the channels to the respective outbound and inbound .
4)application.properties has all the config necessary to send and receive
