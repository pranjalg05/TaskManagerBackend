package projects.taskmanager.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import projects.taskmanager.Entity.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    User findUserByUsername(String username);

}
