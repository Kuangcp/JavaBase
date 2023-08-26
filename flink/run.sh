. ./host.conf
jarId=$1

curl -X POST $host/jars/$jarId/run\?entry-class\=com.github.kuangcp.hi.SimpleStatistic

