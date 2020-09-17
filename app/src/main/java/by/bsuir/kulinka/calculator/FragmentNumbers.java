package by.bsuir.kulinka.calculator;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


public class FragmentNumbers extends Fragment implements View.OnClickListener
{
    private static final String TAG = "MyLog";
    //Список кнопок
    private List<Button> buttonsList;
    //----------------------------------------------------------------------------------------------
    public FragmentNumbers()
    {
        // Required empty public constructor
    }
    //----------------------------------------------------------------------------------------------
    //Объект интерфейса
    private ExpressionInputInterface expressionInput;
    //----------------------------------------------------------------------------------------------
    //Вызывается при присоединении фрагмента к активности
    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        if (context instanceof ExpressionInputInterface)
        {   //Мы обяжем активность имплементировать наш интерфейс
            //Если какая-то другая активность, которая не имплементирует интерфейс,
            //попытается запустить у себя наш фрагмент,
            expressionInput = (ExpressionInputInterface) context;
        }else
        {   //то программа вылетит
            throw new RuntimeException(context.toString() + "Must implement onExpressionInputListener");
        }
        Log.d(TAG, "Метод OnAttach в Fragment завершился");
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Задаем фрагменту разметку
        Log.d(TAG, "Разметка задаётся");
        return inflater.inflate(R.layout.fragment_numbers, container, false);
    }
    //----------------------------------------------------------------------------------------------
    //Вызывается сразу после возврата onCreateView
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        //Инициализируем массив кнопок
        buttonsList = new ArrayList<>();
        //Находим кнопки и заполняем массив кнопок
        for (int el : initializeButtonsID())
        {
            Button btn = view.findViewById(el);
            btn.setOnClickListener(this);
            buttonsList.add(btn);
        }
        Log.d(TAG, "Кнопки созданы, размер массива = " + buttonsList.size());
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v)
    {
        //Log.d(TAG, "Нажатие в Fragment");
        for (Button btn : buttonsList)
        {
            if (v.getId() == btn.getId())
            {
                expressionInput.expressionInput(btn.getText().toString());
            }
        }
    }
    //----------------------------------------------------------------------------------------------
    private int[] initializeButtonsID()
    {
        return new int[]
                {
                        R.id.buttonNumber0,
                        R.id.buttonNumber1,
                        R.id.buttonNumber2,
                        R.id.buttonNumber3,
                        R.id.buttonNumber4,
                        R.id.buttonNumber5,
                        R.id.buttonNumber6,
                        R.id.buttonNumber7,
                        R.id.buttonNumber8,
                        R.id.buttonNumber9,
                        R.id.buttonSymbolDivide,
                        R.id.buttonSymbolDot,
                        R.id.buttonSymbolMinus,
                        R.id.buttonSymbolMultiply,
                        R.id.buttonSymbolPlus,
                        R.id.buttonSymbolResult
                };
    }
}
