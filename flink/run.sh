jarId=$1

host=http://127.0.0.1:8081

curl -X POST $host/jars/$jarId/run\?entry-class\=com.github.kuangcp.hi.SimpleStatistic

