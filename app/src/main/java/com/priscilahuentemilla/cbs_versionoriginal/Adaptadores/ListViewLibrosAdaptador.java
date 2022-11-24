package com.priscilahuentemilla.cbs_versionoriginal.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.priscilahuentemilla.cbs_versionoriginal.Models.Libro;
import com.priscilahuentemilla.cbs_versionoriginal.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListViewLibrosAdaptador extends BaseAdapter {

    Context context;
    ArrayList<Libro> libroData;
    LayoutInflater layoutInflater;
    Libro libroModel;

    public ListViewLibrosAdaptador(Context context, ArrayList<Libro> libroData) {
        this.context = context;
        this.libroData = libroData;
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
    }

    @Override
    public int getCount() {
        return libroData.size();
    }

    @Override
    public Object getItem(int position) {
        return libroData.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView==null){
            rowView = layoutInflater.inflate(R.layout.lista_libros, null, true);
        }
        //enlace de vistas
        TextView titulo = rowView.findViewById(R.id.titulo);
        TextView autor = rowView.findViewById(R.id.autor);
        TextView genero = rowView.findViewById(R.id.genero);
        TextView paginas = rowView.findViewById(R.id.paginas);
        TextView fecharegistro = rowView.findViewById(R.id.fechaRegistro);

        libroModel = libroData.get(position);
        titulo.setText(libroModel.getTitulo());
        autor.setText(libroModel.getAutor());
        genero.setText(libroModel.getGenero());
        paginas.setText(libroModel.getPaginas());
        fecharegistro.setText(libroModel.getFecharegistro());

        return rowView;
    }
}
