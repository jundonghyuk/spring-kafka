# 토픽 생성
./kafka-topics --bootstrap-server kafka1:9091 --create --replication-factor 3 --topic test --partitions 1

# 세그먼트 파일 확인
./kafka-dump-log --print-data-log --files /var/lib/kafka/data/test-0/00000000000000000000.log