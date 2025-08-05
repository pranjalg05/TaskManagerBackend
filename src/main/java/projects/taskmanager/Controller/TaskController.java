package projects.taskmanager.Controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import projects.taskmanager.Entity.Collection;
import projects.taskmanager.Entity.Task;
import projects.taskmanager.Repository.TaskRepository;
import projects.taskmanager.Service.CollectionService;
import projects.taskmanager.Service.TaskService;
import projects.taskmanager.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user/collections/{collectionName}/tasks")
public class TaskController {

    @Autowired
    UserService userService;

    @Autowired
    CollectionService collectionService;

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List> getAllTasksOfCollection(@PathVariable String collectionName){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Collection collection = userService.getUserCollectionByName(username, collectionName);
        if(collection==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<Task> tasks = collection.getTasks();
        if(tasks!=null&&!tasks.isEmpty())
            return new ResponseEntity<>(tasks,  HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Task> addTask(@RequestBody Task task, @PathVariable String collectionName){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Collection collection = userService.getUserCollectionByName(username, collectionName);
        if(collection==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        taskService.addNewTask(task, collection);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> changeTaskStatus(@PathVariable String collectionName, @PathVariable ObjectId taskId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Collection collection = userService.getUserCollectionByName(username, collectionName);
        if(collection==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        taskService.switchTaskStatus(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable String collectionName, @RequestBody Task task){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Collection collection = userService.getUserCollectionByName(username, collectionName);
        if(collection==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        taskService.updateTask(task);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
