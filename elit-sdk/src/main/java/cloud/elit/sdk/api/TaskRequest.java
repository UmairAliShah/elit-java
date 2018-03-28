package cloud.elit.sdk.api;

import java.util.List;

public class TaskRequest {
    private String input;
    private String task;
    private String tool;
    private TaskDependency[] dependencies;

    public TaskRequest(String input, String task, String tool, TaskDependency... dependencies) {
        setInput(input);
        setTask(task);
        setFramework(tool);
        setDependencies(dependencies);
    }

    public TaskRequest(String input, String task, String tool, List<TaskDependency> dependencies) {
        setInput(input);
        setTask(task);
        setFramework(tool);
        setDependencies(dependencies);
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
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

    public TaskDependency[] getDependencies() {
        return dependencies;
    }

    public void setDependencies(TaskDependency[] dependencies) {
        this.dependencies = dependencies;
    }

    public void setDependencies(List<TaskDependency> dependencies) {
        this.dependencies = dependencies.toArray(new TaskDependency[dependencies.size()]);
    }
}
