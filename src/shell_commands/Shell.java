package shell_commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Shell {
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
}

