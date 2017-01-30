package primecode.registerplus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by nagendralimbu on 29/01/2017.
 */

public class MyTokensFragment extends Fragment {

    public MyTokensFragment() {
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
        return inflater.inflate(R.layout.mytokens_fragment, container, false);
    }
}
