package projects.taskmanager.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import projects.taskmanager.Entity.Collection;

public interface CollectionRepository extends MongoRepository<Collection, ObjectId> {

    public Collection getCollectionByCollectionName(String collectionName);

}
