package ptr.hf.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ptr.hf.R;
import ptr.hf.model.ReservationResponse;
import ptr.hf.network.ApiHelper;
import ptr.hf.network.ErrorResponse;
import ptr.hf.network.IApiFinishedListener;
import ptr.hf.network.IApiResultListener;
import ptr.hf.ui.auth.LoginActivity;
import ptr.hf.ui.map.MapFragment;

public class ReservationFragment extends Fragment {

    //    @BindView(R.id.edittext_chargerid)
//    EditText edittextChargerid;
    @BindView(R.id.datepicker_from)
    Button datepickerFrom;
    @BindView(R.id.timepicker_from)
    Button timepickerFrom;
    @BindView(R.id.datepicker_to)
    Button datepickerTo;
    @BindView(R.id.timepicker_to)
    Button timepickerTo;
    @BindView(R.id.send_reservation)
    Button sendReservation;
    Unbinder unbinder;

    private int yearFrom, monthFrom, dayFrom, hourFrom, minuteFrom;
    private int yearTo, monthTo, dayTo, hourTo, minuteTo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_reservation, container, false);

        View view = inflater.inflate(R.layout.fragment_reservation, container, false);
        unbinder = ButterKnife.bind(this, view);

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        yearFrom = currentDate.getYear();
        monthFrom = currentDate.getMonth();
        dayFrom = currentDate.getDay();
        hourFrom = currentDate.getHours();
        minuteFrom = currentDate.getMinutes();
        yearTo = currentDate.getYear();
        monthTo = currentDate.getMonth();
        dayTo = currentDate.getDay();
        hourTo = currentDate.getHours();


//        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday


        datepickerFrom.setText((String) DateFormat.format("yyyy-MM-dd", currentDate));
        datepickerTo.setText((String) DateFormat.format("yyyy-MM-dd", currentDate));
        timepickerFrom.setText((String) DateFormat.format("HH:mm", currentDate));
        calendar.add(Calendar.MINUTE, 30);
        minuteTo = calendar.getTime().getMinutes();
        timepickerTo.setText((String) DateFormat.format("HH:mm", calendar.getTime()));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    @OnClick(R.id.datepicker)
//    public void showTimePickerDialog(View v) {
//        DialogFragment newFragment = new TimePickerFragment();
//        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
//    }

    @OnClick(R.id.send_reservation)
    public void onViewClicked() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.i("resetvation:", "93815" + " " +
                user.getEmail() + " " +
                (int) getDate(yearFrom, monthFrom, dayFrom, hourFrom, minuteFrom, 0).getTime() / 1000 +
                (int) getDate(yearTo, monthTo, dayTo, hourTo, minuteTo, 0).getTime() / 1000);
        ApiHelper
                .INSTANCE
                .postReservation(
                        "93815",
                        user.getEmail(),
                        (int) getDate(yearFrom, monthFrom, dayFrom, hourFrom, minuteFrom, 0).getTime() / 1000,
                        (int) getDate(yearTo, monthTo, dayTo, hourTo, minuteTo, 0).getTime() / 1000,
                        new IApiResultListener<ArrayList<ReservationResponse>>() {
                            @Override
                            public void success(ArrayList<ReservationResponse> result) {
                                Snackbar
                                        .make(getActivity().findViewById(android.R.id.content),
                                                "Sikeres foglalás!",
                                                Snackbar.LENGTH_LONG)
                                        .show();
                                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.container, new MapFragment());
                                ft.commit();
                            }

                            @Override
                            public void error(ErrorResponse errorResponse) {
                                Snackbar
                                        .make(getActivity().findViewById(android.R.id.content),
                                                "Sikeres foglalás!",
                                                Snackbar.LENGTH_LONG)
                                        .show();
                                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.container, new MapFragment());
                                ft.commit();
                            }

                            @Override
                            public void fail() {
                                Snackbar
                                        .make(getActivity().findViewById(android.R.id.content),
                                                "Sikeres foglalás!",
                                                Snackbar.LENGTH_LONG)
                                        .show();
                                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.container, new MapFragment());
                                ft.commit();
                            }
                        }
//                        new IApiFinishedListener() {
//                            @Override
//                            public void success() {
//                                Snackbar
//                                        .make(getActivity().findViewById(android.R.id.content),
//                                                "Sikeres foglalás!",
//                                                Snackbar.LENGTH_LONG)
//                                        .show();
//                                final FragmentTransaction ft = getFragmentManager().beginTransaction();
//                                ft.replace(R.id.container, new MapFragment());
//                                ft.commit();
//                            }
                );
    }

    public static Date getDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    @OnClick({R.id.datepicker_from, R.id.timepicker_from, R.id.datepicker_to, R.id.timepicker_to})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.datepicker_from:
                DatePickerFragment dialogFragmentDateFrom = new DatePickerFragment();
                dialogFragmentDateFrom.show(getActivity().getSupportFragmentManager(), "datePicker");
                dialogFragmentDateFrom.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datepickerFrom.setText(Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(dayOfMonth));
                        yearFrom = year;
                        monthFrom = month;
                        dayFrom = dayOfMonth;
                    }
                });
                break;
            case R.id.timepicker_from:
                TimePickerFragment dialogFragmentTimeFrom = new TimePickerFragment();
                dialogFragmentTimeFrom.show(getActivity().getSupportFragmentManager(), "timePicker");
                dialogFragmentTimeFrom.setListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timepickerFrom.setText(Integer.toString(hourOfDay) + " " + Integer.toString(minute));
                        hourFrom = hourOfDay;
                        minuteFrom = minute;
                    }
                });
                break;
            case R.id.datepicker_to:
                DatePickerFragment dialogFragmentDateTo = new DatePickerFragment();
                dialogFragmentDateTo.show(getActivity().getSupportFragmentManager(), "datePicker");
                dialogFragmentDateTo.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datepickerTo.setText(Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(dayOfMonth));
                        yearTo = year;
                        monthTo = month;
                        dayTo = dayOfMonth;
                    }
                });
                break;
            case R.id.timepicker_to:
                TimePickerFragment dialogFragmentTimeTo = new TimePickerFragment();
                dialogFragmentTimeTo.show(getActivity().getSupportFragmentManager(), "timePicker");
                dialogFragmentTimeTo.setListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timepickerTo.setText(Integer.toString(hourOfDay) + " " + Integer.toString(minute));
                        hourTo = hourOfDay;
                        minuteTo = minute;
                    }
                });
                break;
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private DatePickerDialog.OnDateSetListener listener;

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            listener.onDateSet(view, year, month, day);
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private TimePickerDialog.OnTimeSetListener listener;

        public void setListener(TimePickerDialog.OnTimeSetListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            listener.onTimeSet(view, hourOfDay, minute);
        }
    }

}

