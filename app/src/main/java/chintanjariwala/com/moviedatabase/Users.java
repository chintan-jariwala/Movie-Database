package chintanjariwala.com.moviedatabase;

import com.orm.SugarRecord;

/**
 * Created by chint on 11/5/2016.
 */

public class Users extends SugarRecord{

    public String name = null;
    public String email = null;
    public String passwrold = null;

    public Users() {
    }

    public Users(String name, String passwrold, String email) {
        this.name = name;
        this.passwrold = passwrold;
        this.email = email;
    }
}
