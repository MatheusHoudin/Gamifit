package com.br.gamifit.model;

import com.br.gamifit.dao_factory.FirebaseFactory;
import com.br.gamifit.database.ProfileFirebaseDAO;

public class Profile {
    private User user;
    private Gym gym;
    private Progress progress;
    private boolean active;

    public Profile(){}

    public Profile(Gym gym,User user,Progress progress){
        this.setGym(gym);
        this.setUser(user);
        this.setProgress(progress);
        this.setActive(true);
    }

    public boolean save(){
        ProfileFirebaseDAO profileFirebaseDAO = FirebaseFactory.getProfileFirebaseDAO();
        return profileFirebaseDAO.createProfile(this);
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Gym getGym() {
        return gym;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode()==obj.hashCode();
    }

    @Override
    public int hashCode() {
        return gym.hashCode()+user.hashCode();
    }
}
