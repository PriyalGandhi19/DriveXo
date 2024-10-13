package com.trungngo.carshareapp.ui.customer.booking.popup_driver_arrived;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.trungngo.carshareapp.R;
import com.trungngo.carshareapp.model.User;

public class PopupDriverArrivalFragment extends DialogFragment {

    private PopupDriverArrivalViewModel mViewModel;

    private TextView driverUsernameTextView;
    private TextView vehicleInfo;

    private Button closeBtn;

    public static PopupDriverArrivalFragment newInstance() {
        return new PopupDriverArrivalFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pop_up_driver_arrival, container, false);
        linkViewElements(view);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    /**
     * Link view elements from xml file
     * @param rootView
     */
    private void linkViewElements(View rootView){
        driverUsernameTextView = rootView.findViewById(R.id.driverUsernameTextView);
        vehicleInfo = rootView.findViewById(R.id.vehicleInfo);
        closeBtn = rootView.findViewById(R.id.closeBtn);
    }

    /**
     * Render driver information to view
     * @param driver
     */
    @SuppressLint("SetTextI18n")
    private void setDriverInfo(User driver){
        driverUsernameTextView.setText(driver.getUsername());
        vehicleInfo.setText(driver.getVehiclePlateNumber() + " ● " + driver.getTransportationType());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(PopupDriverArrivalViewModel.class);
        mViewModel.getDriver().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                setDriverInfo(user);
            }
        });
    }

}