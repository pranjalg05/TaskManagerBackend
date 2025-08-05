package projects.taskmanager.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import projects.taskmanager.Entity.Task;

public interface TaskRepository extends MongoRepository<Task, ObjectId> {

    Task getTaskByTaskID(ObjectId taskId);

}
