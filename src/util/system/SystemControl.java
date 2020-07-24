package util.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SystemControl {

    public static List<Process> getRunningProcesses() {
        List<Process> runningProcesses = new ArrayList<>();
        String osName = SystemInformation.getOSName();
        String command = getRunningProcessSystemCommand(osName);
        try {
            String line;
            java.lang.Process process = Runtime.getRuntime().exec("top -stats pid,command,mem,uid -h");
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
            while( (line = bufferedReader.readLine()) != null ) {
                int indexOfWhiteSpace = line.indexOf(" ");
                //System.out.println("PID: " + line.substring(0, indexOfWhiteSpace) );
                //System.out.println(line);

                if (!line.trim().equals("")) {
                    // keep only the process name
                    line = line.substring(1);
                    System.out.println(line);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return runningProcesses;
    }


    public static boolean killProcess(String nameProcess) {
        boolean isProcessKilled = false;
        String osName = SystemInformation.getOSName();
        String command = getKillProcessSystemCommand(osName, nameProcess);
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            if( process.exitValue() == SystemProcessEnum.EXIT_VALUE.getValue() ){
                isProcessKilled = true;
            }
        } catch (IOException e) {
            e.printStackTrace(); // ????
        } catch (InterruptedException ex) {
            ex.printStackTrace(); // ????
        } catch (NullPointerException np) {
            np.printStackTrace(); // ????
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

    private static String getRunningProcessSystemCommand(String osName) {
        String command = null;
        if( osName.toUpperCase().contains("WIN") ){
            command = "tasklist.exe /fo csv /nh";
        }else if( osName.toUpperCase().contains("MAC") ){
            command ="top";
        }
        return command;
    }

}
