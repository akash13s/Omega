package com.akash.projects.common.dfs;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Utils {

    public static String getHost() throws UnknownHostException {
        InetAddress localhost = InetAddress.getLocalHost();
        return localhost.getHostAddress();
    }

}
