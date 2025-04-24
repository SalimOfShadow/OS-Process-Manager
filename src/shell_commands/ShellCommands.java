package shell_commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellCommands {
    public static BufferedReader listProcesses(String os) {
        try {
            String command = (os.equals("Linux") || os.equals("Mac")) ? "ps -e" : "tasklist";
            Process process = Runtime.getRuntime().exec(command);
            System.out.println(command);
            return new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error listing processes: " + e.getMessage());
        }
        return null;
    }

    public static BufferedReader findProcess(String os, String processName) {
        try {
            String command = (os.equals("Linux") || os.equals("Mac")) ? "pgrep " + processName : "tasklist /fi \"imagename eq " + processName + "\"";
            Process process = Runtime.getRuntime().exec(command);
            System.out.println(command);
            return new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (IOException e) {
            System.out.println("Failed to find the selected process: " + e.getMessage());
        }
        return null;
    }

    public static BufferedReader killProcess(String os, int processId) {
        try {
            String command = (os.equals("Linux") || os.equals("Mac")) ? "kill -9 " + processId : "taskkill /IM \"" + processId + "\" /F";
            Process process = Runtime.getRuntime().exec(command);
            return new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (IOException e) {
            System.out.println("Failed to find the selected process: " + e.getMessage());
        }
        return null;
    }

    public static BufferedReader killProcessAsAdmin(String os, int processId) {
        try {
            // TODO - Prompt the user to type it's sudo pwd on Linux
            String command = (os.equals("Linux") || os.equals("Mac")) ? "sudo kill -9 " + processId : "powershell.exe Start-Process cmd -ArgumentList '/c taskkill /IM " + processId + " /F' -Verb RunAs";
            Process process = Runtime.getRuntime().exec(command);
            return new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (IOException e) {
            System.out.println("Failed to kill the selected process: " + e.getMessage());
        }
        return null;
    }
}

