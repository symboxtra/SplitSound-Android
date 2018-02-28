package splitsound.protocol;

/**
 * Created by Neel on 2/25/2018.
 */

public final class RTPConstants {
    public static int RTP_VERSION = 2
    public static int RTP_MAXCSRCS = 15;
    public static int RTP_MINPACKETSIZE = 600;
    public static int RTP_DEFAULTPACKETSIZE = 400;
    public static int RTP_PROBATIONCOUNT = 2;
    public static int RTP_MAXPRIVITEMS = 256;
    public static int RTP_SENDERTIMEOUTMULTIPLIER = 2;
    public static int RTP_BYETIMEOUTMULTIPLIER = 1;
    public static int RTP_MEMBERTIMEOUTMULTIPLIER = 5;
    public static int RTP_COLLISIONTIMEOUTMULTIPLIER = 10;
    public static int RTP_NOTETTIMEOUTMULTIPLIER = 25;
    public static double RTP_DEFAULTSESSIONBANDWIDTH = 10000.0;

    public static int RTP_RTCPTYPE_SR = 200;
    public static int RTP_RTCPTYPE_RR = 201;
    public static int RTP_RTCPTYPE_SDES = 202;
    public static int RTP_RTCPTYPE_BYE = 203;
    public static int RTP_RTCPTYPE_APP = 204;

    public static int RTCP_SDES_ID_CNAME = 1;
    public static int RTCP_SDES_ID_NAME = 2;
    public static int RTCP_SDES_ID_EMAIL = 3;
    public static int RTCP_SDES_ID_PHONE = 4;
    public static int RTCP_SDES_ID_LOCATION = 5;
    public static int RTCP_SDES_ID_TOOL = 6;
    public static int RTCP_SDES_ID_NOTE = 7;
    public static int RTCP_SDES_ID_PRIVATE = 8;
    public static int RTCP_SDES_NUMITEMS_NONPRIVATE = 7;
    public static int RTCP_SDES_MAXITEMLENGTH = 255;

    public static float RTCP_BYE_MAXREASONLENGTH = 255;
    public static float RTCP_DEFAULTMININTERVAL = 5.0f;
    public static float RTCP_DEFAULTBANDWIDTHFRACTION = 0.05f;
    public static float RTCP_DEFAULTSENDERFRACTION = 0.25f;
    public static boolean RTCP_DEFAULTHALFATSTARTUP = true;
    public static boolean RTCP_DEFAULTIMMEDIATEBYE = true;
    public static boolean RTCP_DEFAULTSRBYE = true;
}
