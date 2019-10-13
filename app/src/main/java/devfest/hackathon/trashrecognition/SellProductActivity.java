package devfest.hackathon.trashrecognition;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
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
                databaseReference = FirebaseDatabase.getInstance().getReference("Product");

                nameUser = edtName.getText().toString();
                addressUser = edtAddress.getText().toString();
                phoneUser = edtPhone.getText().toString();
                amount = txtAmount.getText().toString();

                Product product = new Product(amount, nameUser, addressUser, phoneUser);
                databaseReference.setValue(product);
                Toast.makeText(SellProductActivity.this, "Upload database succesfull!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
