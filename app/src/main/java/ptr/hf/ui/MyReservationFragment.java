package ptr.hf.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ptr.hf.R;
import ptr.hf.model.ReservationResponse;
import ptr.hf.network.ApiHelper;
import ptr.hf.network.ErrorResponse;
import ptr.hf.network.IApiResultListener;

public class MyReservationFragment extends Fragment {
    @BindView(R.id.act)
    TextView act;
    @BindView(R.id.past1)
    TextView past1;
    @BindView(R.id.past2)
    TextView past2;
    @BindView(R.id.past3)
    TextView past3;
    @BindView(R.id.past4)
    TextView past4;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_reservations, container, false);

        unbinder = ButterKnife.bind(this, view);
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        ApiHelper.INSTANCE.getReservation(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(), new IApiResultListener<ReservationResponse>() {
            @Override
            public void success(ReservationResponse result) {
//                Calendar calendar = Calendar.getInstance();
//                Date date = calendar.getTime();
                if (result.getData().size() > 4) {
                    DateTime dateTime = new DateTime();
                    DateTime lastReservation = new DateTime(result.getData().get(result.getData().size() - 1).getTo());

                    if (dateTime.isBefore(lastReservation)) {
                        act.setText(new DateTime(result.getData().get(result.getData().size()).getFrom()).toString(DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")) + " - " + new DateTime(result.getData().get(result.getData().size()).getTo()).toString(DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")));
                    }
                    if (result.getData().size() > 4) {
                        past1.setText(new DateTime(result.getData().get(1)).toString(DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")) + " - " + new DateTime(result.getData().get(1)).toString(DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")));
                        past2.setText(new DateTime(result.getData().get(2)).toString(DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")) + " - " + new DateTime(result.getData().get(2)).toString(DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")));
                        past3.setText(new DateTime(result.getData().get(3)).toString(DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")) + " - " + new DateTime(result.getData().get(3)).toString(DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")));
                        past4.setText(new DateTime(result.getData().get(4)).toString(DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")) + " - " + new DateTime(result.getData().get(4)).toString(DateTimeFormat.forPattern("YYYY/MM/dd HH:mm")));
                    }
                }
            }

            @Override
            public void error(ErrorResponse errorResponse) {

            }

            @Override
            public void fail() {

            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
