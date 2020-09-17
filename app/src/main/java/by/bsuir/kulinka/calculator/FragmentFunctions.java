package by.bsuir.kulinka.calculator;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class FragmentFunctions extends Fragment implements View.OnClickListener
{
    //Список кнопок
    private List<Button> buttonsFunList;
    //----------------------------------------------------------------------------------------------
    public List<Button> getButtonsFunList()
    {
        return buttonsFunList;
    }
    //----------------------------------------------------------------------------------------------
    public FragmentFunctions()
    {
        // Required empty public constructor
    }
    //----------------------------------------------------------------------------------------------
    //Объект интерфейса
    private ExpressionInputInterface expressionFunInput;
    //----------------------------------------------------------------------------------------------
    @Override
    public void onAttach(@NonNull Context context)
    {   //Мы обяжем активность имплементировать наш интерфейс
        //Если какая-то другая активность, которая не имплементирует интерфейс,
        //попытается запустить у себя наш фрагмент,
        super.onAttach(context);
        if (context instanceof ExpressionInputInterface)
        {
            expressionFunInput = (ExpressionInputInterface) context;
        }else
        {   //то программа вылетит
            throw new RuntimeException(context.toString() + "Must implement onExpressionFunInputListener");
        }
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Задаем фрагменту разметку
        return inflater.inflate(R.layout.fragment_functions, container, false);
    }
    //----------------------------------------------------------------------------------------------
    //Вызывается сразу после возврата onCreateView
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        //Инициализируем массив кнопок
        buttonsFunList = new ArrayList<>();
        //Находим кнопки и заполняем массив кнопок
        for(int el : initializeFunButtonsID())
        {
            Button btnFun = view.findViewById(el);
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                btnFun.setTextSize(13);
            }
            btnFun.setOnClickListener(this);
            buttonsFunList.add(btnFun);
        }
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v)
    {
        for (Button btnFun : buttonsFunList)
        {
            if (v.getId() == btnFun.getId())
            {
                expressionFunInput.expressionInput(btnFun.getText().toString());
            }
        }
    }
    //----------------------------------------------------------------------------------------------
    private int[] initializeFunButtonsID()
    {
        return new int[]
                {
                        R.id.buttonFunSin,
                        R.id.buttonFunCos,
                        R.id.buttonFunTg,
                        R.id.buttonFunCtg,
                        R.id.buttonFunPercent,
                        R.id.buttonFunFactorial,
                        R.id.buttonFunSqrt,
                        R.id.buttonFunSquare,
                        R.id.buttonFunLg,
                        R.id.buttonFunLn,
                        R.id.buttonSymbolPI,
                        R.id.buttonSymbolExponent,
                        R.id.buttonSymbolLeftBrace,
                        R.id.buttonSymbolRightBrace,
                        R.id.buttonFunPow,
                        R.id.buttonDegOrRad
                };
    }

}
