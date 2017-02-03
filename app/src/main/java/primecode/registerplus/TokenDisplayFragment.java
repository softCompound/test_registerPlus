package primecode.registerplus;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by nagendralimbu on 02/02/2017.
 */

public class TokenDisplayFragment extends Fragment {
    private FragmentButtonClick fragmentButtonClick;
    private ListView mListView;

    public TokenDisplayFragment() {
        super();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.token_display, container, false);

        mListView = (ListView) view.findViewById(R.id.list);
        ArrayList<Token> tokens = fragmentButtonClick.getAllTokens();
        mListView.setAdapter(new TokenAdapter(getContext(), tokens));
        return view;
    }
}
