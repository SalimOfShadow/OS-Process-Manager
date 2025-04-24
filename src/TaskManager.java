import utils.ProcessUtil;

public class TaskManager {

    public static void main(String[] args) {
//        utils.ProcessUtil.listRunningProcesses();
        int processId = ProcessUtil.findProcessIdByName("dockerd");
        System.out.println(processId);
        utils.ProcessUtil.killProcessById(processId);
    }
}