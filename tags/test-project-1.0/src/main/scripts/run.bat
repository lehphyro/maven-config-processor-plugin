cd %1
call mvn -Dmaven.test.skip=true clean install

cd %2
mvn -e config-processor:process
