package primecode.registerplus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nagendralimbu on 02/02/2017.
 */

public class TokenAdapter extends BaseAdapter {
//    private FragmentButtonClick fragmentButtonClick;

    private  ArrayList<Token> allTokens;
    private LayoutInflater mLayoutInflater;
    private Context ctx;

     TokenAdapter(Context context, ArrayList<Token> allTokens) {
        this.ctx = context;
        this.allTokens = allTokens;
        mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//         try {
//             fragmentButtonClick = (FragmentButtonClick) context;
//         } catch (ClassCastException e) {
//             throw new ClassCastException(context.toString()
//                     + " must implement FragmentButtonClick Interface");
//         }
     }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return allTokens.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if(convertView == null) {
            v = mLayoutInflater.inflate(R.layout.token_row, parent, false);
        }else{
            v = convertView;
        }

        TextView nhs = (TextView) v.findViewById(R.id.nhs);
        TextView fullName = (TextView) v.findViewById(R.id.fullName);
        TextView address = (TextView) v.findViewById(R.id.address);
        TextView timestamp = (TextView) v.findViewById(R.id.timestamp);
        TextView inquiry = (TextView) v.findViewById(R.id.inquiry);

        nhs.setText(allTokens.get(position).getNhsNumber());
        fullName.setText(allTokens.get(position).getFullname());
        address.setText(allTokens.get(position).getAddress());
        timestamp.setText(allTokens.get(position).getDate());
        inquiry.setText(allTokens.get(position).getInquiryNature());

        return v;
    }
}
