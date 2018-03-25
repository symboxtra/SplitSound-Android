LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := jthread
LOCAL_C_INCLUDES := ./files/jthread
LOCAL_SRC_FILES := ./files/jthread/jmutex.cpp \
                   ./files/jthread/jthread.cpp
include $(BUILD_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jrtp
LOCAL_C_INCLUDES := ./files \
LOCAL_SRC_FILES := ./files/extratransmitters/rtpfaketransmitter.cpp \
                   ./files/rtcpapppacket.cpp \
                   ./files/rtcpbyepacket.cpp \
                   ./files/rtcpcompoundpacket.cpp \
                   ./files/rtcpcompoundpacketbuilder.cpp \
                   ./files/rtcppacket.cpp \
                   ./files/rtcppacketbuilder.cpp \
                   ./files/rtcprrpacket.cpp \
                   ./files/rtcpscheduler.cpp \
                   ./files/rtcpsdesinfo.cpp \
                   ./files/rtcpsdespacket.cpp \
                   ./files/rtcpsrpacket.cpp \
                   ./files/rtpbyteaddress.cpp \
                   ./files/rtpcollisionlist.cpp \
                   ./files/rtpdebug.cpp \
                   ./files/rtperrors.cpp \
                   ./files/rtpexternaltransmitter.cpp \
                   ./files/rtpinternalsourcedata.cpp \
                   ./files/rtpipv4address.cpp \
                   ./files/rtpipv6address.cpp \
                   ./files/rtplibraryversion.cpp \
                   ./files/rtppacket.cpp \
                   ./files/rtppacketbuilder.cpp \
                   ./files/rtppollthread.cpp \
                   ./files/rtprandom.cpp \
                   ./files/rtprandomrand48.cpp \
                   ./files/rtprandomrands.cpp \
                   ./files/rtprandomurandom.cpp \
                   ./files/rtpsession.cpp \
                   ./files/rtpsessionparams.cpp \
                   ./files/rtpsessionsources.cpp \
                   ./files/rtpsourcedata.cpp \
                   ./files/rtpsources.cpp \
                   ./files/rtptimeutilities.cpp \
                   ./files/rtpudpv4transmitter.cpp \
                   ./files/rtpudpv6transmitter.cpp
include $(BUILD_STATIC_LIBRARY)


#include $(CLEAR_VARS)

#LOCAL_MODULE := jthreadlib
#LOCAL_SRC_FILES := libjthread.so
#include $(PREBUILT_SHARED_LIBRARY)

#include $(CLEAR_VARS)

#LOCAL_MODULE := rtplib
#LOCAL_SRC_FILES := libjrtp.so
#include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE := rtplib

LOCAL_STATIC_LIBRARIES := jthread jrtp

include $(BUILD_SHARED_LIBRARY)