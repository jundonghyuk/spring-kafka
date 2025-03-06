# 토픽 생성
# https://kafka.apache.org/25/documentation.html#min.insync.replicas
./kafka-topics --bootstrap-server kafka1:9091 --create --replication-factor 3 --topic test --partitions 1

# 토픽 정보 확인
./kafka-topics --bootstrap-server kafka1:9091 --topic test --describe

# 프로듀서 클라이언트 실행
# acks to "all" (or "-1")
./kafka-console-producer --bootstrap-server kafka2:9092 --topic test2

# 컨슈머 클라이언트 실행 (컨슈머 그룹 이름, 오프셋 리셋 옵션)
# 오프셋 리셋 옵션: earliest, latest, none
# reset-offsets -to offset 3 --execute: 특정 오프셋으로 리셋
./kafka-console-consumer --bootstrap-server kafka2:9092 --topic test4 --group consumer-group-1 --consumer-property auto.offset.reset=latest

# 세그먼트 파일 확인
# ${topic}-${partitionId} 형식으로 파일이 생성됨
./kafka-dump-log --print-data-log --files /var/lib/kafka/data/test-0/00000000000000000000.log

# 특정 토픽에 대한 파티션 별 오프셋 정보
./kafka-run-class kafka.tools.GetOffsetShell --broker-list kafka3:9093 --topic test4 --time -1

# 컨슈머 그룹이름 확인
./kafka-consumer-groups --bootstrap-server kafka2:9092 --list

# 컨슈머 그룹의 특정 파티션에 대한 오프셋 정보
./kafka-consumer-groups --bootstrap-server kafka2:9092 --describe --group test4

# offset checkpoint 파일 확인
cat replication-offset-checkpoint

# leader epoch checkpoint 파일 확인
# leader-epoch-checkpoint 파일은 리더 변경이 발생할 때마다 업데이트되며, 이는 주로 이전 리더가 실패하고 새로운 리더가 선출되었을 때 일어난다.
cat leader-epoch-checkpoint

# 0
# 1 현재의 리더에포크 번호 -> 리더가 변경될 때 마다 하나씩 증가
# 0 0 리더에포크 번호 / 최종 커밋 후 새로운 메시지를 전송받게 될 오프셋 번호