package utils;

import shell_commands.Shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessUtil {
    public static String currentOs = System.getProperty("os.name");

    public static void listRunningProcesses() {
        try {
            BufferedReader reader = Shell.listProcesses(currentOs);
            String line;
            boolean firstLine = true;
            while (true) {
                assert reader != null;
                if ((line = reader.readLine()) == null) break;
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] processInfo = line.split("\\s+");
                if (processInfo.length > 0) {
                    String processName = (currentOs.equals("Linux") || currentOs.equals("Mac")) ? processInfo[4] : processInfo[0];
                    System.out.println(processName);
                }
            }
        } catch (IOException e) {
            System.out.println("Error listing processes: " + e.getMessage());
        }
    }

    public static void findProcess(String processName) {
        String command = "tasklist /fi \"imagename eq " + processName + "\"";
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            boolean found = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains(processName)) {
                    System.out.println("Found process: " + line.split(" ")[0]);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Process not found: " + processName);
            }

        } catch (IOException e) {
            System.out.println("Failed to find the selected process: " + e.getMessage());
        }
    }

    public static void killProcess(String processName) {
        String command = "taskkill /IM " + processName + " /F";
        String psCommand = "powershell.exe Start-Process cmd -ArgumentList '/c taskkill /IM " + processName + " /F' -Verb RunAs";
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();

            if (line != null && line.contains("SUCCESS: The process ")) {
                System.out.println("Successfully terminated the process");
            } else {
                System.out.println("Normal termination failed. Attempting to elevate process...");
                Process psProcess = Runtime.getRuntime().exec(psCommand);
                BufferedReader psReader = new BufferedReader(new InputStreamReader(psProcess.getInputStream()));
                String psLine;

                while ((psLine = psReader.readLine()) != null) {
                    System.out.println(psLine);
                }
                System.out.println("Successfully terminated the process with elevated privileges.");
            }

        } catch (IOException e) {
            System.out.println("Failed to close the given process. Please make sure the program is running.");
        }
    }
}



