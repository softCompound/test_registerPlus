package primecode.registerplus;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nagendralimbu on 30/01/2017.
 */

public interface FragmentButtonClick {
    void fragmentButtonClicked();
    boolean validateFirebaseDbInput(String string);
    ArrayList<Token> manipulateFirebaseOutput(HashMap<String, Object> output);
}
