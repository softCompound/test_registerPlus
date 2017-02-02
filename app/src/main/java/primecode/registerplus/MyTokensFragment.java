package primecode.registerplus;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by nagendralimbu on 29/01/2017.
 */

public class MyTokensFragment extends Fragment {
    private View view;
    private FragmentButtonClick fragmentButtonClick;


    public MyTokensFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            fragmentButtonClick = (FragmentButtonClick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentButtonClick");
        }
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
        view =  inflater.inflate(R.layout.mytokens_fragment, container, false);

        //get the button
        final Button button = (Button) view.findViewById(R.id.button_view_token);
         button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nhsNumber = (EditText) view.findViewById(R.id.edit_nhsQuery);
                if(fragmentButtonClick.validateFirebaseDbInput(nhsNumber.getText().toString().trim())) {
                    TextView textView = (TextView) view.findViewById(R.id.text_nhs_query);
                    textView.setVisibility(View.INVISIBLE);
                    nhsNumber.setVisibility(View.INVISIBLE);
                    button.setVisibility(View.INVISIBLE);

                    ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar_myTokenFragment);
                    progressBar.setVisibility(View.VISIBLE);
                    fragmentButtonClick.queryNhsNumber(nhsNumber.getText().toString().trim());
                    progressBar.setVisibility(View.INVISIBLE);

                }
                else if(nhsNumber.getText().toString().length() < 1) {
                    Toast.makeText(getContext(), "Please Complete the Form.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
