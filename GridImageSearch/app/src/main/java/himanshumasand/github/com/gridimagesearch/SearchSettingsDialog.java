package himanshumasand.github.com.gridimagesearch;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * Created by Himanshu on 9/23/2015.
 */
public class SearchSettingsDialog extends DialogFragment implements View.OnClickListener{

    private Spinner mSelectSize;
    private Spinner mSelectColor;
    private Spinner mSelectType;
    private EditText mSelectSite;

    public SearchSettingsDialog() {}

    public interface SearchSettingsDialogListener {
        void onSettingsChange(SearchSettings newSettings);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    public static SearchSettingsDialog newInstance(SearchSettings settings) {
        SearchSettingsDialog frag = new SearchSettingsDialog();
        Bundle args = new Bundle();
        //TODO: Change this to serializable
        args.putInt("size", settings.size);
        args.putInt("color", settings.color);
        args.putInt("type", settings.type);
        args.putString("site", settings.site);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container);
        mSelectSize = (Spinner) view.findViewById(R.id.spnSize);
        mSelectColor = (Spinner) view.findViewById(R.id.spnColor);
        mSelectType = (Spinner) view.findViewById(R.id.spnType);
        mSelectSite = (EditText) view.findViewById(R.id.etSite);

        getDialog().setTitle("Settings");
        mSelectSize.setSelection(getArguments().getInt("size"));
        mSelectColor.setSelection(getArguments().getInt("color"));
        mSelectType.setSelection(getArguments().getInt("type"));
        mSelectSite.setText(getArguments().getString("site"));

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Button saveButton = (Button) view.findViewById(R.id.btnSave);
        saveButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSave:
                SearchSettingsDialogListener listener = (SearchSettingsDialogListener) getActivity();
                SearchSettings newSettings = new SearchSettings(mSelectSize.getSelectedItemPosition(), mSelectColor.getSelectedItemPosition(), mSelectType.getSelectedItemPosition(), mSelectSite.getText().toString());
                listener.onSettingsChange(newSettings);
                dismiss();
                break;
        }
    }
}
