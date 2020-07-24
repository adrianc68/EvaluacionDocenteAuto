package util.system;

import java.util.ArrayList;
import java.util.List;

public class SystemInformation {

    public static String getOSName() {
        String osName = java.lang.System.getProperty("os.name");
        return osName;
    }
    
    public static String getJavaVersion() {
        String javaVersion = java.lang.System.getProperty("java.version");
        return javaVersion;
    }

    public static String getOsVersion() {
        String osVersion = java.lang.System.getProperty("os.version");
        return osVersion;
    }

}
