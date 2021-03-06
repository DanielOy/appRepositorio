package com.example.danny.apprepositorio;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.danny.apprepositorio.entidades.Foro;
import com.example.danny.apprepositorio.utilidades.Adaptador;
import com.example.danny.apprepositorio.utilidades.Entidad;
import com.example.danny.apprepositorio.utilidades.Utilidades;
import com.example.danny.apprepositorio.utilidades.UtilidadesForo;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ForoPrincipalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ForoPrincipalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForoPrincipalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView listViewForo;
    ArrayList<String> listaInformacion;
    ArrayList<Foro> listaForo;
    ConexionSQLiteHelper conn;


    private Adaptador adaptador;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ForoPrincipalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForoPrincipalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForoPrincipalFragment newInstance(String param1, String param2) {
        ForoPrincipalFragment fragment = new ForoPrincipalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private ArrayList<Entidad> GetArrayItems() {

        ArrayList<Entidad> listitems  = new ArrayList<>();
        for(Foro item:listaForo)
        {
            switch (item.getLenguaje()){
                case "Java":
                listitems.add(new Entidad(R.drawable.java_b,item.titulo,item.descripcion));
                    break;
                    case "C#":
                listitems.add(new Entidad(R.drawable.cplus_b,item.titulo,item.descripcion));
                    break;
                    case "Python":
                listitems.add(new Entidad(R.drawable.python_b,item.titulo,item.descripcion));
                    break;
                    case "JS":
                listitems.add(new Entidad(R.drawable.javascript_b,item.titulo,item.descripcion));
                    break;
                    case "Android":
                listitems.add(new Entidad(R.drawable.android_b,item.titulo,item.descripcion));
                    break;
                    case "Escritorio":
                listitems.add(new Entidad(R.drawable.escritorio_b,item.titulo,item.descripcion));
                    break;
                    case "Web":
                listitems.add(new Entidad(R.drawable.web_b,item.titulo,item.descripcion));
                    break;
            }

        }
        return listitems;
    }


    private void consultarListaForo() {
        SQLiteDatabase db = conn.getReadableDatabase();

        Foro foro =null;
        listaForo=new ArrayList<Foro>();
        //select * from usuarios
        Cursor cursor = db.rawQuery("SELECT * FROM "+UtilidadesForo.TABLA_FORO,null);

        while (cursor.moveToNext()){
            foro = new Foro();
            foro.setId(cursor.getInt(0));
            foro.setTitulo(cursor.getString(1));
            foro.setLenguaje(cursor.getString(2));
            foro.setDescripcion(cursor.getString(3));
            foro.setAutor(cursor.getString(4));
            foro.setFecha(cursor.getString(5));

            listaForo.add(foro);
        }

        obtenerLista();
    }

    public void consultarlist(){
        conn = new ConexionSQLiteHelper(getActivity(),"bd_app",null,1);

        try{
            consultarListaForo();

            //ArrayAdapter adaptador = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,listaInformacion);
            //listViewForo.setAdapter(adaptador);
            listViewForo.setAdapter((new Adaptador(getActivity(),GetArrayItems())));
            //Comentario

            listViewForo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    opciones(view, position);
                    return false;
                }
            });
        }catch (Exception e){
            Toast.makeText(getActivity(), "Error we", Toast.LENGTH_SHORT).show();
        }
    }

    private void opciones(final View view, final int position) {

        final CharSequence[] opciones={"Ver Detalles","Eliminar registro"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(view.getContext());
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Ver Detalles")){
                    Toast.makeText(view.getContext(), "Ver detalles", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(view.getContext(),ForoTopicActivity.class);
                    in.putExtra("Autor",listaForo.get(position).getAutor());
                    in.putExtra("Titulo",listaForo.get(position).getTitulo());
                    in.putExtra("Descripcion",listaForo.get(position).getDescripcion());
                    in.putExtra("Plataforma",listaForo.get(position).getLenguaje());
                    in.putExtra("Id",listaForo.get(position).getId());
                    startActivity(in);
                }else{
                    if (opciones[i].equals("Eliminar registro")){
                        EliminarRegistro(position,view);
                    }else{
                        dialogInterface.dismiss();}
                }

            }
        });
        alertOpciones.show();

    }

    private void EliminarRegistro(int position,View view) {
        try {
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parametros = {listaForo.get(position).getFecha()};
        db.delete(UtilidadesForo.TABLA_FORO,UtilidadesForo.CAMPO_FECHA+"=?",parametros);
        Toast.makeText(view.getContext(), "Registro eliminado", Toast.LENGTH_SHORT).show();}
        catch (Exception e){}
    }

    private void obtenerLista() {
        listaInformacion = new ArrayList<String>();
        for (int i=0;i<listaForo.size();i++){
            listaInformacion.add(listaForo.get(i).getTitulo() +" - "+
                    listaForo.get(i).getAutor());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_foro_principal, container, false);
        listViewForo = (ListView)v.findViewById(R.id.entradaforo);

        consultarlist();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
