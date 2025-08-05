package projects.taskmanager.Controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import projects.taskmanager.Entity.Collection;
import projects.taskmanager.Entity.User;
import projects.taskmanager.Repository.CollectionRepository;
import projects.taskmanager.Repository.UserRepository;
import projects.taskmanager.Service.CollectionService;
import projects.taskmanager.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user/collections")
public class CollectionController {

    @Autowired
    UserService userService;

    @Autowired
    private CollectionService collectionService;

    @GetMapping
    public ResponseEntity<List> getAllCollectionsOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<Collection> collections = user.getCollections();
        if (collections != null && !collections.isEmpty()) {
            return new ResponseEntity<>(collections, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping
    public ResponseEntity<?> addNewCollection(@RequestBody Collection collection) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (collectionService.saveNewCollection(collection, username)) {
            return new ResponseEntity<>(collection, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{collectionname}")
    public ResponseEntity<?> deleteCollection(@PathVariable String collectionName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Collection collection = userService.getUserCollectionByName(username, collectionName);
        if (collection == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        collectionService.deleteCollection(collection);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{collectionname}")
    public ResponseEntity<?> updateCollection(@PathVariable String collectionName, @RequestBody Collection newcollection) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Collection collection = userService.getUserCollectionByName(username, collectionName);
        if (collection == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        collectionService.updateCollection(collection, newcollection);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
