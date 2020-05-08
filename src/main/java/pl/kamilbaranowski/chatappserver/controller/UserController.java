package pl.kamilbaranowski.chatappserver.controller;

import com.google.api.gax.rpc.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.kamilbaranowski.chatappserver.model.User;
import pl.kamilbaranowski.chatappserver.service.FirebaseService;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class UserController {

    private FirebaseService firebaseService;
    @Autowired
    public UserController(FirebaseService firebaseService){
        this.firebaseService = firebaseService;
    }

    @PostMapping
    public Map<String, Object> login(@RequestBody User user) throws {
        Map<String, Object> result = firebaseService.getUserDetails(user.getEmail());
        System.out.println(result);
       if (result == null) {
            throw new UsernameNotFoundException(user.getEmail() + " not found.");
       }
        return result;
    }
}
