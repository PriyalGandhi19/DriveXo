package com.trungngo.carshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.trungngo.carshareapp.Constants;
import com.trungngo.carshareapp.R;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    EditText birthDateEditText, usernameEditText, phoneEditText, registerVehiclePlateNumberEditText, registerAadharNumberEditText,
            registerLicenseNumberEditText, registerRCNumberEditText;
    Button backBtn, nextBtn, datePickerBtn;
    RadioButton maleRadioBtn, femaleRadioBtn;
    RadioGroup roleGroup;
    RadioButton driverRadioBtn, customerRadioBtn;
    RadioGroup transportationTypeGroup;
    RadioButton registerCarRadioBtn, registerBikeRadioBtn;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        linkViewElements();
        setRoleGroupBtnActionHandler();
        setBackBtnAction();
        setDatePickerBtnAction();
        setNextBtnAction();
        setBirthDateEditTextAutoFormat();
    }

    //Get View variables from xml id
    private void linkViewElements() {
        birthDateEditText = findViewById(R.id.registerBirthEditText);
        usernameEditText = findViewById(R.id.registerUsernameEditText);
        phoneEditText = findViewById(R.id.registerPhoneEditText);
        backBtn = findViewById(R.id.registerBackBtn);
        nextBtn = findViewById(R.id.registerFinalRegisterBtn);
        datePickerBtn = findViewById(R.id.registerPickdateBtn);
        maleRadioBtn = findViewById(R.id.registerMaleRadioBtn);
        femaleRadioBtn = findViewById(R.id.registerFemaleRadioBtn);
        roleGroup = findViewById(R.id.roleGroup);
        customerRadioBtn = findViewById(R.id.registerCustomerRadioBtn);
        driverRadioBtn = findViewById(R.id.registerDriverRadioBtn);
        transportationTypeGroup = findViewById(R.id.transportationTypeGroup);
        registerCarRadioBtn = findViewById(R.id.registerCarRadioBtn);
        registerBikeRadioBtn = findViewById(R.id.registerBikeRadioBtn);
        registerVehiclePlateNumberEditText = findViewById(R.id.registerVehiclePlateNumberEditText);
        registerAadharNumberEditText = findViewById(R.id.registerAadharNumberEditText);
        registerLicenseNumberEditText = findViewById(R.id.registerLicenseNumberEditText);
        registerRCNumberEditText = findViewById(R.id.registerRCNumberEditText);
    }

    private void setRoleGroupBtnActionHandler() {
        roleGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.registerCustomerRadioBtn:
                        transportationTypeGroup.setVisibility(View.INVISIBLE);
                        registerVehiclePlateNumberEditText.setVisibility(View.INVISIBLE);
                        registerAadharNumberEditText.setVisibility(View.INVISIBLE);
                        registerLicenseNumberEditText.setVisibility(View.INVISIBLE);
                        registerRCNumberEditText.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.registerDriverRadioBtn:
                        transportationTypeGroup.setVisibility(View.VISIBLE);
                        registerVehiclePlateNumberEditText.setVisibility(View.VISIBLE);
                        registerAadharNumberEditText.setVisibility(View.VISIBLE);
                        registerLicenseNumberEditText.setVisibility(View.VISIBLE);
                        registerRCNumberEditText.setVisibility(View.VISIBLE);
                        break;

                }
            }
        });
    }

    //Move back to startActivity when pressing 'back' button
    private void setBackBtnAction() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, StartActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    //Move to RegisterFinalActivity when pressing 'next', also passing inputted data of user
    private void setNextBtnAction() {
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String birthDate = birthDateEditText.getText().toString();
                String gender = maleRadioBtn.isChecked() ? "Male" : "Female";
                String role = customerRadioBtn.isChecked() ? "Customer" : "Driver";
                String transportationType = "";
                if (driverRadioBtn.isChecked()) {
                    transportationType = registerCarRadioBtn.isChecked() ? "car" : "bike";
                }
                String vehiclePlateNumber = registerVehiclePlateNumberEditText.getText().toString();
                String aadharNumber = registerAadharNumberEditText.getText().toString();
                String licenseNumber = registerLicenseNumberEditText.getText().toString();
                String rcNumber = registerRCNumberEditText.getText().toString();

                //Check empty input
                if (checkEmptyInput(username, phone, birthDate)) {
                    Toast.makeText(RegisterActivity.this, Constants.ToastMessage.emptyInputError, Toast.LENGTH_SHORT).show();
                } else {
                    //Transfer data
                    Intent i = new Intent(RegisterActivity.this, RegisterFinalActivity.class);
                    i.putExtra(Constants.FSUser.usernameField, username);
                    i.putExtra(Constants.FSUser.phoneField, phone);
                    i.putExtra(Constants.FSUser.birthDateField, birthDate);
                    i.putExtra(Constants.FSUser.genderField, gender);
                    i.putExtra(Constants.FSUser.roleField, role);
                    i.putExtra(Constants.FSUser.transportationType, transportationType);
                    i.putExtra(Constants.FSUser.vehiclePlateNumber, vehiclePlateNumber);
                    i.putExtra(Constants.FSUser.aadharNumber, aadharNumber);
                    i.putExtra(Constants.FSUser.licenseNumber, licenseNumber);
                    i.putExtra(Constants.FSUser.rcNumber, rcNumber);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    //Check if one of the input is empty
    private boolean checkEmptyInput(String username, String phone, String birthDate) {
        return username.isEmpty() || phone.isEmpty() || birthDate.length() < 10
                || birthDate.contains("D") || birthDate.contains("M") || birthDate.contains("Y");
    }

    //date picker dialog for birthday
    private void setDatePickerBtnAction() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(datePickerBtn.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                birthDateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

    }

    //Validation after input birth date in the edit text
    private void setBirthDateEditTextAutoFormat() {

        birthDateEditText.addTextChangedListener(new TextWatcher() {
            private String curDateStr = "";
            private final Calendar calendar = Calendar.getInstance();
            private final int tempYear = calendar.get(Calendar.YEAR);

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Take action at most 1 number is changed at a time.
                if (!s.toString().equals(curDateStr) && count == 1) {
                    //Current date string in the edit text, after latest change, without the "/" character
                    String curDateStrAfterChangedWithoutSlash = s.toString().replaceAll("[^\\d.]|\\.", "");
                    //Current date string in the edit text, before the latest change, without the "/" character
                    String curDateStrBeforeChangedWithoutSlash = curDateStr.replaceAll("[^\\d.]|\\.", "");

                    int dateStrAfterChangedLen = curDateStrAfterChangedWithoutSlash.length();
                    int cursorPos = dateStrAfterChangedLen; //Cursor position

                    for (int i = 2; i <= dateStrAfterChangedLen && i < 6; i += 2) {
                        cursorPos++;
                    }

                    //If delete the slash character "/", move cursor back 1 position
                    if (curDateStrAfterChangedWithoutSlash.equals(curDateStrBeforeChangedWithoutSlash))
                        cursorPos--;

                    //If the current date string, after latest change, without slash, is not fully filled
                    if (curDateStrAfterChangedWithoutSlash.length() < 8) {
                        String dateFormat = "DDMMYYYY";
                        //
                        curDateStrAfterChangedWithoutSlash = curDateStrAfterChangedWithoutSlash
                                + dateFormat.substring(curDateStrAfterChangedWithoutSlash.length());
                    } else {
                        //Validate and fix the input date if necessary
                        int day = Integer.parseInt(curDateStrAfterChangedWithoutSlash.substring(0, 2));
                        int month = Integer.parseInt(curDateStrAfterChangedWithoutSlash.substring(2, 4));
                        int year = Integer.parseInt(curDateStrAfterChangedWithoutSlash.substring(4, 8));

                        month = month < 1 ? 1 : Math.min(month, 12); //Max month is 12
                        calendar.set(Calendar.MONTH, month - 1);

                        year = (year < 1900) ? 1900 : Math.min(year, tempYear); //Max year for birthday is this year
                        calendar.set(Calendar.YEAR, year);

                        //Get the right day according to the input year and month
                        day = Math.min(day, calendar.getActualMaximum(Calendar.DATE));
                        curDateStrAfterChangedWithoutSlash = String.format("%02d%02d%02d", day, month, year);
                    }

                    //finalize the form of displayed date string
                    curDateStrAfterChangedWithoutSlash = String.format("%s/%s/%s", curDateStrAfterChangedWithoutSlash.substring(0, 2),
                            curDateStrAfterChangedWithoutSlash.substring(2, 4),
                            curDateStrAfterChangedWithoutSlash.substring(4, 8));

                    //Set date string as text in the EditText view and set the cursor position, update current date string
                    cursorPos = Math.max(cursorPos, 0);
                    curDateStr = curDateStrAfterChangedWithoutSlash;
                    birthDateEditText.setText(curDateStr);
                    birthDateEditText.setSelection(Math.min(cursorPos, curDateStr.length()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
