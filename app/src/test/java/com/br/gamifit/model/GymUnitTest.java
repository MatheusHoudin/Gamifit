package com.br.gamifit.model;

import com.br.gamifit.dao_factory.FirebaseFactory;
import com.br.gamifit.database.InviteFirebaseDAO;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.AtLeast;
import org.mockito.junit.MockitoJUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GymUnitTest {

    @Test
    public void createInviteTest(){
        Gym gym = new Gym("Fitness Everyhere","f9bfp43");
        User user = mock(User.class);
        when(user.getName()).thenReturn("Matheus Gomes");
        when(user.getEmail()).thenReturn("msau@gmal.com");
        when(user.getPassword()).thenReturn("nnfo5tnb");

        GymInvite expectedInvite = mock(GymInvite.class);
        when(expectedInvite.getGym()).thenReturn(gym);
        when(expectedInvite.getUser()).thenReturn(user);

        GymInvite createdInvite = gym.createInviteToJoin(user);

        assertEquals(expectedInvite.getGym(),createdInvite.getGym());
        assertEquals(expectedInvite.getUser(),createdInvite.getUser());
    }

    @Test
    public void sendInviteToJoinTest(){
        Gym gym = new Gym("Fitness Everyhere","f9bfp43");

        GymInvite gymInvite = mock(GymInvite.class);
        User user = mock(User.class);
        when(user.getName()).thenReturn("Matheus Gomes");
        when(user.getEmail()).thenReturn("matheusdin04@gmail.com");
        when(user.getPassword()).thenReturn("msabd9334");
        when(gymInvite.getUser()).thenReturn(user);
        when(gymInvite.getGym()).thenReturn(gym);

        InviteFirebaseDAO inviteFirebaseDAO = mock(InviteFirebaseDAO.class);
        FirebaseDatabase firebaseDatabase = mock(FirebaseDatabase.class);
        DatabaseReference databaseReference = mock(DatabaseReference.class);
        FirebaseFactory firebaseFactory = mock(FirebaseFactory.class);

        when(firebaseFactory.getInviteFirebaseDAO()).thenReturn(inviteFirebaseDAO);
        when(firebaseDatabase.getReference()).thenReturn(databaseReference);
        when(databaseReference.child("invite")).thenReturn(databaseReference);
        when(databaseReference.push()).thenReturn(databaseReference);
        when(databaseReference.getKey()).thenReturn("-adnV9YSYFo3");
        when(FirebaseDatabase.getInstance()).thenReturn(firebaseDatabase);
        when(inviteFirebaseDAO.createInvite(gymInvite)).thenReturn(true);

        boolean result = gym.sendInviteToJoin(user);
        verify(inviteFirebaseDAO,atLeast(1)).createInvite(gymInvite);
        assertEquals(true,result);

    }

}
