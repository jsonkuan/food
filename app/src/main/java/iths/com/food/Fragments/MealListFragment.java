package iths.com.food.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Helper.MealAdapter;
import iths.com.food.Model.Category;
import iths.com.food.Model.Meal;
import iths.com.food.R;

/**
 * Created by jas0n on 2016-11-25.
 *
 */

public class MealListFragment extends Fragment {

    public static final String MEAL_ID = "meal_id";
    DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_list, container, false);

        setHasOptionsMenu(true);
        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.meallist_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setTitle("FoodFlash!");
        myToolbar.setLogo(R.drawable.empty_heart);

        db = new DatabaseHelper(getActivity());

        Bundle bundle = this.getArguments();
        String category = bundle.getString(CategoryFragment.CHOSEN_CATEGORY);
        Toast.makeText(getActivity(), category, Toast.LENGTH_SHORT).show();
        Category listCategory = db.getCategory(category);
        ArrayList<Meal> mealsInCategory = listCategory.getMeals();
        ArrayList<String> idArray = new ArrayList<>();
        for(int i = 0; i < mealsInCategory.size(); i++) {
            idArray.add(Long.toString(mealsInCategory.get(i).getId()));
        }


        MealAdapter adapter = new MealAdapter(getActivity(), idArray);
        ListView listView = (ListView) view.findViewById(R.id.fragmentMealList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                         long chosenMeal = Long.valueOf(String.valueOf(adapterView.getItemAtPosition(i)));
                        openMeal(chosenMeal);
                    }
                }
        );

        db.close();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //TODO: Implement back button
                Fragment fragment = new CategoryFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            default: super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void openMeal(long id) {
        MealFragment newFragment = new MealFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(MEAL_ID, id);
        newFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, newFragment).addToBackStack(null).commit();
    }
}

