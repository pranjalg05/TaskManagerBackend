package projects.taskmanager.Service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.taskmanager.Entity.Collection;
import projects.taskmanager.Entity.Task;
import projects.taskmanager.Repository.TaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    @Lazy
    CollectionService collectionService;

    public void addNewTask(Task task, Collection collection){
        task.setCompleted(false);
        task.setDateCreated(LocalDate.now());
        taskRepository.save(task);
        collection.getTasks().add(task);
        collectionService.saveCollection(collection);
    }

    public void deleteTask(Task task){
        taskRepository.delete(task);
    }

    public void switchTaskStatus(ObjectId taskId){
        Task task = taskRepository.getTaskByTaskID(taskId);
        task.setCompleted(!task.isCompleted());
        taskRepository.save(task);
    }

    public void updateTask(Task task){
        Task oldTask = taskRepository.getTaskByTaskID(task.getTaskID());
        oldTask.setCompleted(task.isCompleted());
        oldTask.setTitle(task.getTitle());
        taskRepository.save(task);
    }

}
