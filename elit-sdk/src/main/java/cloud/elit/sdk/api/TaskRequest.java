/*
 * Copyright 2018 Emory University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        setTool(tool);
        setDependencies(dependencies);
    }

    public TaskRequest(String input, String task, String tool, List<TaskDependency> dependencies) {
        setInput(input);
        setTask(task);
        setTool(tool);
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

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
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
