file=$(find . -iname "*.jar*")
echo $file

host=http://127.0.0.1:8081

curl -X POST -H "Expect:" -F "jarfile=@$file" $host/jars/upload

