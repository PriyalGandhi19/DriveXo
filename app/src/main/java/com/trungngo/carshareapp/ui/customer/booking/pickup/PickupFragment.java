package com.trungngo.carshareapp.ui.customer.booking.pickup;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.trungngo.carshareapp.Constants;
import com.trungngo.carshareapp.R;
import com.trungngo.carshareapp.ui.customer.booking.BookingViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class PickupFragment extends Fragment {

    private PickupViewModel mViewModel;


    //Places autocomplete
    private PlacesClient placesClient;
    private AutocompleteSupportFragment autocompleteFragment;

    public static PickupFragment newInstance() {
        return new PickupFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pickup, container, false);
        //linkViewElements(view)
        initGooglePlacesAutocomplete();
        setActionHandlers();

        return view;
    }

    public void setActionHandlers(){
        setPlaceSelectedActionHandler();
    }


    /**
     * Init GooglePlacesAutocomplete search bar
     */
    private void initGooglePlacesAutocomplete() {
        //Init the SDK
        String apiKey = getString(R.string.google_api_key);

        if (!Places.isInitialized()) {
            Places.initialize(requireActivity().getApplicationContext(), apiKey);
        }

        this.placesClient = Places.createClient(requireActivity().getApplicationContext());

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.maps_place_autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(
                Arrays.asList(
                        Place.Field.ID,
                        Place.Field.NAME,
                        Place.Field.LAT_LNG,
                        Place.Field.ADDRESS,
                        Place.Field.ADDRESS_COMPONENTS,
                        Place.Field.PLUS_CODE
                ));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        autocompleteFragment.setOnPlaceSelectedListener(null);
    }

    /**
     * Set up a PlaceSelectionListener to handle the response
     */
    private void setPlaceSelectedActionHandler() {
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
//                smoothlyMoveCameraToPosition(place.getLatLng(), Constants.GoogleMaps.CameraZoomLevel.betweenCityAndStreets);
                //Send customer selected drop off place to booking fragment
                BookingViewModel bookingViewModel = ViewModelProviders.of(requireActivity()).get(BookingViewModel.class);
                bookingViewModel.setCustomerSelectedPickupPlace(place);
            }

            @Override
            public void onError(@NotNull Status status) {
                Toast.makeText(getActivity().getApplicationContext(),
                        Constants.ToastMessage.placeAutocompleteError + status, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PickupViewModel.class);


    }


}