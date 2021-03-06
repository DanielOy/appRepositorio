package com.example.danny.apprepositorio;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.danny.apprepositorio.entidades.Aportaciones;
import com.example.danny.apprepositorio.entidades.Foro;
import com.example.danny.apprepositorio.utilidades.UtilidadesAportaciones;
import com.example.danny.apprepositorio.utilidades.UtilidadesForo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ForoAportacionesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ForoAportacionesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForoAportacionesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView listViewAportaciones;
    ArrayList<String> listaInformacion;
    ArrayList<Aportaciones> listaAportaciones;
    ConexionSQLiteHelper conn;

    private OnFragmentInteractionListener mListener;

    public ForoAportacionesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForoAportacionesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForoAportacionesFragment newInstance(String param1, String param2) {
        ForoAportacionesFragment fragment = new ForoAportacionesFragment();
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

    private void consultarListaForo() {
        SQLiteDatabase db = conn.getReadableDatabase();

        Aportaciones aportaciones =null;
        listaAportaciones =new ArrayList<Aportaciones>();
        //select * from usuarios
        String[] parametros = new String[]{("user")};
        Cursor cursor = db.rawQuery("SELECT * FROM "+UtilidadesAportaciones.TABLA_APORTACIONES+" WHERE "+UtilidadesAportaciones.CAMPO_USUARIO+
                "=?",parametros);
        cursor.moveToFirst();

        while (cursor.moveToNext()){
            aportaciones = new Aportaciones();
            aportaciones.setId(cursor.getInt(0));
            aportaciones.setUsuario(cursor.getString(1));
            aportaciones.setTitulo(cursor.getString(2));
            aportaciones.setPlataforma(cursor.getString(3));
            aportaciones.setDescripcion(cursor.getString(4));
            aportaciones.setFecha(cursor.getString(5));

            listaAportaciones.add(aportaciones);
        }

        obtenerLista();
    }

    public void consultarlist(){
        conn = new ConexionSQLiteHelper(getActivity(),"bd_app",null,1);

        try{
            consultarListaForo();

            ArrayAdapter adaptador = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,listaInformacion);
            listViewAportaciones.setAdapter(adaptador);
            //Comentario
            listViewAportaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String informacion = "id: "+ listaAportaciones.get(position).getId()+"\n";
                    informacion+="Titulo: "+ listaAportaciones.get(position).getTitulo()+"\n";
                    informacion+="Descripcion: "+ listaAportaciones.get(position).getDescripcion();
                    Toast.makeText(getActivity(), informacion, Toast.LENGTH_SHORT).show();
                }
            });}catch (Exception e){
            Toast.makeText(getActivity(), "Error we", Toast.LENGTH_SHORT).show();
        }
    }
    private void obtenerLista() {
        listaInformacion = new ArrayList<String>();
        for (int i = 0; i< listaAportaciones.size(); i++){
            Date date= new Date();
           try {
               String str  = (listaAportaciones.get(i).getFecha());
               SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
               date = dateFormat.parse(str);
           }catch (Exception ex){}

            listaInformacion.add( date.toString()+" - "+
                    listaAportaciones.get(i).getDescripcion());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_foro_aportaciones, container, false);
        listViewAportaciones = (ListView)v.findViewById(R.id.listaAportaciones);
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
