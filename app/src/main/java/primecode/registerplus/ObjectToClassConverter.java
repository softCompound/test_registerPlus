package primecode.registerplus;

/**
 * Created by nagendralimbu on 01/02/2017.
 */

final class ObjectToClassConverter {

    private ObjectToClassConverter() {
        super();
    }

    public static String[] filtrStringToClass(String string){
        StringBuilder sb = new StringBuilder(string);
        sb.deleteCharAt(sb.indexOf("{"));
        sb.deleteCharAt(sb.indexOf("}"));

        int count = 0;
        String[] values = new String[5];
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

}
