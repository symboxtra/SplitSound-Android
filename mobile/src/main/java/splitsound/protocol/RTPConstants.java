package splitsound.protocol;

/**
 * Created by Neel on 2/25/2018.
 */

public interface RTPConstants
{
    public final int RTP_VERSION=2
        public final int RTP_MAXCSRCS=15;
        public final int RTP_MINPACKETSIZE=600;
        public final int RTP_DEFAULTPACKETSIZE=400;
        public final int RTP_PROBATIONCOUNT=2;
        public final int RTP_MAXPRIVITEMS=256;
        public final int RTP_SENDERTIMEOUTMULTIPLIER=2;
        public final int RTP_BYETIMEOUTMULTIPLIER=1;
        public final int RTP_MEMBERTIMEOUTMULTIPLIER=5;
        public final int RTP_COLLISIONTIMEOUTMULTIPLIER=10;
        public final int RTP_NOTETTIMEOUTMULTIPLIER=25;
        public final double RTP_DEFAULTSESSIONBANDWIDTH=10000.0;

        public final int RTP_RTCPTYPE_SR=200;
        public final int RTP_RTCPTYPE_RR=201;
        public final int RTP_RTCPTYPE_SDES=202;
        public final int RTP_RTCPTYPE_BYE=203;
        public final int RTP_RTCPTYPE_APP=204;

        public final int RTCP_SDES_ID_CNAME=1;
        public final int RTCP_SDES_ID_NAME=2;
        public final int RTCP_SDES_ID_EMAIL=3;
        public final int RTCP_SDES_ID_PHONE=4;
        public final int RTCP_SDES_ID_LOCATION=5;
        public final int RTCP_SDES_ID_TOOL=6;
        public final int RTCP_SDES_ID_NOTE=7;
        public final int RTCP_SDES_ID_PRIVATE=8;
        public final int RTCP_SDES_NUMITEMS_NONPRIVATE=7;
        public final int RTCP_SDES_MAXITEMLENGTH=255;

        public final float RTCP_BYE_MAXREASONLENGTH=255;
        public final float RTCP_DEFAULTMININTERVAL=5.0f;
        public final float RTCP_DEFAULTBANDWIDTHFRACTION=0.05f;
        public final float RTCP_DEFAULTSENDERFRACTION=0.25f;
        public final boolean RTCP_DEFAULTHALFATSTARTUP=true;
        public final boolean RTCP_DEFAULTIMMEDIATEBYE=true;
        public final boolean RTCP_DEFAULTSRBYE=true;
}
