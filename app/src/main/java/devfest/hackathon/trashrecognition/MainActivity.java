package devfest.hackathon.trashrecognition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LiveObjectDetectionActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Utils.allPermissionsGranted(this)) {
            Utils.requestRuntimePermissions(this);
        }
    }
}
