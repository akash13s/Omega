package com.akash.projects.common.dfs;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Utils {

    public static String getHost() throws UnknownHostException {
        Inet4Address host = (Inet4Address) Inet4Address.getLocalHost();
        return host.getHostAddress();
    }

    public static String getLocalHost() throws UnknownHostException {
        InetAddress localhost = InetAddress.getLocalHost();
        return localhost.getHostAddress();
    }

}
