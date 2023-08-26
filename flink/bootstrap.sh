. ./host.conf
result=$(./upload.sh)

temp=${result#*flink-web-upload/}
jarId=${temp%%\"*}

echo "\n start run: " $jarId "\n"

# 当 job 执行完成后, get请求 才会返回
run(){
  jobResult=$(./run.sh $1)

  echo $jobResult
  temp=${jobResult#*:\"}
  jobId=${temp%%\"\}*}

  echo $(date) "jobId: " $jobId
}

# 当 job 在执行的时候, 并不会在 jobs/ 
jobs(){
  host=$1
  for i in $(seq 10); do
	  sleep 1
	  echo "\n start curl " $host/jobs/ "\n"

	  curl $host/jobs

	  echo ""
  done;
}

jobs $host &

run $jarId