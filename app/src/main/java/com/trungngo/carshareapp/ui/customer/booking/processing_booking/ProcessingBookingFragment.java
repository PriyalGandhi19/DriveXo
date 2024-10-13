package com.trungngo.carshareapp.ui.customer.booking.processing_booking;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.trungngo.carshareapp.R;
import com.trungngo.carshareapp.ui.customer.booking.BookingViewModel;

public class ProcessingBookingFragment extends Fragment {

    private ProcessingBookingViewModel mViewModel;

    private TextView originTextView;
    private TextView destinationTextView;
    private TextView priceTextView;
    private Button cancelBookingBtn;

    public static ProcessingBookingFragment newInstance() {
        return new ProcessingBookingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_processing_booking, container, false);
        linkViewElements(view);
        setActionHandlers();
        return view;
    }

    private void setActionHandlers() {
        cancelBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookingViewModel bookingViewModel = ViewModelProviders.of(requireActivity()).get(BookingViewModel.class);
                bookingViewModel.setCancelBookingBtnPressed(true);
            }
        });
    }

    /**
     * Link view elements from xml file
     * @param rootView
     */
    private void linkViewElements(View rootView) {
        originTextView = rootView.findViewById(R.id.originTextView);
        destinationTextView = rootView.findViewById(R.id.destinationTextView);
        priceTextView = rootView.findViewById(R.id.priceTextView);
        cancelBookingBtn = rootView.findViewById(R.id.cancelBookingBtn);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(ProcessingBookingViewModel.class);
        mViewModel.getDropOffPlaceString().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                destinationTextView.setText(s);
            }
        });

        mViewModel.getPickupPlaceString().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                originTextView.setText(s);
            }
        });

        mViewModel.getPriceInVNDString().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                priceTextView.setText(s);
            }
        });
    }

}