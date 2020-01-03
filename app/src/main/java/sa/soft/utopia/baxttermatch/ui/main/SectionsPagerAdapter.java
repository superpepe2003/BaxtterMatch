package sa.soft.utopia.baxttermatch.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import sa.soft.utopia.baxttermatch.Cards.CardsFragment;
import sa.soft.utopia.baxttermatch.Matches.MatchesFragment;
import sa.soft.utopia.baxttermatch.Perfil.PerfilFragment;
import sa.soft.utopia.baxttermatch.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return PlaceholderFragment.newInstance(position + 1);
        switch (position){
            case 0:
                PerfilFragment perfilFragment = new PerfilFragment();
                return perfilFragment;
            case 1:
                CardsFragment homeFragment = new CardsFragment();
                return homeFragment;
            case 2:
                MatchesFragment matchesFragment = new MatchesFragment();
                return matchesFragment;

        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}