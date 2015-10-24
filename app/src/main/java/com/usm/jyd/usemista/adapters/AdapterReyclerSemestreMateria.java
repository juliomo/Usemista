package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.objects.Materia;
import com.usm.jyd.usemista.objects.Semestre;

import java.util.List;

/**
 * Created by der_w on 10/23/2015.
 */
public class AdapterReyclerSemestreMateria
        extends ExpandableRecyclerAdapter<
        AdapterReyclerSemestreMateria.SemestreViewHolder,
        AdapterReyclerSemestreMateria.MateriaViewHolder> {

    private LayoutInflater mLayoutInflater;
    /**
     * Primary constructor. Sets up {@link #mParentItemList} and {@link #mItemList}.
     * <p/>
     * Changes to {@link #mParentItemList} should be made through add/remove methods in
     * {@link ExpandableRecyclerAdapter}
     *
     * @param parentItemList List of all {@link ParentListItem} objects to be
     *                       displayed in the RecyclerView that this
     *                       adapter is linked to
     */
    public AdapterReyclerSemestreMateria(Context context, List<ParentListItem> parentItemList) {
        super(parentItemList);
        mLayoutInflater=LayoutInflater.from(context);
    }

    @Override
    public SemestreViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View view=mLayoutInflater.inflate(R.layout.row_rc_semestre,
                parentViewGroup,false);
        return new SemestreViewHolder(view);
    }

    @Override
    public MateriaViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View view =mLayoutInflater.inflate(R.layout.row_rc_semestre_materia,
                childViewGroup,false);
        return new MateriaViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(SemestreViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        Semestre semestre= (Semestre)parentListItem;
        parentViewHolder.textViewSemestre.setText(semestre.getTitle());
    }

    @Override
    public void onBindChildViewHolder(MateriaViewHolder childViewHolder, int position, Object childListItem) {
        Materia materia = (Materia)childListItem;
        childViewHolder.textViewMateria.setText(materia.getTitulo());
    }

    public class SemestreViewHolder extends ParentViewHolder {

        public TextView textViewSemestre;

        public SemestreViewHolder(View itemView) {
            super(itemView);
            textViewSemestre=(TextView)itemView.findViewById(R.id.semestre);
        }

    }

        public class  MateriaViewHolder extends ChildViewHolder{

            public  TextView textViewMateria;
            public MateriaViewHolder(View itemView) {
                super(itemView);
                textViewMateria=(TextView)itemView.findViewById(R.id.materia);
            }
        }
}
