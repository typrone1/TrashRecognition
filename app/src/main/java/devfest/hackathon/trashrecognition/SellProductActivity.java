package devfest.hackathon.trashrecognition;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import devfest.hackathon.trashrecognition.common.Product;

public class SellProductActivity extends AppCompatActivity {
    Button btnRemove;
    Button btnAdd;
    TextView txtScore;
    int value = 0;

    TextView txtLoad, txtAmount;

    EditText edtAddress, edtPhone;
    TextView btnexercise;
    EditText edtName;
    String nameUser;
    String addressUser;
    String phoneUser;
    String amount;

    DatabaseReference databaseReference;

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addControls();
        btnAdd = findViewById(R.id.btnAdd);
        btnRemove = findViewById(R.id.btnremove);
        txtScore = findViewById(R.id.txtAmount);

        btnAdd.setOnClickListener(v -> {
            value += 1;
            txtScore.setText(value + "");
        });

        btnRemove.setOnClickListener(v -> {
            value -= 1;
            if (value < 1) {
                txtScore.setText("0");
            } else {
                txtScore.setText(value + "");
            }
        });
        addEvent();

    }

    private void addControls() {

        edtName = findViewById(R.id.txtUsername);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhone = findViewById(R.id.edtPhone);
        btnexercise = findViewById(R.id.btnexercise);
        txtAmount = findViewById(R.id.txtAmount);


    }

    private void addEvent() {
        btnexercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                databaseReference = FirebaseDatabase.getInstance().getReference("Product");
//
//
//                nameUser = edtName.getText().toString();
//                addressUser = edtAddress.getText().toString();
//                phoneUser = edtPhone.getText().toString();
//                amount = txtAmount.getText().toString();
//
//                Product product = new Product(amount, nameUser, addressUser, phoneUser);
//
//                databaseReference.child("Information product").push().setValue(product);
//                Toast.makeText(SellProductActivity.this, "Upload database succesfull!", Toast.LENGTH_SHORT).show();

//                webView=findViewById(R.id.wbDisplay);
//                webView.setWebViewClient(new WebViewClient());
//                webView.loadUrl("https://green-planet-team.firebaseapp.com/thu-mua-rac");

//                Intent intent = new Intent(Intent.ACTION_VIEW);
////                intent.setData(Uri.parse("https://green-planet-team.firebaseapp.com/thu-mua-rac"));
////                startActivity(intent);

//                String url = "https://green-planet-team.firebaseapp.com/thu-mua-rac";
//                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//                CustomTabsIntent customTabsIntent = builder.build();
//                customTabsIntent.launchUrl(SellProductActivity.this, Uri.parse(url));

                startActivity(new Intent(SellProductActivity.this,ProductActivity.class));
            }
        });
    }
}
