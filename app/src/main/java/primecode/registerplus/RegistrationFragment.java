package primecode.registerplus;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by nagendralimbu on 29/01/2017.
 */

public class RegistrationFragment extends Fragment {
    private FragmentButtonClick fragmentButtonClick;
    private View view;
    private String fullName, address, nhsNumber, selectedSpinner;
    public RegistrationFragment() {
        super();
    }

    /**
     * This method is called sometime after the completion of oncreate method.
     * onCreate is used to handle non-graphical initialisation and is called after onAttach is completed.
     * Any graphical initialisation needs to be done here.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.registration_form_fragment, container, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.reg_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.inqury_nature_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Button button = (Button) view.findViewById(R.id.button_get_token);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()) {
                    setProgressBar();
                    fragmentButtonClick.fragmentButtonClicked(fullName, address, selectedSpinner, nhsNumber);
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            fragmentButtonClick = (FragmentButtonClick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentButtonClick Interface");
        }
    }

    public void setProgressBar(){
        ProgressBar pbar = (ProgressBar) view.findViewById(R.id.progressBar2);
        pbar.setVisibility(View.VISIBLE);
        pbar.setProgress(0);
        pbar.incrementProgressBy(100);

        TextView tfullName = (TextView) view.findViewById(R.id.reg_name);
        TextView tnhs = (TextView) view.findViewById(R.id.reg_nhsNumber);
        TextView taddress = (TextView) view.findViewById(R.id.reg_address);
        TextView tspinner = (TextView) view.findViewById(R.id.text_spinner);

        Button button = (Button) view.findViewById(R.id.button_get_token);

        tfullName.setVisibility(View.GONE);
        tnhs.setVisibility(View.GONE);
        taddress.setVisibility(View.GONE);
        tspinner.setVisibility(View.GONE);
        button.setVisibility(View.GONE);

    }

    private boolean validateForm(){
        EditText editFullName = (EditText) view.findViewById(R.id.editTextName);
        fullName = editFullName.getText().toString().trim();

        EditText editAddress = (EditText) view.findViewById(R.id.editTextAddress);
        address = editAddress.getText().toString().trim();

        Spinner spinner = (Spinner) view.findViewById(R.id.reg_spinner);
        selectedSpinner = spinner.getSelectedItem().toString();

        EditText editNhs = (EditText) view.findViewById(R.id.editNhsNum);
        nhsNumber = editNhs.getText().toString().trim();

        if (selectedSpinner.equals("Select from the list") ||
                (!fragmentButtonClick.validateFirebaseDbInput(fullName)) ||
                (address.length() < 1) ||
                (!fragmentButtonClick.validateFirebaseDbInput(nhsNumber))) {
            Toast.makeText(getContext(), "Please Complete the Form.", Toast.LENGTH_SHORT).show();
        }
        else {

            editAddress.setText("");
            editFullName.setText("");
            editNhs.setText("");
            spinner.setSelection(0);

            editFullName.setVisibility(View.GONE);
            editAddress.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            editNhs.setVisibility(View.GONE);
            return true;
        }
        return false;
    }
}
