package com.akash.projects.common.dfs;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class Utils {

    public static String getHost() throws UnknownHostException {
        Inet4Address localhost = (Inet4Address) Inet4Address.getLocalHost();
        return localhost.getHostAddress();
    }

}
