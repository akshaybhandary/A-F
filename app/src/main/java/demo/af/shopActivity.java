package demo.af;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

/**
 * Created by Akshay on 11/25/2015.
 */
public class shopActivity extends Activity {

    String title;
    Bitmap afImage;
    String description;
    String target;
    String Image;
    ImageView actImage;
    Button btnTitle;
    TextView aftextView;
    FrameLayout frame;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoplayout);
        aftextView=(TextView)findViewById(R.id.afText);
        btnTitle=(Button)findViewById(R.id.afButton);
        //frame=(FrameLayout)findViewById(R.id.frame1);

        Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("Image");
        title=getIntent().getExtras().getString("buttonTitle");
        description=getIntent().getExtras().getString("description");
        target=getIntent().getExtras().getString("target");
        Image=getIntent().getExtras().getString("image");
        aftextView.setText(description);
        btnTitle.setText(title);
        new DownloadImageTask((ImageView) findViewById(R.id.buttonImage))
                .execute(Image);


        btnTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),webViewActivity.class);
                intent.putExtra("target",target);
                startActivity(intent);


            }
        });

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {


            dialog = new ProgressDialog(shopActivity.this);

            dialog.setTitle("Processing");
            dialog.setMessage("Loading Data");
            dialog.show();
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {


            try {
                InputStream in = new java.net.URL(Image).openStream();
                afImage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return afImage;
        }

        protected void onPostExecute(Bitmap result) {
            actImage=(ImageView)findViewById(R.id.buttonImage);
            bmImage.setImageBitmap(result);
            dialog.dismiss();
        }
    }


}
