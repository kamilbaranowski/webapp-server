package pl.kamilbaranowski.chatappserver.controller;

import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kamilbaranowski.chatappserver.exception.model.InvalidEmailException;
import pl.kamilbaranowski.chatappserver.model.User;
import pl.kamilbaranowski.chatappserver.service.FirebaseService;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class UserController {

    private FirebaseService firebaseService;
    @Autowired
    public UserController(FirebaseService firebaseService){
        this.firebaseService = firebaseService;
    }


    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> register(User user) throws InvalidEmailException {
        System.out.println("Registration user: " + user.toString());
        try {
            firebaseService.registerUser(user);
        } catch (Exception e) {
            throw new InvalidEmailException("There is username with given email");
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping(consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public Map<String, String> login(User user) throws ExecutionException, InterruptedException, FirebaseAuthException {
        return firebaseService.loginUser(user);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(){
        try {
            return ResponseEntity.ok(firebaseService.getAllUsers());
        } catch (ExecutionException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/users", params = {"uid"})
    public ResponseEntity<?> getUserByUid(@RequestParam String uid) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(firebaseService.getUsernameByUid(uid));
    }
}
