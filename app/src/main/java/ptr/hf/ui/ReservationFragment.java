package ptr.hf.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ptr.hf.R;
import ptr.hf.network.ApiHelper;
import ptr.hf.network.ErrorResponse;
import ptr.hf.network.IApiFinishedListener;

public class ReservationFragment extends Fragment {

    @BindView(R.id.edittext_chargerid)
    EditText edittextChargerid;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_reservation, container, false);

        View view = inflater.inflate(R.layout.fragment_reservation, container, false);
        unbinder = ButterKnife.bind(this, view);
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
        ApiHelper
                .INSTANCE
                .postReservation(
                        edittextChargerid.getText().toString(),
                        user.getEmail(),
                        1507639115,
                        1507639600,
                        new IApiFinishedListener() {
                            @Override
                            public void success() {

                            }

                            @Override
                            public void error(ErrorResponse errorResponse) {

                            }

                            @Override
                            public void fail() {

                            }
                        });
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
                        datepickerFrom.setText(Integer.toString(year)+ "-" + Integer.toString(month)+"-"+Integer.toString(dayOfMonth));
                    }
                });
                break;
            case R.id.timepicker_from:
                TimePickerFragment dialogFragmentTimeFrom = new TimePickerFragment();
                dialogFragmentTimeFrom.show(getActivity().getSupportFragmentManager(), "timePicker");
                dialogFragmentTimeFrom.setListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timepickerFrom.setText(Integer.toString(hourOfDay)+" "+Integer.toString(minute));
                    }
                });
                break;
            case R.id.datepicker_to:
                DatePickerFragment dialogFragmentDateTo = new DatePickerFragment();
                dialogFragmentDateTo.show(getActivity().getSupportFragmentManager(), "datePicker");
                dialogFragmentDateTo.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datepickerTo.setText(Integer.toString(year)+ "-" + Integer.toString(month)+"-"+Integer.toString(dayOfMonth));
                    }
                });
                break;
            case R.id.timepicker_to:
                TimePickerFragment dialogFragmentTimeTo = new TimePickerFragment();
                dialogFragmentTimeTo.show(getActivity().getSupportFragmentManager(), "timePicker");
                dialogFragmentTimeTo.setListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timepickerTo.setText(Integer.toString(hourOfDay)+" "+Integer.toString(minute));
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
            listener.onDateSet(view,year,month,day);
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

