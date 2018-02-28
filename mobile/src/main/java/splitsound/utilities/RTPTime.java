package splitsound.utilities;

import java.util.concurrent.TimeUnit;

import splitsound.protocol.RTPConstants;

/**
 * Created by Neel on 2/25/2018.
 */

public class RTPTime
{
    private double m_t;
    class RTPNTPTime
    {
        private int msw, lsw;

        public RTPNTPTime(int m, int n)
        {
            msw = m;
            lsw = m;
        }

        public int getLSW()
        {
            return lsw;
        }

        public int getMSW()
        {
            return msw;
        }
    }
    public RTPTime(double t)
    {
        m_t = t;
    }

    public RTPTime(double seconds, int microseconds)
    {
        if(seconds >= 0)
            m_t = seconds + 1e-6*(double)microseconds;
        else
        {
            double possec = -seconds;

            m_t = (double)possec + 1e-6*(double)microseconds;
            m_t = -m_t;
        }
    }

    public RTPTime(RTPNTPTime ntime)
    {
        if(ntime.getMSW() < RTP_NTPTIMEOFFSET)
        {
            m_t = 0;
        }
        else
        {
            int sec = ntime.getMSW() - RTP_NTPTIMEOFFSET;
            double x = (double)ntime.getLSW();
            x /= Math.pow(65536.0, 2);
            x *= 1000000.0;
            int micros = (int)x;

            m_t = (double)sec + 1e-6*(double)micros;

        }
    }

    public double getSeconds()
    {
        return (double)((int)m_t);
    }

    public int getMicroSeconds()
    {
        int microsec;

        if(m_t >= 0)
        {
            double sec = (double)(int)m_t;
            microsec = (int)(1e6*(m_t - (double)sec) + 0.5);
        }
        else
        {
            double sec = (double)((int)-m_t);
            microsec = (int)(1e6*(-m_t - (double)sec) + 0.5);
        }

        if(microsec >= 1000000)
            return 999999;

        return microsec;
    }

    public double calculateMicroseconds(double performanceCount, double performanceFreq)
    {
        double f = performanceFreq;
        double a = performanceCount;
        double b = (double)((int)(a / f));
        double c = (double)((int)(a % f));

        return b*C1000000 + (c*C1000000) / f;

    }

    public RTPTime currentTime()
    {
        long time = System.nanoTime();

        long timeInMs = TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS);

        double t = 1e-6*(double)(timeInMs);

        return new RTPTime(t);
    }

    public void wait(RTPTime delay)
    {
        if(delay.m_t <= 0)
            return;

        double sec = (double)(int)delay.m_t;
        int microsec = (int)(1e6*(delay.m_t - sec));

        int timeInMilli = (int)(sec*1000) + (microsec / 1000);

        try
        {
            Thread.sleep(timeInMilli);
        }catch(InterruptedException e)
        {}
    }
}
