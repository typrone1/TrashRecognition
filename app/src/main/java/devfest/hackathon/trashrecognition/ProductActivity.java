package devfest.hackathon.trashrecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ProductActivity extends AppCompatActivity {

    WebView wbProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        wbProduct=findViewById(R.id.wbProduct);

        wbProduct.setWebViewClient(new WebViewClient());
        wbProduct.getSettings().setJavaScriptEnabled(true); // Enable JavaScript
        wbProduct.loadUrl("https://green-planet-team.firebaseapp.com/thu-mua-rac");

    }
}
