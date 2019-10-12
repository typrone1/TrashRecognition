package devfest.hackathon.trashrecognition;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import devfest.hackathon.trashrecognition.common.Label;
import devfest.hackathon.trashrecognition.common.Product;

public class MainActivity extends AppCompatActivity {
    TextView txtLoad;
    ImageView imgProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLoad = findViewById(R.id.txtLoad);
        imgProduct = findViewById(R.id.imgpage);

        //load image to main activity
        Picasso.get().load("https://image.freepik.com/free-vector/people-sorting-garbage-recycling_53876-43124.jpg").into(imgProduct);


        txtLoad.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LiveObjectDetectionActivity.class)));

    }




    @Override
    protected void onResume() {
        super.onResume();
        if (!Utils.allPermissionsGranted(this)) {
            Utils.requestRuntimePermissions(this);
        }
    }
}
