Setup Instructions for windows environment:

Download Apache Kafka from kafka.apache.org/downloads
Current version version 4.1.0

Open powershell to extract tgz file using tar command
PS D:\Kafka_Project> tar -xzf .\kafka_2.13-4.1.0.tgz

Format Log Directories
PS D:\Kafka_Project\kafka_2.13-4.1.0> .\bin\windows\kafka-storage.bat format --standalone -t 23142222 -c .\config\server.properties

start Kafka server
PS D:\Kafka_Project\kafka_2.13-4.1.0> .\bin\windows\kafka-server-start.bat .\config\server.properties

start IntellijIDEA -> import project from Github

Select InstructionsCaptureApplication file -> Run

Start Postman
Use below collection to test csv and json files
https://.postman.co/workspace/My-Workspace~0a1f44f3-1e29-4114-8e9a-44bee07c990a/collection/37775965-833de9f3-a3e8-4e37-bc81-6ebe511e847b?action=share&creator=37775965

Sample files are included in project misc directory
sampleTrade.csv
TradeInstructions.json

Sample json for KafkaListener test is located in
sampleKafkaMessage.txt
To send Kafka message
PS D:\Kafka_Project\kafka_2.13-4.1.0> .\bin\windows\kafka-console-producer.bat --topic instructions.inbound --bootstrap-server localhost:9092
{"platFormId": "ACCT999", "account": "999-975-456789", "security": "BK", "type": "BUY", "amount": 500000, "timestamp": "2025-09-28T10:10:10Z"}





