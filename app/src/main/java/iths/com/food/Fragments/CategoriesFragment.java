package iths.com.food.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import iths.com.food.R;

/**
 * Created by asakwarnmark on 2016-11-23.
 */

public class CategoriesFragment extends Fragment{

    ListView listViewOfCategories;
    ArrayList<String> categoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        listViewOfCategories = (ListView) view.findViewById(R.id.listViewOfCategories);

        categoryList = new ArrayList<>();

        categoryList.add("1");
        categoryList.add("2");
        categoryList.add("3");
        categoryList.add("4");
        categoryList.add("5");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categoryList);

        listViewOfCategories.setAdapter(adapter);

        return view;
    }

}
