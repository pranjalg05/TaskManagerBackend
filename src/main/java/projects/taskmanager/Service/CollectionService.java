package projects.taskmanager.Service;

import com.mongodb.MongoWriteException;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.taskmanager.Entity.Collection;
import projects.taskmanager.Entity.CollectionType;
import projects.taskmanager.Entity.Task;
import projects.taskmanager.Entity.User;
import projects.taskmanager.Repository.CollectionRepository;
import projects.taskmanager.Repository.TaskRepository;

@Slf4j
@Service
public class CollectionService {

    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    @Lazy
    UserService userService;

    @Autowired
    private TaskService taskService;

    public boolean saveNewCollection(Collection collection, String username){
        try {
            User user = userService.findByUsername(username);
            collection.setType(CollectionType.CUSTOM);
            Collection saved = collectionRepository.save(collection);
            user.getCollections().add(saved);
            userService.saveUser(user);
            return true;
        } catch (Exception e){
            log.error("Error creating a new Collection ", e);
            return false;
        }
    }

    public void saveCollection(Collection collection){
        collectionRepository.save(collection);
    }

    public Collection generateDailyCollection(){
        Collection daily = new Collection();
        daily.setCollectionName("Daily");
        daily.setType(CollectionType.DAILY);
        collectionRepository.save(daily);
        return daily;
    }

    public Collection getCollectionByName(String name){
        return collectionRepository.getCollectionByCollectionName(name);
    }

    public void deleteCollection(Collection collection){
        for(Task task: collection.getTasks()){
            taskService.deleteTask(task);
        }
        collectionRepository.delete(collection);
    }

    public void updateCollection(Collection old, Collection newc){
        old.setCollectionName(newc.getCollectionName());
        collectionRepository.save(old);
    }

}
