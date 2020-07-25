package util;

import java.io.IOException;

public class SystemControl {

    public static boolean killProcess(String nameProcess) throws IOException, InterruptedException {
        boolean isProcessKilled = false;
        String osName = SystemInformation.getOSName();
        String command = getKillProcessSystemCommand(osName, nameProcess);
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            if( process.exitValue() == SystemProcessEnum.EXIT_VALUE.getValue() ){
                isProcessKilled = true;
            }
        } catch (IOException | InterruptedException | NullPointerException e) {
            throw e;
        }
        return isProcessKilled;
    }

    private static String getKillProcessSystemCommand(String osName, String nameProcess) {
        String command = null;
        if( osName.toUpperCase().contains("WIN") ){
            command = "taskkill /f /t /im " + nameProcess;
        }else if( osName.toUpperCase().contains("MAC") ){
            command ="killall " + nameProcess;
        }
        return command;
    }

}
