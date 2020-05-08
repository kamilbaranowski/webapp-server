package pl.kamilbaranowski.chatappserver.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import pl.kamilbaranowski.chatappserver.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

/*
    public String loginUser(User user) throws FirebaseAuthException, ExecutionException, InterruptedException {
        String uid = user.getEmail();
        String token = null;
        if(checkUserInDatabase(uid)){
            token = generateToken(user);
        }
        System.out.println("Token: " + token);
        /*DocumentReference documentReference = FirestoreClient.getFirestore().collection("users").document(String.valueOf(uid));
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot documentSnapshot = future.get();
        System.out.println(documentSnapshot.getData());
        return token;
    }
    */
    public void registerUser(User user) throws Exception {
        if (checkUserInDatabase(user.getEmail())){
            throw  new Exception("User exist");
        }
        else {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(user.getEmail())
                    .setPassword(user.getPassword())
                    .setDisabled(false);

            FirebaseAuth.getInstance().createUser(request);

            saveUserDetail(user);
        }
    }

    public void saveUserDetail(User user){
        Firestore dbf = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbf.collection("users")
                .document(user.getEmail())
                .set(user);
        //dbf.collection("users").document("uid").listCollections();
    }

    private boolean checkUserInDatabase(String uid) throws ExecutionException, InterruptedException {
        boolean ifUserInDatabase = false;
        ApiFuture<QuerySnapshot> users = FirestoreClient.getFirestore().collection("users").get();
        QuerySnapshot querySnapshot = users.get();

        for (QueryDocumentSnapshot q: querySnapshot){
            if (q.getId().equals(uid)){
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
}
