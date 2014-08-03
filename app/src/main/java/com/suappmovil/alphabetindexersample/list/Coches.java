package com.suappmovil.alphabetindexersample.list;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

import com.suappmovil.alphabetindexersample.MainActivity;
import com.suappmovil.alphabetindexersample.R;
import com.suappmovil.alphabetindexersample.db.MainDB;

import static com.suappmovil.alphabetindexersample.content.Coches.getUriRead;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * interface.
 */
public class Coches extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private OnListCochesListener mListener;
    private com.suappmovil.alphabetindexersample.adapter.Coches mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Coches() {
    }

    public static Coches newInstance(int sectionNumber) {
        Coches fragment = new Coches();
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        getListView().setFastScrollEnabled(true);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Change Adapter to display your content

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getString(R.string.sin_coches));
        // We have a menu item to show in action bar.
        setHasOptionsMenu(true);
        mAdapter = new com.suappmovil.alphabetindexersample.adapter.Coches(
                this.getActivity().getBaseContext(),
                R.layout.coche_row,
                null,
                new String[]{
                        MainDB.Coches.Field.MARCA
                        , MainDB.Coches.Field.MODELO
                        , MainDB.Coches.Field.VERSION
                        , MainDB.Coches.Field.ANYO,

                },
                new int[]{
                        R.id.txt_marca
                        , R.id.txt_modelo
                        , R.id.txt_version
                        , R.id.txt_anyo

                },
                0
        );
        this.setListAdapter(mAdapter);
        // Start out with a progress indicator.
        setListShown(false);


        this.getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnListCochesListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnListCochesListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        getListView().setItemChecked(position, true);
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onListCochesItemClick(id);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri uri = getUriRead();
        return new CursorLoader(this.getActivity().getBaseContext(), uri, null, null,null, "Marca COLLATE NOCASE,Modelo COLLATE NOCASE,Version COLLATE NOCASE");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
        // The list should now be shown.
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
        // The list should now be shown.
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListCochesListener {
        // TODO: Update argument type and name
        public void onListCochesItemClick(long id);
    }

}
