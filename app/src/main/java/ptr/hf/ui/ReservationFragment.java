package ptr.hf.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ptr.hf.R;
import ptr.hf.model.Reservation;
import ptr.hf.model.ReservationResponse;
import ptr.hf.network.ApiHelper;
import ptr.hf.network.ErrorResponse;
import ptr.hf.network.IApiResultListener;

public class ReservationFragment extends Fragment {
    @BindView(R.id.edittext_chargerid)
    EditText edittextChargerid;
    @BindView(R.id.datepicker_from)
    DatePicker datepickerFrom;
    @BindView(R.id.datepicker_to)
    DatePicker datepickerTo;
    Unbinder unbinder;
    @BindView(R.id.send_reservation)
    Button sendReservation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.send_reservation)
    public void onViewClicked() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ApiHelper
                .INSTANCE
                .postReservation(edittextChargerid.getText().toString(),
                        user.getEmail(), datepickerFrom.getDayOfMonth(),
                        datepickerTo.getDayOfMonth(),
                        new IApiResultListener<ReservationResponse>() {
            @Override
            public void success(ReservationResponse result) {
                Snackbar
                        .make(findViewById(android.R.id.content),
                                "A foglalási kód: "+result.getData().getReservationId(),
                                Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            public void error(ErrorResponse errorResponse) {

            }

            @Override
            public void fail() {

            }
        });
    }
}
