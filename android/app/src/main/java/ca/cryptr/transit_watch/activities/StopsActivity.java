package ca.cryptr.transit_watch.activities;

import android.accounts.Account;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ca.cryptr.transit_watch.R;

public class StopsActivity extends Activity {

    private static final String TAG = StopsActivity.class.getSimpleName();

    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stops, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                return true;
            case R.id.about:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
