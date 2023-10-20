package algebra.hr.dal.entity;

import algebra.hr.dal.enums.TaskType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Task")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TaskID", updatable = false, nullable = false)
    private int taskID;

    @Enumerated(EnumType.STRING)
    @Column(name = "TaskType", nullable = false)
    private TaskType taskType;

    @ManyToMany(mappedBy = "tasks")
    private List<Quiz> quizzes = new ArrayList<>();

    @Column(name = "TaskText", nullable = false, length = 1024)
    private String taskText;

    public Task() {
    }

    public Task(TaskType taskType, List<Quiz> quizzes, String taskText) {
        this.taskType = taskType;
        this.quizzes = quizzes;
        this.taskText = taskText;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
}