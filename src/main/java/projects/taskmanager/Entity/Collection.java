package projects.taskmanager.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "collections")
public class Collection {

    @Id
    private ObjectId collectionId;
    @NonNull
    @Indexed(unique = true)
    private String collectionName;
    private CollectionType type;
    @DBRef
    private List<Task> tasks = new ArrayList<>();

}
