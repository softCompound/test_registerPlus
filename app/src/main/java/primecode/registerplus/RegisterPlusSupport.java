package primecode.registerplus;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by nagendralimbu on 03/02/2017.
 */

public final class RegisterPlusSupport {
    public RegisterPlusSupport() {
        super();
    }

    static String[] filtrStringToClass(String string){

        StringBuilder sb = new StringBuilder(string);
        sb.deleteCharAt(sb.indexOf("{"));
        sb.deleteCharAt(sb.indexOf("}"));

        String[] values = new String[6];

        int count = 0;
        int valuesCount = 0;
        int lastEqualsIndex = 0;

        while(sb.indexOf("=") > 0) {
            if(count%2 == 0){
                //search for = occurence
                //adding the values takes place in this block
                lastEqualsIndex = sb.lastIndexOf("=");
                values[valuesCount] = sb.substring(lastEqualsIndex + 1);
                sb.delete(lastEqualsIndex, sb.length());
                valuesCount++;
                count++;
            }
            else if(count%2 > 0){
                //search for (comma)

                sb.delete(sb.lastIndexOf(","), lastEqualsIndex);
                count++;
            }
        }

        return values;
    }

    static ArrayList<Token> manipulateFirebaseOutput(HashMap<String, Object> output) {
        Set entrySet = output.entrySet();

        if(entrySet.size() > 0) {
            ArrayList<Token> allTokens = new ArrayList<>();
            Iterator it = entrySet.iterator();
            while(it.hasNext()){
                Map.Entry me = (Map.Entry) it.next();

                String tokenText = me.getValue().toString();
                String[] tokenValues = filtrStringToClass(tokenText);
                Token token = new Token(tokenValues[0], tokenValues[1],tokenValues[4],tokenValues[2],tokenValues[3]);
                System.out.print("4 = " + tokenValues[4] + "3 = " + tokenValues[3] + "2 = " + tokenValues[2] + "1 = " + tokenValues[1]+ "0 = " + tokenValues[0]);
                allTokens.add(token);
            }

            return allTokens;
        }

        return null;
    }
}

