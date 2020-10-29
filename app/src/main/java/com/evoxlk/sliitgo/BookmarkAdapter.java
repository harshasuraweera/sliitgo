package com.evoxlk.sliitgo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<String> arrayListTitle;
    private ArrayList<String> arrayListUrl;


    public BookmarkAdapter(Context ctx, ArrayList<String> arrayListTitle, ArrayList<String> arrayListUrl){

        inflater = LayoutInflater.from(ctx);
        this.arrayListTitle = arrayListTitle;
        this.arrayListUrl = arrayListUrl;

    }

    @Override
    public BookmarkAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.single_bookmark, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final BookmarkAdapter.MyViewHolder holder, final int position) {

        holder.bookMarkTitle.setText(arrayListTitle.get(position));
        holder.bookMarkUrl.setText(arrayListUrl.get(position));

        //load clicked bookmark

        holder.bookMarkUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), MainInterface.class);
                intent.putExtra("url", arrayListUrl.get(position));
                view.getContext().startActivity(intent);

            }
        });
        holder.bookMarkTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), MainInterface.class);
                intent.putExtra("url", arrayListUrl.get(position));
                view.getContext().startActivity(intent);

            }
        });




        //manage bookmark

        holder.bookMarkTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {

                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Delete Bookmark");
                passwordResetDialog.setMessage("Are you sure to delete this bookmark");


                passwordResetDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //delete bookmark


                        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("bookmarks").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));


                        Query deleteQuery = mDatabaseRef.orderByChild("bookMarkUrl").equalTo(arrayListUrl.get(position));

                        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {
                                    for (DataSnapshot dSnapshot : snapshot.getChildren()) {

                                       dSnapshot.getRef().removeValue();
                                        Intent intent = new Intent(view.getContext(), MainInterface.class);
                                        ProgressDialog dialog = ProgressDialog.show(view.getContext(), "",
                                                "Deleting. Please wait...", true);
                                        view.getContext().startActivity(intent);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }



                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


                passwordResetDialog.create().show();


                return true;
            }
        });





    }

    @Override
    public int getItemCount() {
        return arrayListTitle.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView bookMarkTitle , bookMarkUrl;



        public MyViewHolder(View itemView) {
            super(itemView);

            bookMarkTitle = itemView.findViewById(R.id.bookMarkTitle);
            bookMarkUrl = itemView.findViewById(R.id.bookMarkUrl);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

    }
}