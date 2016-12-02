package iths.com.food.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import iths.com.food.Helper.CategoryAdapter;
import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Model.Category;
import iths.com.food.R;

/**
 * Created by asakwarnmark on 2016-11-23.
 */

public class CategoryFragment extends Fragment {

    public static final String CHOSEN_CATEGORY = "category";
    public ArrayList<String> foodtypes;
    ListAdapter listAdapter;
    DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category, container, false);

        setHasOptionsMenu(true);
        Toolbar myToolbar = (Toolbar) v.findViewById(R.id.category_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        myToolbar.setTitle("FoodFlash!");
        myToolbar.setLogo(R.drawable.empty_heart);

        db = new DatabaseHelper(this.getActivity().getApplicationContext());
        ArrayList<Category> categories = db.getCategories();
        foodtypes = new ArrayList<>(categories.size());
        for (int i = 0; i < categories.size(); i++) {
            foodtypes.add(categories.get(i).getName());
        }

        CategoryAdapter adapter = new CategoryAdapter(getActivity(), foodtypes);
        ListView listView = (ListView) v.findViewById(R.id.fragmentCategory);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String chosenCategory = String.valueOf(adapterView.getItemAtPosition(i));
                        showCategory(chosenCategory);
                    }
                }
        );
        db.close();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.meal_category_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_category_item:
                //TODO: Change layout to AddCategoryFragment
                System.out.println("It Works");
                break;
            default:
                    System.out.println("error");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((BaseAdapter) listAdapter).notifyDataSetChanged();
    }

    private void showCategory(String category) {
        MealListFragment newFragment = new MealListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CHOSEN_CATEGORY, category);
        newFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, newFragment).addToBackStack(null).commit();
    }
}
