#Daemon

readonly NEXT_EDS_PATH = /shbweb/next_eds

#Daemon Process Name
readonly NEXT_EDS_DAEMON_NAME = nextImgDaemon

#Daemon Process Path
readonly NEXT_EDS_DAEMON_PATH = ${NEXT_EDS_PATH}/next_eds_img

#move to working directory
cd ${NEXT_EDS_DAEMON_PATH}

#Daemon Process shutdown
./${NEXT_EDS_DAEMON_NAME}shutdown.sh

#wait(seconds)
sleep 5

#Daemon Process start
./${NEXT_EDS_DAEMON_NAME}Starter.sh

#end of script