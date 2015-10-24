package com.usm.jyd.usemista.objects;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Created by der_w on 10/23/2015.
 */
public class Semestre implements ParentListItem {

    private List<Materia> materiaList;
    private String title;

    public String getTitle(){return title;}
    public void setTitle(String title){this.title=title;}

    public Semestre(){}
    public void setMateriaItemList(List<Materia> list) {
        materiaList = list;
    }
    @Override
    public List<Materia> getChildItemList() {
        return materiaList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
