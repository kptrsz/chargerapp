package ptr.hf.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ptr.hf.R;

public class SettingsFragment extends Fragment {

    @BindView(R.id.spinner_connectors)
    Spinner spinnerConnectors;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.settings_in_pager, container, false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(
                        getActivity(),
                        R.array.list_connectors,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConnectors.setAdapter(adapter);


        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}