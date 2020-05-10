package pl.kamilbaranowski.chatappserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.kamilbaranowski.chatappserver.model.Message;
import pl.kamilbaranowski.chatappserver.service.FirebaseService;

import java.util.concurrent.ExecutionException;

@RestController
public class MessageController {

    private FirebaseService firebaseService;

    @Autowired
    public MessageController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping(path = "/message", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> saveMessage(Message message){
        this.firebaseService.saveMessage(message);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/message")
    public ResponseEntity<?> getMessages(@RequestParam String sender, @RequestParam String receiver){
        try {
            return ResponseEntity.ok(this.firebaseService.getMessages(sender, receiver));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
