package demo.af;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import parser.JsonParser;

public class MainActivity extends AppCompatActivity {

    TableRow tableAf;
    TableRow tableHco;
    Bitmap imageAf=null;
    Bitmap imageHco=null;
    String target;
    String buttonTitle;
    String description;
    String footer;
    String title;
    String descriptionHco;
    String targetHco ;
    String buttontitleHco;
    String titleHco;
    String imageHcoUrl;
    String imageAfUrl;
    ProgressDialog dialog;
    FrameLayout frame;
    SharedPreferences preferences;
    int height;
    int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startactivity);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);


        tableAf=(TableRow)findViewById(R.id.tableaf);
        tableHco=(TableRow)findViewById(R.id.tableho);
        frame=(FrameLayout)findViewById(R.id.frame);

        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(this);
        String previouslyEncodedImage = shre.getString("image_data", "");
        String previouslyEncodedImage2 = shre.getString("image_dataHco", "");
if (!isOnline()){
    Toast.makeText(getApplicationContext(),"You are not connected to the internet.Please Check your internet connection and try again later",Toast.LENGTH_LONG).show();
}else {
    if (!previouslyEncodedImage.equalsIgnoreCase("")) {


        final ImageView afImage = (ImageView) findViewById(R.id.afImage);
        TextView tvAf = (TextView) findViewById(R.id.textViewAf);


        String imageAfPath = shre.getString("afPath", "");

        imageAf = loadImageFromStorage(imageAfPath, "afImage");
        imageHco = loadImageFromStorage(imageAfPath, "hcoImage");
        title = shre.getString("title", "");
        titleHco = shre.getString("titleHco", "");
        description = shre.getString("description", "");
        descriptionHco = shre.getString("descriptionHco", "");
        buttonTitle = shre.getString("buttonTitle", "");
        buttontitleHco = shre.getString("buttontitleHco", "");
        target = shre.getString("target", "");
        targetHco = shre.getString("targetHco", "");

        imageAfUrl = shre.getString("imageAfUrl", "");
        imageHcoUrl = shre.getString("imageHcoUrl", "");


        tvAf.setText(title);
        TextView tvHco = (TextView) findViewById(R.id.hcoText);
        tvHco.setText(titleHco);


        width = shre.getInt("width", 0);
        afImage.setMinimumWidth(width);


        height = shre.getInt("height", 0);
        afImage.setImageBitmap(Bitmap.createScaledBitmap(imageAf, height, width, false));
        ImageView hcoImage = (ImageView) findViewById(R.id.hcoImage);
        hcoImage.setImageBitmap(Bitmap.createScaledBitmap(imageHco, height, width, false));


        frame.setVisibility(View.INVISIBLE);


    } else {
        new AsyncTaskParseJson().execute();
    }
}


        tableAf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), shopActivity.class);

                intent.putExtra("image", imageAfUrl);
                intent.putExtra("target",target);
                intent.putExtra("description",description);
                intent.putExtra("buttonTitle", buttonTitle);
                startActivity(intent);

            }
        });

        tableHco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), shopActivity.class);
              //  ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                //imageHco.compress(Bitmap.CompressFormat.PNG, 50, _bs);
                intent.putExtra("image", imageHcoUrl);
                intent.putExtra("target",targetHco);
                intent.putExtra("description",descriptionHco);
                intent.putExtra("buttonTitle",buttontitleHco);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        // the height will be set at this point
        int height = tableAf.getMeasuredHeight();
    }

     public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";

        // set your json string url here

         String afUrl ="http://www.abercrombie.com/anf/nativeapp/Feeds/promotions.json";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        @Override
        protected void onPreExecute() {


            dialog = new ProgressDialog(MainActivity.this);

            dialog.setTitle("Processing");
            dialog.setMessage("Loading Data");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {

            try {

                // instantiate our json parser
                JsonParser jParser = new JsonParser();

                // get json string from url
                JSONObject json = jParser.getJSONFromUrl(afUrl);


                dataJsonArr = json.getJSONArray("promotions");


                for (int i = 0; i < dataJsonArr.length(); i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);
                    if (i==0) {
                        // Storing each json item in variable
                        JSONObject button = c.getJSONObject("button");


                        target = button.getString("target");
                        buttonTitle = button.getString("title");
                        description = c.getString("description");
                        footer = c.getString("footer");
                        title = c.getString("title");
                        imageAfUrl = c.getString("image");
                        InputStream in = null;
                        try {
                            in = new java.net.URL(imageAfUrl).openStream();
                            imageAf = BitmapFactory.decodeStream(in);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else{

                        JSONArray buttonHco = c.getJSONArray("button");
                        JSONObject target= (JSONObject) buttonHco.get(0);


                        descriptionHco = c.getString("description");
                        targetHco = target.getString("target");
                        buttontitleHco = target.getString("title");
                        titleHco = c.getString("title");
                        imageHcoUrl = c.getString("image");
                        InputStream in = null;
                        try {
                           in = new java.net.URL(imageHcoUrl).openStream();
                            imageHco = BitmapFactory.decodeStream(in);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {

            ImageView afImage=(ImageView)findViewById(R.id.afImage);
            TextView tvAf=(TextView)findViewById(R.id.textViewAf);

            tvAf.setText(title);
            TextView tvHco=(TextView)findViewById(R.id.hcoText);
            tvHco.setText(titleHco);
            afImage.setMinimumWidth(tableAf.getWidth());
            width=tableAf.getWidth();
            height=tableAf.getHeight();
            afImage.setImageBitmap(Bitmap.createScaledBitmap(imageAf, height, width, false));
            ImageView hcoImage=(ImageView)findViewById(R.id.hcoImage);
            hcoImage.setImageBitmap(Bitmap.createScaledBitmap(imageHco, height, width, false));
            dialog.dismiss();
            frame.setVisibility(View.INVISIBLE);




            String afImagePath=saveToInternalSorage(imageAf, "afImage");
            String hcoImagePath=saveToInternalSorage(imageHco, "hcoImage");



            //textEncode.setText(encodedImage);
            int width1= imageHco.getWidth();
            int width2=hcoImage.getWidth();
            SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor edit=shre.edit();
           // edit.putString("image_data", encodedImage);

            edit.putString("target",target);
            edit.putString("buttonTitle",buttonTitle);
            edit.putString("description",description);
            edit.putString("footer",footer);
            edit.putString("title",title);
            edit.putString("descriptionHco",descriptionHco);
            edit.putString("targetHco", targetHco);
            edit.putString("buttontitleHco",buttontitleHco);
            edit.putString("titleHco",titleHco);
           // edit.putString("image_dataHco",encodedImage1);
            edit.putInt("width", width);
            edit.putInt("height",height);
            edit.putString("afPath", afImagePath);
            edit.putString("hCoPath", hcoImagePath);
            edit.putString("imageAfUrl",imageAfUrl);
            edit.putString("imageHcoUrl",imageHcoUrl);

            edit.commit();














        }
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private String saveToInternalSorage(Bitmap bitmapImage,String imageName)  {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,imageName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private Bitmap loadImageFromStorage(String path,String name)
    {

        try {
            File f=new File(path, name);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
           return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
