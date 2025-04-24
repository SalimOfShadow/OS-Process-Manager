import utils.ProcessUtil;

public class TaskManager {

    public static void main(String[] args) {
//        utils.ProcessUtil.listRunningProcesses();
//        utils.ProcessUtil.killProcess("Notepad.exe");
        int processId = ProcessUtil.findProcessIdByName("dockerd");
        System.out.println(processId);
    }
}