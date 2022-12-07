#Daemon

#Daemon Process Name
readonly NEXT_EDS_DAEMON_NAME = nextImgDaemon

#run daemon process
pid = $(ps - ef | grep -v grep | grep "-Dproject=${NEXT_EDS_DAEMON_NAME}" | awk {'print $2'})

#PID check
if [[$pid = ""]]; then
	echo "${NEXT_EDS_DAEMON_NAME} is not running"
else
	#process kill
	echo "shutdown ${NEXT_EDS_DAEMON_NAME}"
	kill -9 ${pid}
fi

#end of script