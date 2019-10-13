package devfest.hackathon.trashrecognition;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    Animation animimgpage, bttone, bttwo, btthree, ltr;

    TextView titlepage,subtitlepage;
    View bgprogress,bgprogresstop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mappingData();

        addAnimation();



        //load image to main activity
        Picasso.get().load("https://image.freepik.com/free-vector/people-sorting-garbage-recycling_53876-43124.jpg").into(imgProduct);


        txtLoad.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LiveObjectDetectionActivity.class)));
    }


    private void mappingData() {
        txtLoad     = findViewById(R.id.txtLoad);
        imgProduct = findViewById(R.id.imgpage);
        titlepage = findViewById(R.id.titlepage);
        bgprogresstop = findViewById(R.id.bgprogresstop);
        bgprogress = findViewById(R.id.bgprogress);
        subtitlepage = findViewById(R.id.subtitlepage);
    }

    private void addAnimation() {
        // load animation
        animimgpage = AnimationUtils.loadAnimation(this, R.anim.animimgpage);
        bttone = AnimationUtils.loadAnimation(this, R.anim.bttone);
        bttwo = AnimationUtils.loadAnimation(this, R.anim.bttwo);
        btthree = AnimationUtils.loadAnimation(this, R.anim.btthree);
        ltr = AnimationUtils.loadAnimation(this, R.anim.ltr);

        // export animate
        imgProduct.startAnimation(animimgpage);
        titlepage.startAnimation(bttone);
        subtitlepage.startAnimation(bttone);

        txtLoad.startAnimation(btthree);
        bgprogress.startAnimation(bttwo);
        bgprogresstop.startAnimation(ltr);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!Utils.allPermissionsGranted(this)) {
            Utils.requestRuntimePermissions(this);
        }
    }
}
