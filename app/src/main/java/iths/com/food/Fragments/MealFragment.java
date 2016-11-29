package iths.com.food.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import iths.com.food.Helper.CategoryAdapter;
import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.R;

/**
 * Created by jas0n on 2016-11-25.
 */

public class MealFragment extends Fragment {

    public ArrayList<String> foodtypes;
    ListAdapter listAdapter;
    DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal, container, false);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("0");
        testArray.add("1");
        testArray.add("2");
        testArray.add("3");
        testArray.add("99999");

        CategoryAdapter adapter = new CategoryAdapter(getActivity(), testArray);
        ListView listView = (ListView) view.findViewById(R.id.fragmentMealList);
        listView.setAdapter(adapter);


        return view;
    }


}
