package primecode.registerplus;

/**
 * Created by nagendralimbu on 30/01/2017.
 */

public class Token {
    private String fullname, address, inquiryNature, nhsNumber, date;

    public Token(String fullname, String address, String inquiryNature, String nhsNumber, String date) {
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