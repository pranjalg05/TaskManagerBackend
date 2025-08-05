package projects.taskmanager.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import projects.taskmanager.Entity.User;
import projects.taskmanager.Service.UserDetailsServiceImpl;
import projects.taskmanager.Service.UserService;
import projects.taskmanager.Utils.JwtUtil;

@Slf4j
@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AuthenticationManager manager;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthcheck() {
        System.out.println(">>> Health check hit");
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<User> createUser(@RequestBody User user){
        if(userService.saveNewUser(user)){
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){
        try {
            manager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    ));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e){
            log.error("Incorrect Username/Password: " ,e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}

