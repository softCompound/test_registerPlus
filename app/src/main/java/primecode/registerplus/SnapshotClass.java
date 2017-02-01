package primecode.registerplus;

import java.util.ArrayList;

/**
 * Created by nagendralimbu on 01/02/2017.
 */

public class SnapshotClass {

    private String timeStamp;
    private ArrayList<Token> tokens;

    public SnapshotClass(){}

    public String getTimeStamp(){return timeStamp;}

    public ArrayList<Token> getTokens(){return tokens;}
}
