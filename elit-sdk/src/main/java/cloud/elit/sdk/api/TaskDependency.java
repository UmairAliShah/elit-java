package cloud.elit.sdk.api;

public class TaskDependency {
    private String task;
    private String tool;

    public TaskDependency(String task, String tool) {
        setTask(task);
        setTool(tool);
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }
}
