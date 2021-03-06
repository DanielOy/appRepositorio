package com.example.danny.apprepositorio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ForoActivity extends AppCompatActivity
        implements ForoAportacionesFragment.OnFragmentInteractionListener,
        ForoPrincipalFragment.OnFragmentInteractionListener
{

    ForoPrincipalFragment fragmentPrincipal;
    ForoAportacionesFragment fragmetAportaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foro);

        fragmentPrincipal=new ForoPrincipalFragment();
        fragmetAportaciones = new ForoAportacionesFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.contenedorrFragment,fragmentPrincipal).commit();

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onclickForo(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (view.getId())
        {
            case R.id.btnPrincipal:
                transaction.replace(R.id.contenedorrFragment,fragmentPrincipal);
                break;
            case R.id.btnAportaciones:
                transaction.replace(R.id.contenedorrFragment,fragmetAportaciones);
                break;
        }
        transaction.commit();
    }
    public void onclickAdd(View view) {
        Intent add = new Intent(getApplicationContext(),AddForoActivity.class);
        //startActivity(add);
        startActivity(add);
        //finish();
    }
}
