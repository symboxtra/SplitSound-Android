package splitsound.com.net;

import jlibrtp.Participant;
import jlibrtp.RTCPAppIntf;

/**
 * Created by Neel on 5/11/2018.
 */

public class RTCPReceiverTask implements RTCPAppIntf, Runnable
{
    @Override
    public void run()
    {

    }

    @Override
    public void SRPktReceived(long var1, long var3, long var5, long var7, long var9, long var11, long[] var13, int[] var14, int[] var15, long[] var16, long[] var17, long[] var18, long[] var19)
    {
    }

    @Override
    public void RRPktReceived(long var1, long[] var3, int[] var4, int[] var5, long[] var6, long[] var7, long[] var8, long[] var9)
    {
    }

    @Override
    public void SDESPktReceived(Participant[] var1)
    {
    }

    @Override
    public void BYEPktReceived(Participant[] var1, String var2)
    {
    }

    @Override
    public void APPPktReceived(Participant var1, int var2, byte[] var3, byte[] var4)
    {
    }
}
