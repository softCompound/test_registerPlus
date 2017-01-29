package primecode.registerplus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by nagendralimbu on 28/01/2017.
 */

public class SplashActivity extends AppCompatActivity{
    /*
    * @Nullable
    * When decorating a method call parameter, this denotes that the parameter can
    * legitimately be null and the method will gracefully deal with it. Typically
    * used on optional parameters.
    */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 800);

    }
}
