package cloud.elit.sdk.api;

public class TaskDependency {
    private String task;
    private String tool;

    public TaskDependency(String task, String tool) {
        setTask(task);
        setFramework(tool);
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getFramework() {
        return tool;
    }

    public void setFramework(String tool) {
        this.tool = tool;
    }
}
