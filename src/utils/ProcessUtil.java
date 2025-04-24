package utils;

import shell_commands.ShellCommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessUtil {
    public static String currentOs = System.getProperty("os.name");

    public static void listRunningProcesses() {
        try {
            BufferedReader reader = ShellCommands.listProcesses(currentOs);
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

    public static int findProcessIdByName(String processName) {
        try {
            BufferedReader reader = ShellCommands.findProcess(currentOs, processName);
            String line;
            boolean headerSkipped = false;

            while (true) {
                assert reader != null;
                if ((line = reader.readLine()) == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;
                // Parse Windows response table
                if (currentOs.toLowerCase().contains("windows")) {
                    if (!headerSkipped) {
                        if (line.startsWith("=")) {
                            headerSkipped = true;
                        }
                        continue;
                    }

                    if (line.toLowerCase().startsWith(processName.toLowerCase())) {
                        String[] parts = line.split("\\s+");
                        if (parts.length >= 2) {
                            System.out.println("Found process: " + parts[1]);
                            return Integer.parseInt(parts[1]);
                        }
                    }

                } else {
                    // For Linux/macOS: pgrep output is just the PID
                    if (line.matches("\\d+")) {
                        System.out.println("Found process: " + line);
                        return Integer.parseInt(line);
                    }
                }
            }
            System.out.println("Process not found: " + processName);
            return -1;
        } catch (IOException e) {
            System.out.println("Failed to find the selected process: " + e.getMessage());
            return -1;
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



