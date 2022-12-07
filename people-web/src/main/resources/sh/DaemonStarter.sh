#Daemon

export LANG=ko_KR.eucKR

readonly NEXT_EDS_PATH = /shbweb/next_eds

#Daemon Process Name
readonly NEXT_EDS_DAEMON_NAME = nextImgDaemon

#Daemon Process Main Class
readonly NEXT_EDS_DAEMON_MAIN_CLASS = next.eds.img.itf.NextImgDaemonStarter

#Daemon Process Path
readonly NEXT_EDS_DAEMON_PATH = ${NEXT_EDS_PATH}/next_eds_img

#Daemon Process log4j.xml file
readonly NEXT_EDS_DAEMON_LOG4J_FILE = ${NEXT_EDS_DAEMON_PATH}/Config/log4j_img.xml

#Daemon Process log Path
readonly NEXT_EDS_DAEMON_LOG_PATH = /shblog/next_eds/img

#Common Path
export NEXT_EDS_COMMON_PATH = ${NEXT_EDS_PATH}/next_eds_common

#Common Library files
NEXT_EDS_COMMON_LIB_FILES = ''
for file in ${NEXT_EDS_COMMON_PATH}/lib/*jar
do
	NEXT_EDS_COMMON_LIB_FILES = ${NEXT_EDS_COMMON_LIB_FILES}:${file}
done

#Daemon Library files
NEXT_EDS_DAEMON_LIB_FILES = ''
for file in ${NEXT_EDS_DAEMON_PATH}/lib/*jar
do
	NEXT_EDS_DAEMON_LIB_FILES = ${NEXT_EDS_DAEMON_LIB_FILES}:${file}
done


readonly HOST_NM = 'hostname'

#inzisoft License
export SHLIB_PATH = /shbweb/EDS/SHB_EDS_pdfProcDaemon1/sl_new:.

if [[ ${HOST_NM} = "xpexpa1p"]]; then
	INZISOFT_LICENSE_FILE = "/shbweb/EDS/SHB_EDS_pdfProcDaemon1/inzi/conv_lic/inzi.license.ShinhanBank.xpexpa1p.20210906_OP.xml"
elif [[ ${HOST_NM} = "xpexpa2p"]]; then
	INZISOFT_LICENSE_FILE = "/shbweb/EDS/SHB_EDS_pdfProcDaemon1/inzi/conv_lic/inzi.license.ShinhanBank.xpexpa2p.20210906_OP.xml"
else 
	INZISOFT_LICENSE_FILE = "/shbweb/EDS/SHB_EDS_pdfProcDaemon1/inzi/conv_lic/inzi.license.ShinhanBank.xpexpa1p.20211020_OP.xml"
fi

export INZISOFT_LICENSE_FILE


#Security.key 
if [[ ${HOST_NM} = "xpexpa1p"]]; then
	export SECURITY_KEY = "/tmax/jeus/jeus6/config/xpexpa1p/security/security.key"
elif [[ ${HOST_NM} = "xpexpa2p"]]; then
	export SECURITY_KEY = "/tmax/jeus/jeus6/config/xpexpa2p/security/security.key"
else 
	export SECURITY_KEY = "/tmax/jeus/jeus6/config/xpexpa1d/security/security.key"
fi

#move to working directory
cd ${NEXT_EDS_COMMON_PATH}

#classpath setting
export CLASSPATH = "$CLASSPATH:${NEXT_EDS_COMMON_LIB_FILES}:${NEXT_EDS_COMMON_PATH}/bin:${NEXT_EDS_DAEMON_LIB_FILES}:${NEXT_EDS_DAEMON_PATH}/bin"
#echo $CLASSPATH

#java executable options
readonly JVM_PROP_OPTIONS = "-Dproject=${NEXT_EDS_DAEMON_NAME} -Djeus.security.keypath=${SECURITY_KEY} -Dlog4j.configuration=File:${NEXT_EDS_DAEMON_LOG4J_FILE}"
readonly MEMORY_OPTIONS   = "-Xms1024m -Xmx2048m -Xmn512m"
readonly HEAP_DUMP_OPTIONS = "-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${NEXT_EDS_DAEMON_LOG_PATH}"
readonly GC_OPTIONS = "-verbose:gc -XX:+printGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -Xverbosegc:file=${NEXT_EDS_DAEMON_LOG_PATH}/${NEXT_EDS_DAEMON_NAME}_gc.log"



#run daemon process
pid = $(ps - ef | grep -v grep | grep "-Dproject=${NEXT_EDS_DAEMON_NAME}" | awk {'print $2'})

#PID check
if [[$pid = ""]]; then
	echo "start ${NEXT_EDS_DAEMON_NAME}"
	
	#server start
	if [[$1 = "fg" ]]; then
		/opt/java8/bin/java -d64 ${JVM_PROP_OPTIONS} ${MEMORY_OPTIONS} ${HEAP_DUMP_OPTIONS} ${GC_OPTIONS} ${NEXT_EDS_DAEMON_MAIN_CLASS} 
	else
		/opt/java8/bin/java -d64 ${JVM_PROP_OPTIONS} ${MEMORY_OPTIONS} ${HEAP_DUMP_OPTIONS} ${GC_OPTIONS} ${NEXT_EDS_DAEMON_MAIN_CLASS} 1>/dev/null 2>&1 &
	fi
else
	echo "already ${NEXT_EDS_DAEMON_NAME} running"
fi

#end of script