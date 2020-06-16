package pl.kamilbaranowski.chatappserver.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import pl.kamilbaranowski.chatappserver.model.Message;
import pl.kamilbaranowski.chatappserver.model.User;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    public Map<String, Map<String, Object>> getAllUsers() throws ExecutionException, InterruptedException {
        Map<String, Map<String, Object>> allUsers = new HashMap<>();
        ApiFuture<QuerySnapshot> users = FirestoreClient.getFirestore().collection("users").get();
        QuerySnapshot querySnapshot = users.get();
        querySnapshot.forEach((userSnapshot) -> {
            allUsers.put(userSnapshot.getId(), userSnapshot.getData());

        });
        return allUsers;
    }

    public Map<String, Object> getMessages(String sender, String receiver) throws ExecutionException, InterruptedException {
        System.out.println("Sender: " + sender + "\nReceiver: " + receiver);
        Map<String, Object> messages = new HashMap<>();
        //ApiFuture<QuerySnapshot> messages = FirestoreClient.getFirestore().collection("messages").document(sender).collection(receiver).get();
        FirestoreClient.getFirestore().collection("messages")
                .document(sender)
                .collection(receiver)
                .listDocuments().forEach((document) -> {
                    messages.put(document.getId(), document.toString());
            System.out.println("asdsad");
        });
        System.out.println("Messages: " + messages);


        return messages;
    }


    public Map<String, String> loginUser(User user) throws FirebaseAuthException, ExecutionException, InterruptedException {
        Map<String, String> jsonToken = new HashMap<>();
        String email = user.getEmail();
        System.out.println(email);
        String token = null;
        String uid = getUserUid(user);
        if(checkUserInDatabase(email)){
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", "user");
            claims.put("customExp", System.currentTimeMillis() + 43200); //12h
            token = FirebaseAuth.getInstance().createCustomToken(uid, claims);
            jsonToken.put("token", token);
        }
        else {
            System.out.println("No such user in database");
        }



        System.out.println("Token: " + token);
        return jsonToken;
    }

    public String getUserUid(User user) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> users = FirestoreClient.getFirestore().collection("users").get();
        QuerySnapshot querySnapshot = users.get();
        String uid = null;
        for (QueryDocumentSnapshot q: querySnapshot){
            if (q.getData().get("email").equals(user.getEmail()) && q.getData().get("password").equals(user.getPassword())){
                uid = q.getId();
                break;
            }
        }
        System.out.println("User UID: " + uid + "\nGiven user: " + user.toString());
        return uid;
    }

    public void registerUser(User user) throws Exception {
        if (checkUserInDatabase(user.getEmail())){
            throw  new Exception("User exist");
        }
        else {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(user.getEmail())
                    .setPassword(user.getPassword())
                    .setDisabled(false);
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            String uid = userRecord.getUid();

            saveUserDetail(new User(uid, user.getEmail(), user.getUsername(), user.getPassword(), user.getStatus()));
        }
    }

    public void saveMessage(Message message){
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = firestore.collection("messages")
                .document(message.getSender())
                .collection(message.getReceiver())
                .document(message.getTimestamp().toString())
                .set(message);
    }

    public void saveUserDetail(User user){
        Firestore dbf = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbf.collection("users")
                .document(user.getUid())
                .set(user);
    }

    private boolean checkUserInDatabase(String email) throws ExecutionException, InterruptedException {
        boolean ifUserInDatabase = false;
        ApiFuture<QuerySnapshot> users = FirestoreClient.getFirestore().collection("users").get();
        QuerySnapshot querySnapshot = users.get();

        for (QueryDocumentSnapshot q: querySnapshot){
            if (q.getData().get("email").equals(email)){
                ifUserInDatabase = true;
                break;
            }
        }
        return ifUserInDatabase;
    }

    public Map<String, Object> getUserDetails(String email) throws ExecutionException, InterruptedException {
        Map<String, Object> user = null;
        ApiFuture<QuerySnapshot> users = FirestoreClient.getFirestore().collection("users").get();
        QuerySnapshot querySnapshot = users.get();

        for (QueryDocumentSnapshot q: querySnapshot){
            if (q.getId().equals(email)){
                user = q.getData();
                break;
            }
        }
        return user;
    }

    public Map<String, Object> getUsernameByUid(String uid) throws ExecutionException, InterruptedException {
        System.out.println("UID: " + uid);
        Map<String, Object> user = new HashMap<>();
        //ApiFuture<QuerySnapshot> messages = FirestoreClient.getFirestore().collection("messages").document(sender).collection(receiver).get();
        user = FirestoreClient.getFirestore().collection("users")
                .document(uid)
                .get()
                .get()
                .getData();

        System.out.println("User: " + user.toString());
    return user;
    }
}
