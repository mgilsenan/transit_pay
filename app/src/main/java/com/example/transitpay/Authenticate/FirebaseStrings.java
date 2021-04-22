package com.example.transitpay.Authenticate;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseStrings {

    private final static String firebasePhone = "phone";
    private final static String firebaseName = "name";
    private final static String firebaseUser = "user";
    private final static String firebaseEmail = "email";
    private final static String firebaseLoginBefore = "loginBefore";
    private final static String getFirebaseEmailVerified = "emailVerified";
    private final static String FirebaseValidCard = "Valid card";
    private final static String firebasePassword = "password";


    public static String getFirebasePassword() {
        return firebasePassword;

    }
    public static String getFirebaseName() {
        return firebaseName;
    }




    public static String getGetFirebaseValidCard() {
        return FirebaseValidCard;
    }


    public static String getFirebaseEmail() {
        return firebaseEmail;
    }

    public static String getFirebaseLoginBefore() {
        return firebaseLoginBefore;
    }

    public static String getGetFirebaseEmailVerified() {
        return getFirebaseEmailVerified;
    }

    public static String getFirebasePhone(){return firebasePhone;}

    public static String getFirebaseUser(){return firebaseUser;}


    // return the root of real time data bases.
    public static DatabaseReference getRef(){

        return FirebaseDatabase.getInstance().getReference(firebaseUser);
    }
}
