package util;

public class SystemInformation {

    public static String getOSName() {
        String osName = System.getProperty("os.name");
        return osName;
    }

    public static String getJavaVersion() {
        String javaVersion = System.getProperty("java.version");
        return javaVersion;
    }

    public static String getOsVersion() {
        String osVersion = System.getProperty("os.version");
        return osVersion;
    }

}
