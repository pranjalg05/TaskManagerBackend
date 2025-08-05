package projects.taskmanager.Service;

import com.mongodb.MongoWriteException;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projects.taskmanager.Entity.Collection;
import projects.taskmanager.Entity.User;
import projects.taskmanager.Repository.UserRepository;

import java.util.*;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CollectionService collectionService;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean saveNewUser(User user){
        try{
            user.setRoles(List.of("USER"));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Collection dailyCollection = collectionService.generateDailyCollection();
            user.getCollections().add(dailyCollection);
            userRepository.save(user);
            return true;
        } catch (MongoWriteException e){
            log.error("User already exits: ", e.getMessage());
            return false;
        }
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public boolean updateUser(String username, User newUser){
        try {
            User dbUser = userRepository.findUserByUsername(username);
            dbUser.setUsername(newUser.getUsername());
            dbUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            userRepository.save(dbUser);
            return true;
        } catch (MongoWriteException e){
            log.error("Username already exists: ", e.getMessage());
            return false;
        }
    }

    public User findByUsername(String username){
        return userRepository.findUserByUsername(username);
    }

    public Collection getUserCollectionByName(String username, String collectionName){
        User user = userRepository.findUserByUsername(username);
        for(Collection collection: user.getCollections())
            if(collection.getCollectionName().equals(collectionName))
                return collection;
        return null;
    }

    public void deleteUser(String username){
        User user = findByUsername(username);
        for(Collection collection: user.getCollections()){
            collectionService.deleteCollection(collection);
        }
        userRepository.delete(user);
    }

    public List<User> allUsers(){
        return userRepository.findAll();
    }

}
