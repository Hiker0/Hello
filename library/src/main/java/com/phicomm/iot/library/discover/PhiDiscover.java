package com.phicomm.iot.library.discover;

import java.util.List;

/**
 * Created by Johnson on 2017-04-13.
 */

public abstract class PhiDiscover {


    protected DiscoveredDevice mHostInfo;
    /**
     * Create an instance of PhiDiscover.
     * Can on;y be compile depency jdk level 1.8
     */
//    public static PhiDiscover create(InetAddress addr, String hostname, String sn) throws IOException
//    {
//        return new PhiDiscoverMuticastImpl(addr, hostname, sn);
//    }
    public void setHost(DiscoveredDevice host){
        mHostInfo = host;
    }
    public abstract void startResearchDevice() throws Exception;;
    public abstract void reStart();
    public abstract void cancel();
    /**
     * Listen for services of a given type. The type has to be a fully qualified
     * type name such as <code>_http._tcp.local.</code>.
     *
     * @param listener listener for service updates
     */
    public abstract void addServiceListener(PhiDiscoverListener listener);
    /**
     * Remove listener for services of a given type.
     *
     * @param listener listener for service updates
     */
    public abstract void removeServiceListener(PhiDiscoverListener listener);

    public abstract List<DiscoveredDevice> getDevices();
}
