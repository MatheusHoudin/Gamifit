package com.br.gamifit.database;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.br.gamifit.dao_factory.FirebaseFactory;
import com.br.gamifit.database.dao_interface.IUserDAO;
import com.br.gamifit.model.ObserverResponse;
import com.br.gamifit.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class UserFirebaseDAO extends Observable implements IUserDAO {
    private DatabaseReference firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    public static final Integer OPERATION_DONE_SUCESSFULLY = 1;
    public static final Integer OPERATION_CREATE_USER_ACCOUNT = 2;
    public static final Integer OPERATION_SAVE_USER = 3;

    public UserFirebaseDAO(){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void getAllUsers() {
        firebaseDatabase.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);
                    setChanged();
                    notifyObservers(user);
                }
                notifyObservers(OPERATION_DONE_SUCESSFULLY);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void getUsersByTheirName(String name) {
        firebaseDatabase.child("user").orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);
                    setChanged();
                    notifyObservers(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getUser(String email) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("user").orderByChild("email").equalTo(email).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        User caughtUser = data.getValue(User.class);
                        ObserverResponse response = new ObserverResponse("getUser",caughtUser);
                        setChanged();
                        notifyObservers(response);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getScannedUser(String scannedUserCode) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("user").orderByChild("code").equalTo(scannedUserCode).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        User caughtUser = data.getValue(User.class);
                        ObserverResponse response = new ObserverResponse("getScannedUser",caughtUser);
                        setChanged();
                        notifyObservers(response);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void createUserAcount(final User user) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Exception exception = task.getException();
                OperationResult operationResult = new OperationResult(exception,OPERATION_CREATE_USER_ACCOUNT);
                setChanged();
                notifyObservers(operationResult);
            }
        });
    }

    @Override
    public void saveUser(User user) {
        user.setCode(firebaseDatabase.child("user").push().getKey());
        firebaseDatabase.child("user").child(user.getCode()).setValue(user).addOnCompleteListener(
                new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Exception exception = task.getException();
                OperationResult operationResult = new OperationResult(exception,OPERATION_SAVE_USER);
                setChanged();
                notifyObservers(operationResult);
            }
        });
    }

    public void updateUserToken(String token,User user){
        firebaseDatabase.child("user/"+user.getCode()+"/token").setValue(token);
    }

}
