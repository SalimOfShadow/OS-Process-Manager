import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class TaskManager {

    public static void main(String[] args) {
//        ProcessUtil.listRunningProcesses();
//        ProcessUtil.killProcess("Notepad.exe");
        ProcessUtil.findProcess("Notepad.exe");
    }
}