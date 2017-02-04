package primecode.registerplus;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nagendralimbu on 30/01/2017.
 */

public class Token {

    private String fullname, address, inquiryNature, nhsNumber, date;

    public Token(String nhsNumber, String fullname, String address, String inquiryNature, String date) {
        this.fullname = fullname;
        this.address = address;
        this.inquiryNature = inquiryNature;
        this.nhsNumber = nhsNumber;
        this.date = date;
    }

    public String getFullname() {return this.fullname;}

    public String getAddress() {return this.address;}

    public String getInquiryNature() {return this.inquiryNature;}

    public String getNhsNumber() {return this.nhsNumber;}

    public String getDate() {return this.date;}


}
