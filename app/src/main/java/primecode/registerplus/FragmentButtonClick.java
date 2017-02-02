package primecode.registerplus;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nagendralimbu on 30/01/2017.
 */

public interface FragmentButtonClick {
    void createMyTokenFragment();
    ArrayList<Token> getAllTokens();
    void fragmentButtonClicked(String fullName, String address, String selectedSpinner, String nhsNumber);
    boolean validateFirebaseDbInput(String string);
    void makeToast(String s);
    HashMap<String, Object> queryNhsNumber(String nhsNumber);
    ArrayList<Token> manipulateFirebaseOutput(HashMap<String, Object> output);
}
