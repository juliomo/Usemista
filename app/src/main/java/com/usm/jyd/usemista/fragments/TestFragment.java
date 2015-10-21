package com.usm.jyd.usemista.fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.usm.jyd.usemista.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends android.support.v4.app.Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private YourRecyclerAdapter yourRecyclerAdapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public TestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout =inflater.inflate(R.layout.fragment_test, container, false);


        recyclerView = (RecyclerView) layout.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        yourRecyclerAdapter = new YourRecyclerAdapter(getActivity());
        recyclerView.setAdapter(yourRecyclerAdapter);


        return layout;
    }


}


class YourRecyclerAdapter extends RecyclerView.Adapter<YourRecyclerAdapter.YourRecyclerViewHolder> {
    private ArrayList<String> list = new ArrayList<>();
    private LayoutInflater inflater;

    public YourRecyclerAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        list.add("A-Bomb (HAS)");
        list.add("A.I.M.");
        list.add("Abe");
        list.add("Abin");
        list.add("Abomination");
        list.add("Abraxas");
        list.add("Absorbing");
        list.add("Adam");
        list.add("Agent Bob");
        list.add("Agent Zero");
        list.add("Air Walker");
        list.add("Ajax");
        list.add("Alan Scott");
        list.add("Alex Mercer");
        list.add("Alex Woolsly");
        list.add("Alfred Pennyworth");
        list.add("Allan Quartermain");
        list.add("Amazo");
        list.add("Ammo Ando");
        list.add("Masahashi Angel");
        list.add("Angel Dust");
        list.add("Angel Salvadore");
        list.add("A-Bomb");
        list.add("Abe");
        list.add("Abin");
        list.add("Abomination");
        list.add("Abraxas");
        list.add("Absorbing");
        list.add("Adam");
        list.add("Agent Bob");
        list.add("Agent Zero");
        list.add("Air Walker");
        list.add("Ajax");
        list.add("Alan Scott");
        list.add("Alex Mercer");
        list.add("Alex Woolsly");
        list.add("Alfred Pennyworth");
        list.add("Allan Quartermain");
        list.add("Amazo");
        list.add("Ammo Ando");
        list.add("Masahashi Angel");
        list.add("Angel Dust");
        list.add("Angel Salvadore");

    }

    @Override
    public YourRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View root = inflater.inflate(R.layout.row_rc_fr_base_test, viewGroup, false);
        YourRecyclerViewHolder holder = new YourRecyclerViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(YourRecyclerViewHolder yourRecyclerViewHolder, int i) {
        yourRecyclerViewHolder.textView.setText(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class YourRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public YourRecyclerViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_superhero);
        }
    }
}
