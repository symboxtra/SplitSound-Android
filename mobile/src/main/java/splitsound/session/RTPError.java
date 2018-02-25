package splitsound.session;

/**
 * Created by J. McKernan on 2/25/2018.
 */

public class RTPError
{
	public static final int ERR_RTP_OUTOFMEM                                          = -1;
	public static final int ERR_RTP_NOTHREADSUPPORT                                   = -2;
	public static final int ERR_RTP_COLLISIONLIST_BADADDRESS                          = -3;
	public static final int ERR_RTP_HASHTABLE_ELEMENTALREADYEXISTS                    = -4;
	public static final int ERR_RTP_HASHTABLE_ELEMENTNOTFOUND                         = -5;
	public static final int ERR_RTP_HASHTABLE_FUNCTIONRETURNEDINVALIDHASHINDEX        = -6;
	public static final int ERR_RTP_HASHTABLE_NOCURRENTELEMENT                        = -7;
	public static final int ERR_RTP_KEYHASHTABLE_FUNCTIONRETURNEDINVALIDHASHINDEX     = -8;
	public static final int ERR_RTP_KEYHASHTABLE_KEYALREADYEXISTS                     = -9;
	public static final int ERR_RTP_KEYHASHTABLE_KEYNOTFOUND                          = -10;
	public static final int ERR_RTP_KEYHASHTABLE_NOCURRENTELEMENT                     = -11;
	public static final int ERR_RTP_PACKBUILD_ALREADYINIT                             = -12;
	public static final int ERR_RTP_PACKBUILD_CSRCALREADYINLIST                       = -13;
	public static final int ERR_RTP_PACKBUILD_CSRCLISTFULL                            = -14;
	public static final int ERR_RTP_PACKBUILD_CSRCNOTINLIST                           = -15;
	public static final int ERR_RTP_PACKBUILD_DEFAULTMARKNOTSET                       = -16;
	public static final int ERR_RTP_PACKBUILD_DEFAULTPAYLOADTYPENOTSET                = -17;
	public static final int ERR_RTP_PACKBUILD_DEFAULTTSINCNOTSET                      = -18;
	public static final int ERR_RTP_PACKBUILD_INVALIDMAXPACKETSIZE                    = -19;
	public static final int ERR_RTP_PACKBUILD_NOTINIT                                 = -20;
	public static final int ERR_RTP_PACKET_BADPAYLOADTYPE                             = -21;
	public static final int ERR_RTP_PACKET_DATAEXCEEDSMAXSIZE                         = -22;
	public static final int ERR_RTP_PACKET_EXTERNALBUFFERNULL                         = -23;
	public static final int ERR_RTP_PACKET_ILLEGALBUFFERSIZE                          = -24;
	public static final int ERR_RTP_PACKET_INVALIDPACKET                              = -25;
	public static final int ERR_RTP_PACKET_TOOMANYCSRCS                               = -26;
	public static final int ERR_RTP_POLLTHREAD_ALREADYRUNNING                         = -27;
	public static final int ERR_RTP_POLLTHREAD_CANTINITMUTEX                          = -28;
	public static final int ERR_RTP_POLLTHREAD_CANTSTARTTHREAD                        = -29;
	public static final int ERR_RTP_RTCPCOMPOUND_INVALIDPACKET                        = -30;
	public static final int ERR_RTP_RTCPCOMPPACKBUILDER_ALREADYBUILDING               = -31;
	public static final int ERR_RTP_RTCPCOMPPACKBUILDER_ALREADYBUILT                  = -32;
	public static final int ERR_RTP_RTCPCOMPPACKBUILDER_ALREADYGOTREPORT              = -33;
	public static final int ERR_RTP_RTCPCOMPPACKBUILDER_APPDATALENTOOBIG              = -34;
	public static final int ERR_RTP_RTCPCOMPPACKBUILDER_BUFFERSIZETOOSMALL            = -35;
	public static final int ERR_RTP_RTCPCOMPPACKBUILDER_ILLEGALAPPDATALENGTH          = -36;
	public static final int ERR_RTP_RTCPCOMPPACKBUILDER_ILLEGALSUBTYPE                = -37;
	public static final int ERR_RTP_RTCPCOMPPACKBUILDER_INVALIDITEMTYPE               = -38;
	public static final int ERR_RTP_RTCPCOMPPACKBUILDER_MAXPACKETSIZETOOSMALL         = -39;
	public static final int ERR_RTP_RTCPCOMPPACKBUILDER_NOCURRENTSOURCE               = -40;
	public static final int ERR_RTP_RTCPCOMPPACKBUILDER_NOREPORTPRESENT               = -41;
	public static final int ERR_RTP_RTCPCOMPPACKBUILDER_NOTBUILDING                   = -42;
	public static final int ERR_RTP_RTCPCOMPPACKBUILDER_NOTENOUGHBYTESLEFT            = -43;
	public static final int ERR_RTP_RTCPCOMPPACKBUILDER_REPORTNOTSTARTED              = -44;
	public static final int ERR_RTP_RTCPCOMPPACKBUILDER_TOOMANYSSRCS                  = -45;
	public static final int ERR_RTP_RTCPCOMPPACKBUILDER_TOTALITEMLENGTHTOOBIG         = -46;
	public static final int ERR_RTP_RTCPPACKETBUILDER_ALREADYINIT                     = -47;
	public static final int ERR_RTP_RTCPPACKETBUILDER_ILLEGALMAXPACKSIZE              = -48;
	public static final int ERR_RTP_RTCPPACKETBUILDER_ILLEGALTIMESTAMPUNIT            = -49;
	public static final int ERR_RTP_RTCPPACKETBUILDER_NOTINIT                         = -50;
	public static final int ERR_RTP_RTCPPACKETBUILDER_PACKETFILLEDTOOSOON             = -51;
	public static final int ERR_RTP_SCHEDPARAMS_BADFRACTION                           = -52;
	public static final int ERR_RTP_SCHEDPARAMS_BADMINIMUMINTERVAL                    = -53;
	public static final int ERR_RTP_SCHEDPARAMS_INVALIDBANDWIDTH                      = -54;
	public static final int ERR_RTP_SDES_LENGTHTOOBIG                                 = -55;
	public static final int ERR_RTP_SDES_MAXPRIVITEMS                                 = -56;
	public static final int ERR_RTP_SDES_PREFIXNOTFOUND                               = -57;
	public static final int ERR_RTP_SESSION_ALREADYCREATED                            = -58;
	public static final int ERR_RTP_SESSION_CANTGETLOGINNAME                          = -59;
	public static final int ERR_RTP_SESSION_CANTINITMUTEX                             = -60;
	public static final int ERR_RTP_SESSION_MAXPACKETSIZETOOSMALL                     = -61;
	public static final int ERR_RTP_SESSION_NOTCREATED                                = -62;
	public static final int ERR_RTP_SESSION_UNSUPPORTEDTRANSMISSIONPROTOCOL           = -63;
	public static final int ERR_RTP_SESSION_USINGPOLLTHREAD                           = -64;
	public static final int ERR_RTP_SOURCES_ALREADYHAVEOWNSSRC                        = -65;
	public static final int ERR_RTP_SOURCES_DONTHAVEOWNSSRC                           = -66;
	public static final int ERR_RTP_SOURCES_ILLEGALSDESTYPE                           = -67;
	public static final int ERR_RTP_SOURCES_SSRCEXISTS                                = -68;
	public static final int ERR_RTP_TRANS_BUFFERLENGTHTOOSMALL                        = -69;
	public static final int ERR_RTP_UDPV4TRANS_ALREADYCREATED                         = -70;
	public static final int ERR_RTP_UDPV4TRANS_ALREADYINIT                            = -71;
	public static final int ERR_RTP_UDPV4TRANS_ALREADYWAITING                         = -72;
	public static final int ERR_RTP_UDPV4TRANS_CANTBINDRTCPSOCKET                     = -73;
	public static final int ERR_RTP_UDPV4TRANS_CANTBINDRTPSOCKET                      = -74;
	public static final int ERR_RTP_UDPV4TRANS_CANTCREATESOCKET                       = -75;
	public static final int ERR_RTP_UDPV4TRANS_CANTINITMUTEX                          = -76;
	public static final int ERR_RTP_UDPV4TRANS_CANTSETRTCPRECEIVEBUF                  = -77;
	public static final int ERR_RTP_UDPV4TRANS_CANTSETRTCPTRANSMITBUF                 = -78;
	public static final int ERR_RTP_UDPV4TRANS_CANTSETRTPRECEIVEBUF                   = -79;
	public static final int ERR_RTP_UDPV4TRANS_CANTSETRTPTRANSMITBUF                  = -80;
	public static final int ERR_RTP_UDPV4TRANS_COULDNTJOINMULTICASTGROUP              = -81;
	public static final int ERR_RTP_UDPV4TRANS_DIFFERENTRECEIVEMODE                   = -82;
	public static final int ERR_RTP_UDPV4TRANS_ILLEGALPARAMETERS                      = -83;
	public static final int ERR_RTP_UDPV4TRANS_INVALIDADDRESSTYPE                     = -84;
	public static final int ERR_RTP_UDPV4TRANS_NOLOCALIPS                             = -85;
	public static final int ERR_RTP_UDPV4TRANS_NOMULTICASTSUPPORT                     = -86;
	public static final int ERR_RTP_UDPV4TRANS_NOSUCHENTRY                            = -87;
	public static final int ERR_RTP_UDPV4TRANS_NOTAMULTICASTADDRESS                   = -88;
	public static final int ERR_RTP_UDPV4TRANS_NOTCREATED                             = -89;
	public static final int ERR_RTP_UDPV4TRANS_NOTINIT                                = -90;
	public static final int ERR_RTP_UDPV4TRANS_NOTWAITING                             = -91;
	public static final int ERR_RTP_UDPV4TRANS_PORTBASENOTEVEN                        = -92;
	public static final int ERR_RTP_UDPV4TRANS_SPECIFIEDSIZETOOBIG                    = -93;
	public static final int ERR_RTP_UDPV6TRANS_ALREADYCREATED                         = -94;
	public static final int ERR_RTP_UDPV6TRANS_ALREADYINIT                            = -95;
	public static final int ERR_RTP_UDPV6TRANS_ALREADYWAITING                         = -96;
	public static final int ERR_RTP_UDPV6TRANS_CANTBINDRTCPSOCKET                     = -97;
	public static final int ERR_RTP_UDPV6TRANS_CANTBINDRTPSOCKET                      = -98;
	public static final int ERR_RTP_UDPV6TRANS_CANTCREATESOCKET                       = -99;
	public static final int ERR_RTP_UDPV6TRANS_CANTINITMUTEX                          = -100;
	public static final int ERR_RTP_UDPV6TRANS_CANTSETRTCPRECEIVEBUF                  = -101;
	public static final int ERR_RTP_UDPV6TRANS_CANTSETRTCPTRANSMITBUF                 = -102;
	public static final int ERR_RTP_UDPV6TRANS_CANTSETRTPRECEIVEBUF                   = -103;
	public static final int ERR_RTP_UDPV6TRANS_CANTSETRTPTRANSMITBUF                  = -104;
	public static final int ERR_RTP_UDPV6TRANS_COULDNTJOINMULTICASTGROUP              = -105;
	public static final int ERR_RTP_UDPV6TRANS_DIFFERENTRECEIVEMODE                   = -106;
	public static final int ERR_RTP_UDPV6TRANS_ILLEGALPARAMETERS                      = -107;
	public static final int ERR_RTP_UDPV6TRANS_INVALIDADDRESSTYPE                     = -108;
	public static final int ERR_RTP_UDPV6TRANS_NOLOCALIPS                             = -109;
	public static final int ERR_RTP_UDPV6TRANS_NOMULTICASTSUPPORT                     = -110;
	public static final int ERR_RTP_UDPV6TRANS_NOSUCHENTRY                            = -111;
	public static final int ERR_RTP_UDPV6TRANS_NOTAMULTICASTADDRESS                   = -112;
	public static final int ERR_RTP_UDPV6TRANS_NOTCREATED                             = -113;
	public static final int ERR_RTP_UDPV6TRANS_NOTINIT                                = -114;
	public static final int ERR_RTP_UDPV6TRANS_NOTWAITING                             = -115;
	public static final int ERR_RTP_UDPV6TRANS_PORTBASENOTEVEN                        = -116;
	public static final int ERR_RTP_UDPV6TRANS_SPECIFIEDSIZETOOBIG                    = -117;
	public static final int ERR_RTP_INTERNALSOURCEDATA_INVALIDPROBATIONTYPE           = -118;
	public static final int ERR_RTP_SESSION_USERDEFINEDTRANSMITTERNULL                = -119;
	public static final int ERR_RTP_FAKETRANS_ALREADYCREATED                          = -120;
	public static final int ERR_RTP_FAKETRANS_ALREADYINIT                             = -121;
	public static final int ERR_RTP_FAKETRANS_CANTINITMUTEX                           = -122;
	public static final int ERR_RTP_FAKETRANS_COULDNTJOINMULTICASTGROUP               = -123;
	public static final int ERR_RTP_FAKETRANS_DIFFERENTRECEIVEMODE                    = -124;
	public static final int ERR_RTP_FAKETRANS_ILLEGALPARAMETERS                       = -125;
	public static final int ERR_RTP_FAKETRANS_INVALIDADDRESSTYPE                      = -126;
	public static final int ERR_RTP_FAKETRANS_NOLOCALIPS                              = -127;
	public static final int ERR_RTP_FAKETRANS_NOMULTICASTSUPPORT                      = -128;
	public static final int ERR_RTP_FAKETRANS_NOSUCHENTRY                             = -129;
	public static final int ERR_RTP_FAKETRANS_NOTAMULTICASTADDRESS                    = -130;
	public static final int ERR_RTP_FAKETRANS_NOTCREATED                              = -131;
	public static final int ERR_RTP_FAKETRANS_NOTINIT                                 = -132;
	public static final int ERR_RTP_FAKETRANS_PORTBASENOTEVEN                         = -133;
	public static final int ERR_RTP_FAKETRANS_SPECIFIEDSIZETOOBIG                     = -134;
	public static final int ERR_RTP_FAKETRANS_WAITNOTIMPLEMENTED                      = -135;
	public static final int ERR_RTP_RTPRANDOMURANDOM_CANTOPEN                         = -136;
	public static final int ERR_RTP_RTPRANDOMURANDOM_ALREADYOPEN                      = -137;
	public static final int ERR_RTP_RTPRANDOMRANDS_NOTSUPPORTED                       = -138;
	public static final int ERR_RTP_EXTERNALTRANS_ALREADYCREATED                      = -139;
	public static final int ERR_RTP_EXTERNALTRANS_ALREADYINIT                         = -140;
	public static final int ERR_RTP_EXTERNALTRANS_ALREADYWAITING                      = -141;
	public static final int ERR_RTP_EXTERNALTRANS_BADRECEIVEMODE                      = -142;
	public static final int ERR_RTP_EXTERNALTRANS_CANTINITMUTEX                       = -143;
	public static final int ERR_RTP_EXTERNALTRANS_ILLEGALPARAMETERS                   = -144;
	public static final int ERR_RTP_EXTERNALTRANS_NOACCEPTLIST                        = -145;
	public static final int ERR_RTP_EXTERNALTRANS_NODESTINATIONSSUPPORTED             = -146;
	public static final int ERR_RTP_EXTERNALTRANS_NOIGNORELIST                        = -147;
	public static final int ERR_RTP_EXTERNALTRANS_NOMULTICASTSUPPORT                  = -148;
	public static final int ERR_RTP_EXTERNALTRANS_NOSENDER                            = -149;
	public static final int ERR_RTP_EXTERNALTRANS_NOTCREATED                          = -150;
	public static final int ERR_RTP_EXTERNALTRANS_NOTINIT                             = -151;
	public static final int ERR_RTP_EXTERNALTRANS_NOTWAITING                          = -152;
	public static final int ERR_RTP_EXTERNALTRANS_SENDERROR                           = -153;
	public static final int ERR_RTP_EXTERNALTRANS_SPECIFIEDSIZETOOBIG                 = -154;
	public static final int ERR_RTP_UDPV4TRANS_CANTGETSOCKETPORT                      = -155;
	public static final int ERR_RTP_UDPV4TRANS_NOTANIPV4SOCKET                        = -156;
	public static final int ERR_RTP_UDPV4TRANS_SOCKETPORTNOTSET                       = -157;
	public static final int ERR_RTP_UDPV4TRANS_CANTGETSOCKETTYPE                      = -158;
	public static final int ERR_RTP_UDPV4TRANS_INVALIDSOCKETTYPE                      = -159;
	public static final int ERR_RTP_UDPV4TRANS_CANTGETVALIDSOCKET                     = -160;
	public static final int ERR_RTP_UDPV4TRANS_TOOMANYATTEMPTSCHOOSINGSOCKET          = -161;
	public static final int ERR_RTP_RTPSESSION_CHANGEREQUESTEDBUTNOTIMPLEMENTED       = -162;
	public static final int ERR_RTP_SECURESESSION_CONTEXTALREADYINITIALIZED           = -163;
	public static final int ERR_RTP_SECURESESSION_CANTINITIALIZE_SRTPCONTEXT          = -164;
	public static final int ERR_RTP_SECURESESSION_CANTINITMUTEX                       = -165;
	public static final int ERR_RTP_SECURESESSION_CONTEXTNOTINITIALIZED               = -166;
	public static final int ERR_RTP_SECURESESSION_NOTENOUGHDATATOENCRYPT              = -167;
	public static final int ERR_RTP_SECURESESSION_CANTENCRYPTRTPDATA                  = -168;
	public static final int ERR_RTP_SECURESESSION_CANTENCRYPTRTCPDATA                 = -169;
	public static final int ERR_RTP_SECURESESSION_NOTENOUGHDATATODECRYPT              = -170;
	public static final int ERR_RTP_SECURESESSION_CANTDECRYPTRTPDATA                  = -171;
	public static final int ERR_RTP_SECURESESSION_CANTDECRYPTRTCPDATA                 = -172;
	public static final int ERR_RTP_ABORTDESC_ALREADYINIT                             = -173;
	public static final int ERR_RTP_ABORTDESC_NOTINIT                                 = -174;
	public static final int ERR_RTP_ABORTDESC_CANTCREATEABORTDESCRIPTORS              = -175;
	public static final int ERR_RTP_ABORTDESC_CANTCREATEPIPE                          = -176;
	public static final int ERR_RTP_SESSION_THREADSAFETYCONFLICT                      = -177;
	public static final int ERR_RTP_SELECT_ERRORINSELECT                              = -178;
	public static final int ERR_RTP_SELECT_SOCKETDESCRIPTORTOOLARGE                   = -179;
	public static final int ERR_RTP_SELECT_ERRORINPOLL                                = -180;
	public static final int ERR_RTP_TCPTRANS_NOTINIT                                  = -181;
	public static final int ERR_RTP_TCPTRANS_ALREADYINIT                              = -182;
	public static final int ERR_RTP_TCPTRANS_ALREADYCREATED                           = -183;
	public static final int ERR_RTP_TCPTRANS_ILLEGALPARAMETERS                        = -184;
	public static final int ERR_RTP_TCPTRANS_CANTINITMUTEX                            = -185;
	public static final int ERR_RTP_TCPTRANS_ALREADYWAITING                           = -186;
	public static final int ERR_RTP_TCPTRANS_NOTCREATED                               = -187;
	public static final int ERR_RTP_TCPTRANS_INVALIDADDRESSTYPE                       = -188;
	public static final int ERR_RTP_TCPTRANS_NOSOCKETSPECIFIED                        = -189;
	public static final int ERR_RTP_TCPTRANS_NOMULTICASTSUPPORT                       = -190;
	public static final int ERR_RTP_TCPTRANS_RECEIVEMODENOTSUPPORTED                  = -191;
	public static final int ERR_RTP_TCPTRANS_SPECIFIEDSIZETOOBIG                      = -192;
	public static final int ERR_RTP_TCPTRANS_NOTWAITING                               = -193;
	public static final int ERR_RTP_TCPTRANS_SOCKETALREADYINDESTINATIONS              = -194;
	public static final int ERR_RTP_TCPTRANS_SOCKETNOTFOUNDINDESTINATIONS             = -195;
	public static final int ERR_RTP_TCPTRANS_ERRORINSEND                              = -196;
	public static final int ERR_RTP_TCPTRANS_ERRORINRECV                              = -197;
	
	private static final RTPErrorInfo[] ERROR_DESCRIPTIONS  =
	{
		new RTPErrorInfo(ERR_RTP_OUTOFMEM,"Out of memory" ),
		new RTPErrorInfo(ERR_RTP_NOTHREADSUPPORT, "No JThread support was compiled in"),
		new RTPErrorInfo(ERR_RTP_COLLISIONLIST_BADADDRESS, "Passed invalid address (null) to collision list"),
		new RTPErrorInfo(ERR_RTP_HASHTABLE_ELEMENTALREADYEXISTS, "Element already exists in hash table"),
		new RTPErrorInfo(ERR_RTP_HASHTABLE_ELEMENTNOTFOUND, "Element not found in hash table"),
		new RTPErrorInfo(ERR_RTP_HASHTABLE_FUNCTIONRETURNEDINVALIDHASHINDEX, "Function returned an illegal hash index"),
		new RTPErrorInfo(ERR_RTP_HASHTABLE_NOCURRENTELEMENT, "No current element selected in hash table"),
		new RTPErrorInfo(ERR_RTP_KEYHASHTABLE_FUNCTIONRETURNEDINVALIDHASHINDEX, "Function returned an illegal hash index"),
		new RTPErrorInfo(ERR_RTP_KEYHASHTABLE_KEYALREADYEXISTS, "Key value already exists in key hash table"),
		new RTPErrorInfo(ERR_RTP_KEYHASHTABLE_KEYNOTFOUND, "Key value not found in key hash table"),
		new RTPErrorInfo(ERR_RTP_KEYHASHTABLE_NOCURRENTELEMENT, "No current element selected in key hash table"),
		new RTPErrorInfo(ERR_RTP_PACKBUILD_ALREADYINIT, "RTP packet builder is already initialized"),
		new RTPErrorInfo(ERR_RTP_PACKBUILD_CSRCALREADYINLIST, "The specified CSRC is already in the RTP packet builder's CSRC list"),
		new RTPErrorInfo(ERR_RTP_PACKBUILD_CSRCLISTFULL, "The RTP packet builder's CSRC list already contains 15 entries"),
		new RTPErrorInfo(ERR_RTP_PACKBUILD_CSRCNOTINLIST, "The specified CSRC was not found in the RTP packet builder's CSRC list"),
		new RTPErrorInfo(ERR_RTP_PACKBUILD_DEFAULTMARKNOTSET, "The RTP packet builder's default mark flag is not set"),
		new RTPErrorInfo(ERR_RTP_PACKBUILD_DEFAULTPAYLOADTYPENOTSET, "The RTP packet builder's default payload type is not set"),
		new RTPErrorInfo(ERR_RTP_PACKBUILD_DEFAULTTSINCNOTSET, "The RTP packet builder's default timestamp increment is not set"),
		new RTPErrorInfo(ERR_RTP_PACKBUILD_INVALIDMAXPACKETSIZE, "The specified maximum packet size for the RTP packet builder is invalid"),
		new RTPErrorInfo(ERR_RTP_PACKBUILD_NOTINIT, "The RTP packet builder is not initialized"),
		new RTPErrorInfo(ERR_RTP_PACKET_BADPAYLOADTYPE, "Invalid payload type"),
		new RTPErrorInfo(ERR_RTP_PACKET_DATAEXCEEDSMAXSIZE, "Tried to create an RTP packet which whould exceed the specified maximum packet size"),
		new RTPErrorInfo(ERR_RTP_PACKET_EXTERNALBUFFERNULL, "Illegal value (null) passed as external buffer for the RTP packet"),
		new RTPErrorInfo(ERR_RTP_PACKET_ILLEGALBUFFERSIZE, "Illegal buffer size specified for the RTP packet"),
		new RTPErrorInfo(ERR_RTP_PACKET_INVALIDPACKET, "Invalid RTP packet format"),
		new RTPErrorInfo(ERR_RTP_PACKET_TOOMANYCSRCS, "More than 15 CSRCs specified for the RTP packet"),
		new RTPErrorInfo(ERR_RTP_POLLTHREAD_ALREADYRUNNING, "Poll thread is already running"),
		new RTPErrorInfo(ERR_RTP_POLLTHREAD_CANTINITMUTEX, "Can't initialize a mutex for the poll thread"),
		new RTPErrorInfo(ERR_RTP_POLLTHREAD_CANTSTARTTHREAD, "Can't start the poll thread"),
		new RTPErrorInfo(ERR_RTP_RTCPCOMPOUND_INVALIDPACKET, "Invalid RTCP compound packet format"),
		new RTPErrorInfo(ERR_RTP_RTCPCOMPPACKBUILDER_ALREADYBUILDING, "Already building this RTCP compound packet"),
		new RTPErrorInfo(ERR_RTP_RTCPCOMPPACKBUILDER_ALREADYBUILT, "This RTCP compound packet is already built"),
		new RTPErrorInfo(ERR_RTP_RTCPCOMPPACKBUILDER_ALREADYGOTREPORT, "There's already a SR or RR in this RTCP compound packet"),
		new RTPErrorInfo(ERR_RTP_RTCPCOMPPACKBUILDER_APPDATALENTOOBIG, "The specified APP data length for the RTCP compound packet is too big"),
		new RTPErrorInfo(ERR_RTP_RTCPCOMPPACKBUILDER_BUFFERSIZETOOSMALL, "The specified buffer size for the RTCP comound packet is too small"),
		new RTPErrorInfo(ERR_RTP_RTCPCOMPPACKBUILDER_ILLEGALAPPDATALENGTH, "The APP data length must be a multiple of four"),
		new RTPErrorInfo(ERR_RTP_RTCPCOMPPACKBUILDER_ILLEGALSUBTYPE, "The APP packet subtype must be smaller than 32"),
		new RTPErrorInfo(ERR_RTP_RTCPCOMPPACKBUILDER_INVALIDITEMTYPE, "Invalid SDES item type specified for the RTCP compound packet"),
		new RTPErrorInfo(ERR_RTP_RTCPCOMPPACKBUILDER_MAXPACKETSIZETOOSMALL, "The specified maximum packet size for the RTCP compound packet is too small"),
		new RTPErrorInfo(ERR_RTP_RTCPCOMPPACKBUILDER_NOCURRENTSOURCE, "Tried to add an SDES item to the RTCP compound packet when no SSRC was present"),
		new RTPErrorInfo(ERR_RTP_RTCPCOMPPACKBUILDER_NOREPORTPRESENT, "An RTCP compound packet must contain a SR or RR"),
		new RTPErrorInfo(ERR_RTP_RTCPCOMPPACKBUILDER_NOTBUILDING, "The RTCP compound packet builder is not initialized"),
		new RTPErrorInfo(ERR_RTP_RTCPCOMPPACKBUILDER_NOTENOUGHBYTESLEFT, "Adding this data would exceed the specified maximum RTCP compound packet size"),
		new RTPErrorInfo(ERR_RTP_RTCPCOMPPACKBUILDER_REPORTNOTSTARTED, "Tried to add a report block to the RTCP compound packet when no SR or RR was started"),
		new RTPErrorInfo(ERR_RTP_RTCPCOMPPACKBUILDER_TOOMANYSSRCS, "Only 31 SSRCs will fit into a BYE packet for the RTCP compound packet"),
		new RTPErrorInfo(ERR_RTP_RTCPCOMPPACKBUILDER_TOTALITEMLENGTHTOOBIG, "The total data for the SDES PRIV item exceeds the maximum size (255 bytes) of an SDES item"),
		new RTPErrorInfo(ERR_RTP_RTCPPACKETBUILDER_ALREADYINIT, "The RTCP packet builder is already initialized"),
		new RTPErrorInfo(ERR_RTP_RTCPPACKETBUILDER_ILLEGALMAXPACKSIZE, "The specified maximum packet size for the RTCP packet builder is too small"),
		new RTPErrorInfo(ERR_RTP_RTCPPACKETBUILDER_ILLEGALTIMESTAMPUNIT, "Speficied an illegal timestamp unit for the the RTCP packet builder"),
		new RTPErrorInfo(ERR_RTP_RTCPPACKETBUILDER_NOTINIT, "The RTCP packet builder was not initialized"),
		new RTPErrorInfo(ERR_RTP_RTCPPACKETBUILDER_PACKETFILLEDTOOSOON, "The RTCP compound packet filled sooner than expected"),
		new RTPErrorInfo(ERR_RTP_SCHEDPARAMS_BADFRACTION, "Illegal sender bandwidth fraction specified"),
		new RTPErrorInfo(ERR_RTP_SCHEDPARAMS_BADMINIMUMINTERVAL, "The minimum RTCP interval specified for the scheduler is too small"),
		new RTPErrorInfo(ERR_RTP_SCHEDPARAMS_INVALIDBANDWIDTH, "Invalid RTCP bandwidth specified for the RTCP scheduler"),
		new RTPErrorInfo(ERR_RTP_SDES_LENGTHTOOBIG, "Specified size for the SDES item exceeds 255 bytes"),
		new RTPErrorInfo(ERR_RTP_SDES_PREFIXNOTFOUND, "The specified SDES PRIV prefix was not found"),
		new RTPErrorInfo(ERR_RTP_SESSION_ALREADYCREATED, "The session is already created"),
		new RTPErrorInfo(ERR_RTP_SESSION_CANTGETLOGINNAME, "Can't retrieve login name"),
		new RTPErrorInfo(ERR_RTP_SESSION_CANTINITMUTEX, "A mutex for the RTP session couldn't be initialized"),
		new RTPErrorInfo(ERR_RTP_SESSION_MAXPACKETSIZETOOSMALL, "The maximum packet size specified for the RTP session is too small"),
		new RTPErrorInfo(ERR_RTP_SESSION_NOTCREATED, "The RTP session was not created"),
		new RTPErrorInfo(ERR_RTP_SESSION_UNSUPPORTEDTRANSMISSIONPROTOCOL, "The requested transmission protocol for the RTP session is not supported"),
		new RTPErrorInfo(ERR_RTP_SESSION_USINGPOLLTHREAD, "This function is not available when using the RTP poll thread feature"),
		new RTPErrorInfo(ERR_RTP_SESSION_USERDEFINEDTRANSMITTERNULL, "A user-defined transmitter was requested but the supplied transmitter component is NULL"),
		new RTPErrorInfo(ERR_RTP_SOURCES_ALREADYHAVEOWNSSRC, "Only one source can be marked as own SSRC in the source table"),
		new RTPErrorInfo(ERR_RTP_SOURCES_DONTHAVEOWNSSRC, "No source was marked as own SSRC in the source table"),
		new RTPErrorInfo(ERR_RTP_SOURCES_ILLEGALSDESTYPE, "Illegal SDES type specified for processing into the source table"),
		new RTPErrorInfo(ERR_RTP_SOURCES_SSRCEXISTS, "Can't create own SSRC because this SSRC identifier is already in the source table"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_ALREADYCREATED, "The transmitter was already created"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_ALREADYINIT, "The transmitter was already initialize"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_ALREADYWAITING, "The transmitter is already waiting for incoming data"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_CANTBINDRTCPSOCKET, "The 'bind' call for the RTCP socket failed"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_CANTBINDRTPSOCKET, "The 'bind' call for the RTP socket failed"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_CANTCREATESOCKET, "Couldn't create the RTP or RTCP socket"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_CANTINITMUTEX, "Failed to initialize a mutex used by the transmitter"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_CANTSETRTCPRECEIVEBUF, "Couldn't set the receive buffer size for the RTCP socket"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_CANTSETRTCPTRANSMITBUF, "Couldn't set the transmission buffer size for the RTCP socket"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_CANTSETRTPRECEIVEBUF, "Couldn't set the receive buffer size for the RTP socket"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_CANTSETRTPTRANSMITBUF, "Couldn't set the transmission buffer size for the RTP socket"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_COULDNTJOINMULTICASTGROUP, "Unable to join the specified multicast group"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_DIFFERENTRECEIVEMODE, "The function called doens't match the current receive mode"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_ILLEGALPARAMETERS, "Illegal parameters type passed to the transmitter"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_INVALIDADDRESSTYPE, "Specified address type isn't compatible with this transmitter"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_NOLOCALIPS, "Couldn't determine the local host name since the local IP list is empty"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_NOMULTICASTSUPPORT, "Multicast support is not available"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_NOSUCHENTRY, "Specified entry could not be found"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_NOTAMULTICASTADDRESS, "The specified address is not a multicast address"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_NOTCREATED, "The 'Create' call for this transmitter has not been called"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_NOTINIT, "The 'Init' call for this transmitter has not been called"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_NOTWAITING, "The transmitter is not waiting for incoming data"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_PORTBASENOTEVEN, "The specified port base is not an even number"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_SPECIFIEDSIZETOOBIG, "The maximum packet size is too big for this transmitter"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_ALREADYCREATED, "The transmitter was already created"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_ALREADYINIT, "The transmitter was already initialize"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_ALREADYWAITING, "The transmitter is already waiting for incoming data"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_CANTBINDRTCPSOCKET, "The 'bind' call for the RTCP socket failed"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_CANTBINDRTPSOCKET, "The 'bind' call for the RTP socket failed"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_CANTCREATESOCKET, "Couldn't create the RTP or RTCP socket"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_CANTINITMUTEX, "Failed to initialize a mutex used by the transmitter"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_CANTSETRTCPRECEIVEBUF, "Couldn't set the receive buffer size for the RTCP socket"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_CANTSETRTCPTRANSMITBUF, "Couldn't set the transmission buffer size for the RTCP socket"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_CANTSETRTPRECEIVEBUF, "Couldn't set the receive buffer size for the RTP socket"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_CANTSETRTPTRANSMITBUF, "Couldn't set the transmission buffer size for the RTP socket"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_COULDNTJOINMULTICASTGROUP, "Unable to join the specified multicast group"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_DIFFERENTRECEIVEMODE, "The function called doens't match the current receive mode"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_ILLEGALPARAMETERS, "Illegal parameters type passed to the transmitter"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_INVALIDADDRESSTYPE, "Specified address type isn't compatible with this transmitter"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_NOLOCALIPS, "Couldn't determine the local host name since the local IP list is empty"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_NOMULTICASTSUPPORT, "Multicast support is not available"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_NOSUCHENTRY, "Specified entry could not be found"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_NOTAMULTICASTADDRESS, "The specified address is not a multicast address"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_NOTCREATED, "The 'Create' call for this transmitter has not been called"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_NOTINIT, "The 'Init' call for this transmitter has not been called"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_NOTWAITING, "The transmitter is not waiting for incoming data"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_PORTBASENOTEVEN, "The specified port base is not an even number"),
		new RTPErrorInfo(ERR_RTP_UDPV6TRANS_SPECIFIEDSIZETOOBIG, "The maximum packet size is too big for this transmitter"),
		new RTPErrorInfo(ERR_RTP_TRANS_BUFFERLENGTHTOOSMALL,"The hostname is larger than the specified buffer size"),
		new RTPErrorInfo(ERR_RTP_SDES_MAXPRIVITEMS,"The maximum number of SDES private item prefixes was reached"),
		new RTPErrorInfo(ERR_RTP_INTERNALSOURCEDATA_INVALIDPROBATIONTYPE,"An invalid probation type was specified"),
		new RTPErrorInfo(ERR_RTP_FAKETRANS_ALREADYCREATED, "The transmitter was already created"),
		new RTPErrorInfo(ERR_RTP_FAKETRANS_ALREADYINIT, "The transmitter was already initialize"),
		new RTPErrorInfo(ERR_RTP_FAKETRANS_CANTINITMUTEX, "Failed to initialize a mutex used by the transmitter"),
		new RTPErrorInfo(ERR_RTP_FAKETRANS_COULDNTJOINMULTICASTGROUP, "Unable to join the specified multicast group"),
		new RTPErrorInfo(ERR_RTP_FAKETRANS_DIFFERENTRECEIVEMODE, "The function called doens't match the current receive mode"),
		new RTPErrorInfo(ERR_RTP_FAKETRANS_ILLEGALPARAMETERS, "Illegal parameters type passed to the transmitter"),
		new RTPErrorInfo(ERR_RTP_FAKETRANS_INVALIDADDRESSTYPE, "Specified address type isn't compatible with this transmitter"),
		new RTPErrorInfo(ERR_RTP_FAKETRANS_NOLOCALIPS, "Couldn't determine the local host name since the local IP list is empty"),
		new RTPErrorInfo(ERR_RTP_FAKETRANS_NOMULTICASTSUPPORT, "Multicast support is not available"),
		new RTPErrorInfo(ERR_RTP_FAKETRANS_NOSUCHENTRY, "Specified entry could not be found"),
		new RTPErrorInfo(ERR_RTP_FAKETRANS_NOTAMULTICASTADDRESS, "The specified address is not a multicast address"),
		new RTPErrorInfo(ERR_RTP_FAKETRANS_NOTCREATED, "The 'Create' call for this transmitter has not been called"),
		new RTPErrorInfo(ERR_RTP_FAKETRANS_NOTINIT, "The 'Init' call for this transmitter has not been called"),
		new RTPErrorInfo(ERR_RTP_FAKETRANS_PORTBASENOTEVEN, "The specified port base is not an even number"),
		new RTPErrorInfo(ERR_RTP_FAKETRANS_SPECIFIEDSIZETOOBIG, "The maximum packet size is too big for this transmitter"),
		new RTPErrorInfo(ERR_RTP_FAKETRANS_WAITNOTIMPLEMENTED, "The WaitForIncomingData is not implemented in the Gst transmitter"),
		new RTPErrorInfo(ERR_RTP_RTPRANDOMURANDOM_CANTOPEN, "Unable to open /dev/urandom for reading"),
		new RTPErrorInfo(ERR_RTP_RTPRANDOMURANDOM_ALREADYOPEN, "The device /dev/urandom was already opened"),
		new RTPErrorInfo(ERR_RTP_RTPRANDOMRANDS_NOTSUPPORTED, "The rand_s call is not supported on this platform"),
		new RTPErrorInfo(ERR_RTP_EXTERNALTRANS_ALREADYCREATED, "The external transmission component was already created"),
		new RTPErrorInfo(ERR_RTP_EXTERNALTRANS_ALREADYINIT, "The external transmission component was already initialized"),
		new RTPErrorInfo(ERR_RTP_EXTERNALTRANS_ALREADYWAITING, "The external transmission component is already waiting for incoming data"),
		new RTPErrorInfo(ERR_RTP_EXTERNALTRANS_BADRECEIVEMODE, "The external transmission component only supports accepting all incoming packets"),
		new RTPErrorInfo(ERR_RTP_EXTERNALTRANS_CANTINITMUTEX, "The external transmitter was unable to initialize a required mutex"),
		new RTPErrorInfo(ERR_RTP_EXTERNALTRANS_ILLEGALPARAMETERS, "Only parameters of type RTPExternalTransmissionParams can be passed to the external transmission component"),
		new RTPErrorInfo(ERR_RTP_EXTERNALTRANS_NOACCEPTLIST, "The external transmitter does not have an accept list"),
		new RTPErrorInfo(ERR_RTP_EXTERNALTRANS_NODESTINATIONSSUPPORTED, "The external transmitter does not have a destination list"),
		new RTPErrorInfo(ERR_RTP_EXTERNALTRANS_NOIGNORELIST, "The external transmitter does not have an ignore list"),
		new RTPErrorInfo(ERR_RTP_EXTERNALTRANS_NOMULTICASTSUPPORT, "The external transmitter does not support the multicast functions"),
		new RTPErrorInfo(ERR_RTP_EXTERNALTRANS_NOSENDER, "No sender has been set for this external transmitter"),
		new RTPErrorInfo(ERR_RTP_EXTERNALTRANS_NOTCREATED, "The external transmitter has not been created yet"),
		new RTPErrorInfo(ERR_RTP_EXTERNALTRANS_NOTINIT, "The external transmitter has not been initialized yet"),
		new RTPErrorInfo(ERR_RTP_EXTERNALTRANS_NOTWAITING, "The external transmitter is not currently waiting for incoming data"),
		new RTPErrorInfo(ERR_RTP_EXTERNALTRANS_SENDERROR, "The external transmitter was unable to actually send the data"),
		new RTPErrorInfo(ERR_RTP_EXTERNALTRANS_SPECIFIEDSIZETOOBIG, "The specified data size exceeds the maximum amount that has been set"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_CANTGETSOCKETPORT, "Unable to obtain the existing socket info using 'getsockname'"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_NOTANIPV4SOCKET, "The existing socket specified does not appear to be an IPv4 socket"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_SOCKETPORTNOTSET, "The existing socket that was specified does not have its port set yet"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_CANTGETSOCKETTYPE, "Can't get the socket type of the specified existing socket"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_INVALIDSOCKETTYPE, "The specified existing socket is not an UDP socket"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_CANTGETVALIDSOCKET, "Can't get a valid socket when trying to choose a port automatically"),
		new RTPErrorInfo(ERR_RTP_UDPV4TRANS_TOOMANYATTEMPTSCHOOSINGSOCKET, "Can't seem to get RTP/RTCP ports automatically, too many attempts"),
		new RTPErrorInfo(ERR_RTP_RTPSESSION_CHANGEREQUESTEDBUTNOTIMPLEMENTED, "Flag to change data was requested, but OnChangeRTPOrRTCPData was not reimplemented"),
		new RTPErrorInfo(ERR_RTP_SECURESESSION_CONTEXTALREADYINITIALIZED, "The initialization function was already called"),
		new RTPErrorInfo(ERR_RTP_SECURESESSION_CANTINITIALIZE_SRTPCONTEXT, "Unable to initialize libsrtp context"),
		new RTPErrorInfo(ERR_RTP_SECURESESSION_CANTINITMUTEX, "Unable to initialize a mutex" ),
		new RTPErrorInfo(ERR_RTP_SECURESESSION_CONTEXTNOTINITIALIZED, "The libsrtp context initilization function must be called before it can be used"),
		new RTPErrorInfo(ERR_RTP_SECURESESSION_NOTENOUGHDATATOENCRYPT, "There's not enough RTP or RTCP data to encrypt"),
		new RTPErrorInfo(ERR_RTP_SECURESESSION_CANTENCRYPTRTPDATA, "Unable to encrypt RTP data"),
		new RTPErrorInfo(ERR_RTP_SECURESESSION_CANTENCRYPTRTCPDATA, "Unable to encrypt RTCP data"),
		new RTPErrorInfo(ERR_RTP_SECURESESSION_NOTENOUGHDATATODECRYPT, "There's not enough RTP or RTCP data to decrypt"),
		new RTPErrorInfo(ERR_RTP_SECURESESSION_CANTDECRYPTRTPDATA, "Unable to decrypt RTP data"),
		new RTPErrorInfo(ERR_RTP_SECURESESSION_CANTDECRYPTRTCPDATA, "Unable to decrypt RTCP data"),
		new RTPErrorInfo(ERR_RTP_ABORTDESC_ALREADYINIT, "The RTPAbortDescriptors instance is already initialized" ),
		new RTPErrorInfo(ERR_RTP_ABORTDESC_NOTINIT, "The RTPAbortDescriptors instance is not yet initialized" ),
		new RTPErrorInfo(ERR_RTP_ABORTDESC_CANTCREATEABORTDESCRIPTORS, "Unable to create two connected TCP sockets for the abort descriptors" ),
		new RTPErrorInfo(ERR_RTP_ABORTDESC_CANTCREATEPIPE, "Unable to create a pipe for the abort descriptors" ),
		new RTPErrorInfo(ERR_RTP_SESSION_THREADSAFETYCONFLICT, "For the background poll thread to be used, thread safety must also be set" ),
		new RTPErrorInfo(ERR_RTP_SELECT_ERRORINSELECT, "Error in the call to 'select'" ),
		new RTPErrorInfo(ERR_RTP_SELECT_SOCKETDESCRIPTORTOOLARGE, "A socket descriptor value is too large for a call to 'select' (exceeds FD_SETSIZE)" ),
		new RTPErrorInfo(ERR_RTP_SELECT_ERRORINPOLL, "Error in the call to 'poll' or 'WSAPoll'" ),
		new RTPErrorInfo(ERR_RTP_TCPTRANS_NOTINIT, "The TCP transmitter is not yet initialized" ),
		new RTPErrorInfo(ERR_RTP_TCPTRANS_ALREADYINIT, "The TCP transmitter is already initialized" ),
		new RTPErrorInfo(ERR_RTP_TCPTRANS_NOTCREATED, "The TCP transmitter is not yet created" ),
		new RTPErrorInfo(ERR_RTP_TCPTRANS_ALREADYCREATED, "The TCP transmitter is already created" ),
		new RTPErrorInfo(ERR_RTP_TCPTRANS_ILLEGALPARAMETERS, "The parameters for the TCP transmitter are invalid" ),
		new RTPErrorInfo(ERR_RTP_TCPTRANS_CANTINITMUTEX, "Unable to initialize a mutex during the initialization of the TCP transmitter" ),
		new RTPErrorInfo(ERR_RTP_TCPTRANS_ALREADYWAITING, "The TCP transmitter is already waiting for data" ),
		new RTPErrorInfo(ERR_RTP_TCPTRANS_INVALIDADDRESSTYPE, "The address specified is not a valid address for the TCP transmitter" ),
		new RTPErrorInfo(ERR_RTP_TCPTRANS_NOSOCKETSPECIFIED, "No socket was specified in the address used for the TCP transmitter" ),
		new RTPErrorInfo(ERR_RTP_TCPTRANS_NOMULTICASTSUPPORT, "The TCP transmitter does not support multicasting" ),
		new RTPErrorInfo(ERR_RTP_TCPTRANS_RECEIVEMODENOTSUPPORTED, "The TCP transmitter does not support receive modes other than 'accept all'" ),
		new RTPErrorInfo(ERR_RTP_TCPTRANS_SPECIFIEDSIZETOOBIG, "The maximum packet size for the TCP transmitter is limited to 64KB" ),
		new RTPErrorInfo(ERR_RTP_TCPTRANS_NOTWAITING, "The TCP transmitter is not waiting for data" ),
		new RTPErrorInfo(ERR_RTP_TCPTRANS_SOCKETALREADYINDESTINATIONS, "The specified destination address (socket) was already added to the destination list of the TCP transmitter" ),
		new RTPErrorInfo(ERR_RTP_TCPTRANS_SOCKETNOTFOUNDINDESTINATIONS, "The specified destination address (socket) was not found in the list of destinations of the TCP transmitter" ),
		new RTPErrorInfo(ERR_RTP_TCPTRANS_ERRORINSEND, "An error occurred in the TCP transmitter while sending a packet" ),
		new RTPErrorInfo(ERR_RTP_TCPTRANS_ERRORINRECV, "An error occurred in the TCP transmitter while receiving a packet" ),
		new RTPErrorInfo(0,"")
	};
	
	public static String RTPGetErrorString(int errcode)
	{
		int i;
		
		if (errcode >= 0)
			return "No error";
		
		i = 0;
		while (ERROR_DESCRIPTIONS[i].code != 0)
		{
			if (ERROR_DESCRIPTIONS[i].code == errcode)
				return ERROR_DESCRIPTIONS[i].description;
			i++;
		}
		
		return "Unknown error code: " + errcode;
	}

	public static void main(String[] args)
	{
		System.out.println("Error code: " + RTPError.ERR_RTP_OUTOFMEM);
		System.out.println("Error string: " + RTPGetErrorString(-1));
	}
}
