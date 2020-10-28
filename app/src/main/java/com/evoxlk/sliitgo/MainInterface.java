package com.evoxlk.sliitgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class MainInterface extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    ArrayList<String> arrayListTitle = new ArrayList<String>();
    ArrayList<String> arrayListUrl = new ArrayList<String>();

    WebView webView;




    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface);
        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        //http allow
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);

        //load user's bookmarks
        loadBookMarks();



        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 8_3 like Mac OS X) AppleWebKit/600.14 (KHTML, like Gecko) Mobile/12F70");




        //Runtime External storage permission for saving download files
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);
            }
        }

        //handle downloading
        webView.setDownloadListener(new DownloadListener()
        {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimeType,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));
                request.setMimeType(mimeType);
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading File...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                                url, contentDisposition, mimeType));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
            }});




        Intent intent = getIntent();

        if (intent.getExtras() != null){
            String url = intent.getStringExtra("url");
            webView.loadUrl(url);
        }else{
            webView.loadUrl("https://courseweb.sliit.lk/");
        }


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {



        if (menuItem.getItemId()== R.id.courseweb) {
            loadsWebsite("https://courseweb.sliit.lk/");
            menuItem.setChecked(true);
        }

        if (menuItem.getItemId()== R.id.sliitlksite) {
            loadsWebsite("https://www.sliit.lk/");
            menuItem.setChecked(true);
        }

        if (menuItem.getItemId()== R.id.onlineExams) {
            loadsWebsite("https://onlineexams.sliit.lk/login/index.php");
            menuItem.setChecked(true);
        }

        if (menuItem.getItemId()== R.id.eduscope) {
            loadsWebsite("https://lecturecapture.sliit.lk/login.php");
            menuItem.setChecked(true);
        }

        if (menuItem.getItemId()== R.id.studentPortal) {
            loadsWebsite("http://study.sliit.lk/");
            menuItem.setChecked(true);
        }

        if (menuItem.getItemId()== R.id.aboutApp) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setMessage("This application is designed and developed for the easy of use for SLIIT students");
            alertDialog.show();
        }

        if (menuItem.getItemId()== R.id.developerNotice) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setMessage("App is designed and developed by Harsha Suraweera\nEmail : hi@harshasuraweera.me\nharshamanoj912@gmail.com" +
                    "\nWebsite: harshasuraweera.me");
            alertDialog.show();
        }

        if (menuItem.getItemId()== R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

        return false;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.showBookmarks){
            ShowDialog bookMarkDialog =  new ShowDialog();
            bookMarkDialog.showDialog(MainInterface.this, arrayListTitle, arrayListUrl);
        }

        if (item.getItemId() == R.id.addNewBookmark){
            addNewBookmark();
        }


        if (item.getItemId() == R.id.markThisPageAsBookmark){
            markThisPageAsBookmark(webView);
        }


        return(super.onOptionsItemSelected(item));
    }



    public void loadsWebsite(String url){

        Intent intent = new Intent(this, MainInterface.class);
        intent.putExtra("url", url);

        startActivity(intent);
        overridePendingTransition(-1000, -1000);

    }

    public void markThisPageAsBookmark(WebView webView){
        String webUrl = webView.getUrl();
        MarkThisAsBookmark markThisAsBookmark =  new MarkThisAsBookmark(webUrl);
        markThisAsBookmark.show(getSupportFragmentManager(), "add bookmark dialog");
    }



    //load user's all bookmarks to the arraylist

    public void loadBookMarks(){

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("bookmarks").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

        mDatabaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot dSnapshot : snapshot.getChildren()) {

                        arrayListTitle.add(Objects.requireNonNull(dSnapshot.child("bookmarkTitle").getValue()).toString());
                        arrayListUrl.add(Objects.requireNonNull(dSnapshot.child("bookMarkUrl").getValue()).toString());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //end of the load user's all bookmarks to the arraylist

    public void addNewBookmark(){

        OpenDialog openDialog = new OpenDialog();
        openDialog.show(getSupportFragmentManager(), "add bookmark dialog");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }



    //add new bookmark

    public static class OpenDialog extends AppCompatDialogFragment{

        EditText title, url;
        String bMarkTitle ;
        String bMarkUrl;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final AlertDialog.Builder builder =  new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view =  inflater.inflate(R.layout.add_new_bookmark, null);

            builder.setView(view);

            title = view.findViewById(R.id.newBookmarkTitle);
            url = view.findViewById(R.id.newBookmarkUrl);


            builder.setTitle("Add new bookmark").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).setPositiveButton("Set", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    bMarkTitle = title.getText().toString();
                    bMarkUrl = url.getText().toString();

                    TempBookMark tempBookMark = new TempBookMark(bMarkUrl, bMarkTitle);

                    DatabaseReference databaseRider;
                    UUID uuid = UUID.randomUUID();
                    databaseRider = FirebaseDatabase.getInstance().getReference("bookmarks").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                    databaseRider.child(String.valueOf(uuid)).setValue(tempBookMark);
                    Toast.makeText(view.getContext(), "Successfully added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(view.getContext(), MainInterface.class));

                }
            });

            return  builder.create();


        }


        public static void addBmark(String bMarkUrl, String bMarkTitle){

            DatabaseReference databaseRider;
            databaseRider = FirebaseDatabase.getInstance().getReference("bookmarks").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

            UUID uuid = UUID.randomUUID();

            databaseRider.child(String.valueOf(uuid)).child("bookMarkUrl").setValue(bMarkUrl);
            databaseRider.child(String.valueOf(uuid)).child("bookmarkTitle").setValue(bMarkTitle);


        }

    }


    //end of the new bookmark adding



    //make this page s bookmark

    public static class MarkThisAsBookmark extends AppCompatDialogFragment{

        EditText title, url;
        String thisPageMarkTitle ;
        String thisPageMarkUrl;
        String currentUrl;

        public MarkThisAsBookmark(String currentUrl) {
            this.currentUrl = currentUrl;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final AlertDialog.Builder builder =  new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view =  inflater.inflate(R.layout.mark_this_page_as_bookmark, null);

            builder.setView(view);


            title = view.findViewById(R.id.thisPageBookmarkTitle);
            url = view.findViewById(R.id.thisPageBookmarkUrl);
            url.setText(currentUrl);


            builder.setTitle("Give a title").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).setPositiveButton("Set", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    thisPageMarkTitle = title.getText().toString();
                    thisPageMarkUrl = url.getText().toString();

                    TempBookMark tempBookMark = new TempBookMark(thisPageMarkUrl, thisPageMarkTitle);

                    DatabaseReference databaseRider;
                    UUID uuid = UUID.randomUUID();
                    databaseRider = FirebaseDatabase.getInstance().getReference("bookmarks").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                    databaseRider.child(String.valueOf(uuid)).setValue(tempBookMark);
                    Toast.makeText(view.getContext(), "Successfully added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(view.getContext(), MainInterface.class));

                }
            });

            return  builder.create();

        }

        public static void addBmark(String bMarkUrl, String bMarkTitle){

            DatabaseReference databaseRider;
            databaseRider = FirebaseDatabase.getInstance().getReference("bookmarks").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

            UUID uuid = UUID.randomUUID();

            databaseRider.child(String.valueOf(uuid)).child("bookMarkUrl").setValue(bMarkUrl);
            databaseRider.child(String.valueOf(uuid)).child("bookmarkTitle").setValue(bMarkTitle);


        }

    }


    // end of the bookmark this page



    //set on back pressed

    @Override
    public void onBackPressed(){

        WebView webView = (WebView) findViewById(R.id.webView); // Remove this
        if(webView.canGoBack()){
            webView.goBack();
        }else{
            super.onBackPressed();
        }
    }


    //end of the set on back pressed




}










