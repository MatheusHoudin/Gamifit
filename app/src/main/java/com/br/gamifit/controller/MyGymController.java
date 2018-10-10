package com.br.gamifit.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import com.br.gamifit.R;
import com.br.gamifit.activity.MyGymActivity;
import com.br.gamifit.dao_factory.FirebaseFactory;
import com.br.gamifit.database.UserFirebaseDAO;
import com.br.gamifit.model.Gym;
import com.br.gamifit.model.User;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.Observable;
import java.util.Observer;

public class MyGymController implements Observer{
    private static MyGymController myGymController;

    private Gym gym;
    private Context context;
    private MyGymActivity myGymActivity;

    private MyGymController(@NonNull MyGymActivity myGymActivity, Gym gym){
        this.myGymActivity = myGymActivity;
        this.context = myGymActivity.getApplicationContext();
        this.gym = gym;
    }

    public static MyGymController getGymController(MyGymActivity myGymActivity, Gym gym) {
        if(myGymController ==null){
            myGymController = new MyGymController(myGymActivity,gym);
            setupObservable();
        }
        return myGymController;
    }

    private static void setupObservable(){
        UserFirebaseDAO userFirebaseDAO = FirebaseFactory.getUserFirebaseDAO();
        userFirebaseDAO.addObserver(myGymController);
    }

    /**
     * This method configures an QRCode reader, it sets its title,camera and so on
     * When this code is performed the front camera is opened and you can scan an QRCode
     * The result of it is sent to MyGymActivity(myGymActivity below)
     */
    public void openScanCodeActivity(){
        IntentIntegrator integrator = new IntentIntegrator(myGymActivity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt(context.getResources().getString(R.string.qrcode_text));
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    public void sendInviteToUser(String scannedUserCode){
        UserFirebaseDAO userFirebaseDAO = FirebaseFactory.getUserFirebaseDAO();
        userFirebaseDAO.getScannedUser(scannedUserCode);
    }

    @Override
    public void update(Observable observable, Object o) {
        if(o instanceof User){
            if(observable instanceof UserFirebaseDAO){
                User userToInvite = (User) o;
                createAndSendInviteToUser(userToInvite);
            }
        }
    }

    private void createAndSendInviteToUser(User userToInvite){
        boolean result = gym.sendInviteToJoin(userToInvite);
        if(result){
            myGymActivity.showToastMessage("Convite enviado com sucesso");
        }else{
            myGymActivity.showToastMessage("Convite não enviado com sucesso");
        }
    }
}