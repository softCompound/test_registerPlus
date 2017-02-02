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

public class NhsQueryFragment extends Fragment {
    private ListView mListView;
    private FragmentButtonClick fragmentButtonClick;
    public NhsQueryFragment() {
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
        ArrayList<Token> t = fragmentButtonClick.getNhsQueryArray();
        View view;

        if(t.size() > 0){
            view = inflater.inflate(R.layout.token_display, container, false);

            mListView = (ListView) view.findViewById(R.id.list);
            mListView.setAdapter(new TokenAdapter(getContext(), t));
            return view;
        }
        else{
            view = inflater.inflate(R.layout.nhs_query_unsuccessful, container, false);
            return view;
        }

    }
}
