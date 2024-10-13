package com.trungngo.carshareapp.ui.driver.process_booking;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentReference;
import com.trungngo.carshareapp.model.Booking;
import com.trungngo.carshareapp.model.User;

public class DriverProcessBookingViewModel extends ViewModel {
    private MutableLiveData<User> currentUserObject;
    private MutableLiveData<DocumentReference> currentBookingDocRef;
    private MutableLiveData<Boolean> checkoutDone;

    public DriverProcessBookingViewModel() {
        currentUserObject = new MutableLiveData<>();
        currentBookingDocRef = new MutableLiveData<>();
        checkoutDone = new MutableLiveData<>();
    }

    public void setCurrentUserObject(User currentUserObject) {
        this.currentUserObject.setValue(currentUserObject);
    }

    public void setCheckoutDone(Boolean checkoutDone) {
        this.checkoutDone.setValue(checkoutDone);
    }

    public MutableLiveData<Boolean> getCheckoutDone() {
        return checkoutDone;
    }

    public void setCurrentBookingDocRef(DocumentReference currentBookingDocRef) {
        this.currentBookingDocRef.setValue(currentBookingDocRef);
    }

    public MutableLiveData<User> getCurrentUserObject(){
        return this.currentUserObject;
    }

    public MutableLiveData<DocumentReference> getCurrentBookingDocRef() {
        return currentBookingDocRef;
    }
}
