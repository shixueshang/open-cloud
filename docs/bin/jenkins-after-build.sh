cp -rf $WORKSPACE/opencloud-upms/$JOB_NAME/target/$JOB_NAME.jar /root/deploy
cd /root/deploy
sleep 3
./startup.sh restart $JOB_NAME.jar
