package com.example.firenotes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firenotes.authentication.Splash;
import com.example.firenotes.authentication.login;
import com.example.firenotes.authentication.register;
import com.example.firenotes.model.Note;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
   ActionBarDrawerToggle toggle;
   RecyclerView notelist;
//   adapter adapter;
   FirebaseFirestore fstore;
   FirestoreRecyclerAdapter<Note,NoteViewHolder>  noteAdapter;
   FirebaseUser user;
   FirebaseAuth fauth;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = findViewById(R.id.navigationview);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbara);
        notelist = findViewById(R.id.notelist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Take Notes");


        fstore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        user= fauth.getCurrentUser();

        Query query= fstore.collection("notes").document(user.getUid()).collection("myNotes").orderBy("title",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> allnotes = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class)
                .build();


        noteAdapter = new FirestoreRecyclerAdapter<Note, NoteViewHolder>(allnotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, final int i, @NonNull final Note note) {
                NoteViewHolder.notetitlea.setText(note.getTitle());
                NoteViewHolder.notecontenta.setText(note.getContent());
                final int code= getRandomeColor();
                final String docid = noteAdapter.getSnapshots().getSnapshot(i).getId();
                NoteViewHolder.mcardviewa.setCardBackgroundColor(NoteViewHolder.viewa.getResources().getColor(code,null));
                NoteViewHolder.viewa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "your notes is safe here", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(v.getContext(), shownotedetail.class);
                        intent.putExtra("title",note.getTitle());
                        intent.putExtra("contennt",note.getContent());
                        intent.putExtra("codecolr", code);
                        intent.putExtra("noteid",docid);

                        v.getContext().startActivity(intent);
                    }
                });

                ImageView menuicon= NoteViewHolder.viewa.findViewById(R.id.menuIcon);
                menuicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                      final String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();

                        PopupMenu menu= new PopupMenu(v.getContext(),v);
                        menu.setGravity(Gravity.END);
                        menu.getMenu().add("EDIT").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                              Intent i = new Intent(v.getContext(),editNote.class);
                              i.putExtra("title",note.getTitle());
                              i.putExtra("content",note.getContent());
                              i.putExtra("noteId",docId);
                              startActivity(i);
                                return false;
                            }
                        });
                        menu.getMenu().add("DELETE").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                DocumentReference docref= fstore.collection("notes").document(user.getUid()).collection("myNotes").document(docId);
                                docref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MainActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, "Error in deleting", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                return false;
                            }
                        });

                      menu.show();

                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.noteviewlayout,parent,false);

                return new NoteViewHolder(view);
            }
        };



        navigationView.setNavigationItemSelectedListener(this);


        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();


        View headerview = navigationView.getHeaderView(0);
        TextView userName = headerview.findViewById(R.id.usernamenv);
        TextView userEmail= headerview.findViewById(R.id.useremailnv);

       if (user.isAnonymous()){
           userEmail.setText("Annonymous");
           userName.setText("temperory user");
       }else{
           userEmail.setText(user.getEmail());
           userName.setText(user.getDisplayName());
       }



        FloatingActionButton fab = findViewById(R.id.addnotefloat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(view.getContext(),addnotes.class));

            }
        });









//        List<String> titlees =new ArrayList<>();
//        List<String > contentss = new ArrayList<>();
//        titlees.add("first title");
//        contentss.add("first element content");
//        titlees.add("second title");
//        contentss.add("second element content");
//        titlees.add("third title");
//        contentss.add("third element content");
//        titlees.add("fourth title");
//        contentss.add("fourth element content");

//        adapter= new Adapter(titles,content);
        notelist.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        notelist.setAdapter(noteAdapter);




    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.addnote:
                startActivity(new Intent(this,addnotes.class));
                break;
            case R.id.sync:
                if(user.isAnonymous()){
                    startActivity(new Intent(this, login.class));


                }else {
                    Toast.makeText(this, "you are already connected", Toast.LENGTH_SHORT).show();
                }
            break;
            case R.id.notes:
                Toast.makeText(this, "YOUR NOTES ARE HERE", Toast.LENGTH_SHORT).show();

                break;
            case R.id.lhout:
                if(user.isAnonymous())
                {
                    displayAlert();
                } else{
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), Splash.class));
                    finish();
                }

                break;

            case R.id.shareapp:
                startActivity(new Intent(getApplicationContext(), shareapp.class));
                break;

            case R.id.about:
                startActivity(new Intent(getApplicationContext(),aboutapp.class));
                break;
            case R.id.rating:
                Toast.makeText(this, "thank you user for rating us", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void displayAlert() {
        AlertDialog.Builder warning = new AlertDialog.Builder(this).setTitle("are you sure").setMessage(
                "you are currently a temporary user logout will delete all of your notes .so befor logour please sign in to keep your notres safe ")
                 .setPositiveButton("sync", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         startActivity(new Intent(getApplicationContext(), register.class));
                         finish();
                     }
                 }).setNegativeButton("logout anyway", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //here we delete all notes created by annoymous user
                        //also delete the annoymous user

                        user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(),Splash.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "sorry cant deleted user", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
        warning.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.about){

             startActivity(new Intent(getApplicationContext(),aboutapp.class));
        }

        return super.onOptionsItemSelected(item);
    }

    static public  class  NoteViewHolder extends RecyclerView.ViewHolder{



        static TextView notetitlea;
        static TextView notecontenta;
        static CardView mcardviewa;
        static View viewa;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            notecontenta = itemView.findViewById(R.id.content);
            notetitlea = itemView.findViewById(R.id.titles);
            mcardviewa = itemView.findViewById(R.id.noteCard);
            viewa= itemView;
        }
    }

    private int getRandomeColor() {
        List<Integer> colorcode = new ArrayList<>();

        colorcode.add(R.color.blue);
        colorcode.add(R.color.red);
        colorcode.add(R.color.skyblue);
        colorcode.add(R.color.gray);
        colorcode.add(R.color.greenlight);
        colorcode.add(R.color.pink);
        colorcode.add(R.color.lightPurple);
        colorcode.add(R.color.lightGreen);
        colorcode.add(R.color.notgreen);
        colorcode.add(R.color.yellow);
        Random randomnum= new Random();
        int number = randomnum.nextInt(colorcode.size());

        return colorcode.get(number);

    }

    @Override
    protected void onStart() {
        super.onStart();
       noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter!=null){
            noteAdapter.stopListening();
        }
    }
}
