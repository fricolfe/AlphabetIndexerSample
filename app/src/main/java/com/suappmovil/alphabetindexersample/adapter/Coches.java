package com.suappmovil.alphabetindexersample.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.SectionIndexer;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.suappmovil.alphabetindexersample.R;
import com.suappmovil.alphabetindexersample.db.MainDB;

/**
 * Created by Fermin on 01/08/2014.
 */
public class Coches extends SimpleCursorAdapter implements SectionIndexer {
    private static final int TYPE_SECTION = 0;
    private static final int TYPE_NORMAL = 1;
    private LayoutInflater mInflater;
    private AlphabetIndexer mIndexer;

    public Coches(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public Cursor swapCursor(Cursor c) {
        //Al cambiar de curor debemos de crear o modificar el indexer
        if (mIndexer == null) {
            if (c!=null) {
                mIndexer = new AlphabetIndexer(c,
                        c.getColumnIndex(MainDB.Coches.Field.MARCA),
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            }
        } else {
            mIndexer.setCursor(c);
        }
        return super.swapCursor(c);
    }

    @Override
    public int getViewTypeCount() {
        //Tenemos 2 tipos de vista, la normal y la seccion
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        //Si la posicion es igual a la primera posicion de su seccion usamos la vista de seccion
        return getPositionForSection(getSectionForPosition(position)) == position ? TYPE_SECTION : TYPE_NORMAL;
    }

    @Override
    public Object[] getSections() {
        if (mIndexer != null) {
            return mIndexer.getSections();
        } else {
            return new Object[0];
        }
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (mIndexer != null) {
            return mIndexer.getPositionForSection(sectionIndex);
        } else {
            return 0;
        }
    }

    @Override
    public int getSectionForPosition(int position) {
        if (mIndexer != null) {
            return mIndexer.getSectionForPosition(position);
        } else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Obtener el modo de la posicion
        int vierType = getItemViewType(position);
        //Obtenemos el modo de la vista reciclable
        int covertViewType = TYPE_NORMAL;
        View view;
        if (convertView != null) {
            //La vista de seccion contiene este label
            view = convertView.findViewById(R.id.lbl_section);
            if (view != null) {
                covertViewType = TYPE_SECTION;
            }
        }
        //La vista debe ser del tipo coche_seccion
        if (vierType == TYPE_SECTION) {
            //Podemos reutilizarla?
            if (convertView != null && covertViewType == TYPE_SECTION) {
                view = convertView;
            } else {
                //Sino la creamos
                view = mInflater.inflate(R.layout.coche_section, parent, false);
            }
        } else {
            //Es del tipo coche_row
            //Podemos reutilizarla?
            if (convertView != null && covertViewType == TYPE_NORMAL) {
                view = convertView;
            } else {
                //Sino la creamos
                view = mInflater.inflate(R.layout.coche_row, parent, false);
            }
        }
        //Llamamos a la clase padre
        return super.getView(position, view, parent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int vt = getItemViewType(cursor.getPosition());
        if (vt == TYPE_SECTION) {
            //
            TextView text = (TextView) view.findViewById(R.id.lbl_section);
            if (text != null) {
                Object section = getSections()[getSectionForPosition(cursor.getPosition())];
                text.setText(section.toString());
            }
        }
        super.bindView(view, context, cursor);
    }
}
