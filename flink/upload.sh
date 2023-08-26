. ./host.conf
file=$(find . -iname "*.jar*")
echo $file

curl -X POST -H "Expect:" -F "jarfile=@$file" $host/jars/upload

