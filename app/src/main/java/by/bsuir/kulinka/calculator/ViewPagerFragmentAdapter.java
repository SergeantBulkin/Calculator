package by.bsuir.kulinka.calculator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import by.bsuir.kulinka.calculator.fragment.FragmentFunctions;
import by.bsuir.kulinka.calculator.fragment.FragmentNumbers;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter
{
    ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle)
    {
        super(fragmentManager, lifecycle);
    }
    //----------------------------------------------------------------------------------------------
    //Создаем фрагменты на позиции
    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        switch (position)
        {
            case 0:
                return new FragmentNumbers();
            case 1:
                return new FragmentFunctions();
        }
        return null;
    }
    //----------------------------------------------------------------------------------------------
    //Количество фрагментов
    @Override
    public int getItemCount()
    {
        return 2;
    }
}
