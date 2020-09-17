package by.bsuir.kulinka.calculator;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.math.MathContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import by.bsuir.kulinka.calculator.interfaces.ExpressionInputInterface;

public class MainActivity extends AppCompatActivity implements ExpressionInputInterface
{
    //Переменная для логов
    private static final String TAG = "MyLog";

    //Объявляем поля EditText
    EditText outputTextView;
    EditText inputTextView;

    //Объявляем кнопки
    Button btnClear;
    Button btnDelete;

    //ViewPager2
    ViewPager2 viewPager2;
    ViewPagerFragmentAdapter viewPagerFragmentAdapter;

    //Переменная для результата вычислений
    BigDecimal bigDecimal = null;

    //Переменная для ввода и вывода текста
    String input = "";
    String output = "";
    String finalInput = "";
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Находим поля EditText
        outputTextView = findViewById(R.id.outputTextView);
        inputTextView = findViewById(R.id.inputTextView);

        //Находим кнопки
        btnClear = findViewById(R.id.buttonClear);
        //Обработчик на кнопку Clear
        btnClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                input = "";
                inputTextView.setText("");
                outputTextView.setText("");
            }
        });
        btnDelete = findViewById(R.id.buttonDelete);
        //Обработчик на кнопку Delete
        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (inputTextView.getText().toString().length() != 0)
                {
                    input = inputTextView.getText().toString().substring(0, inputTextView.getText().toString().length() - 1);
                }
                inputTextView.setText(input);
            }
        });

        //Узнаём ориентацию экрана
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            //Находим ViewPager2
            viewPager2 = findViewById(R.id.viewPager2);
            //Создаем адаптер
            viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());
            //Устанвока ориентации прокрутки
            viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            //Установка адаптера
            viewPager2.setAdapter(viewPagerFragmentAdapter);
        }
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            btnClear.setText("C");
            btnClear.setTextSize(15);
            btnDelete.setText("D");
            btnDelete.setTextSize(15);
        }

        Log.d(TAG, "Метод OnCreate в Activity завершился");
    }
    //----------------------------------------------------------------------------------------------
    //Сохранение введенных данных
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "Метод onSaveInstanceState в Activity завершился");
        outState.putString("output", outputTextView.getText().toString());
        outState.putString("input", inputTextView.getText().toString());
    }
    //----------------------------------------------------------------------------------------------
    //Восстановление введенных данных
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "Метод onRestoreInstanceState в Activity завершился");
        String inp = savedInstanceState.getString("input");
        input = inp;
        inputTextView.setText(inp);

        String outp = savedInstanceState.getString("output");
        output = outp;
        outputTextView.setText(outp);
    }
    //----------------------------------------------------------------------------------------------
    //Реализация интерфейса слушателя фрагмента
    @Override
    public void expressionInput(String inputSymbol)
    {
        //Log.d(TAG, "Метод интерфейса");

        //Если предыдущее сообщение было об ошибке, то убираем его
        if (outputTextView.getText().toString().equals("Неверное выражение"))
        {
            outputTextView.setTextSize(55);
            outputTextView.setText("");
        }

        //Проверка ввода первого символа
        if (input.isEmpty())
        {
            Log.d(TAG, "Поле ввода пустое");
            firstCheck(inputSymbol);
            return;
        }

        //Нажатие кнопок
        switch (inputSymbol)
        {
            case "=":
                //Если нажата кнопка равно
                Log.d(TAG, "Нажата кнопка равно");
                //Log.d(TAG, "Исходная строка - " + input);
                //Log.d(TAG, "Преобразованная строка - " + finalInput);
                calculate();
                break;
            case "Х²":
                input = Refactoring.closeBrace(input);
                input = Refactoring.insertBrace(input, 0, true);
                input = Refactoring.insertBrace(input, input.length(), false);
                input = input + "*" + input;
                calculate();
                break;
            case "DEG":
                break;
            default:
                //Проверка на ввод следующего символа
                if (check(inputSymbol))
                {
                    input += inputSymbol;
                    inputTextView.setText(input);
                }else
                {
                    inputTextView.setText(input);
                }
                break;
        }
    }
    //----------------------------------------------------------------------------------------------
    //Проверка вводимых символов
    private boolean check(String newSymbol)
    {
        //Находим последний символ
        char lastSymbol = input.charAt(input.length() - 1);
        if (input.length() > 1)
        {   //Если в строке 2 и более символов

            //Находим предпоследний символ
            char almostLastSymbol = input.charAt(input.length() - 2);

            switch (newSymbol)
            {
                case "-":
                    switch (lastSymbol)
                    {
                        case '-':
                        case '+':
                        case '.':
                        case '^':
                            if (Character.isDigit(almostLastSymbol))
                            {
                                input = input.substring(0, input.length()-1);
                                input += newSymbol;
                            }
                            return false;
                        case '%':
                            input += "*" + newSymbol;
                            return false;
                        case '√':
                        case '∛':
                            return false;
                        case '0':
                            if (almostLastSymbol == '/')
                                return false;
                        case '*':
                        case '/':
                        default:
                            return true;
                    }
                case "+":
                case "/":
                case "*":
                case "^":
                    switch (lastSymbol)
                    {
                        case '-':
                        case '+':
                        case '/':
                        case '.':
                        case '*':
                        case '^':
                        case '!':
                            if (Character.isDigit(almostLastSymbol))
                            {
                                input = input.substring(0, input.length()-1);
                                input += newSymbol;
                                return false;
                            }
                        case '%':
                        case '(':
                        case '√':
                        case '∛':
                            return false;
                        case '0':
                            if (almostLastSymbol == '/')
                                return false;
                        default:
                            return true;
                    }
                case ".":
                    if (lastSymbol == 'e' || lastSymbol == 'π' || lastSymbol == '(' || lastSymbol == '√' || lastSymbol == '∛')
                    {
                        return false;
                    }
                    switch (lastSymbol)
                    {
                        case '-':
                        case '+':
                        case '/':
                        case '.':
                        case '*':
                        case '^':
                        case '!':
                        case '%':
                            if (Character.isDigit(almostLastSymbol))
                            {
                                input = input.substring(0, input.length()-1);
                                input += newSymbol;
                                return false;
                            }
                        default:
                            return true;
                    }
                case "e":
                case "π":
                case "∛":
                case "√":
                    if (lastSymbol == '0' && almostLastSymbol == '/')
                        return false;
                    if (Character.isDigit(lastSymbol) || lastSymbol == 'e' || lastSymbol == 'π')
                    {
                        input += "*" + newSymbol;
                        return false;
                    }
                    if (lastSymbol == '.')
                    {
                        input = input.substring(0, input.length()-1);
                        input += "*" + newSymbol;
                        return false;
                    }
                    return true;
                case "sin":
                case "cos":
                case "tg":
                case "ctg":
                case "lg":
                case "ln":
                    if (lastSymbol == '0' && almostLastSymbol == '/')
                        return false;
                    if (Character.isDigit(lastSymbol))
                    {
                        input += "*" + newSymbol + "(";
                        return false;
                    }
                    if (lastSymbol == '+' || lastSymbol == '-' || lastSymbol == '*' ||
                            lastSymbol == '/' || lastSymbol == '^' || lastSymbol == '√' ||
                            lastSymbol == '∛' || lastSymbol == '%' || lastSymbol == '(' ||
                            lastSymbol == '!')
                    {
                        input += newSymbol + "(";
                        return false;
                    }
                    if (lastSymbol == '.')
                    {
                        input = input.substring(0, input.length()-1);
                        input += "*" + newSymbol + "(";
                        return false;
                    }
                    return true;
                case "!":
                    if (Character.isDigit(lastSymbol) && lastSymbol != '0')
                    {
                        input = input + "*" + newSymbol;
                        return false;
                    }
                    switch (lastSymbol)
                    {
                        case '!':
                            return false;
                        case '.':
                            input = input.substring(0, input.length()-1);
                            input += newSymbol;
                            return false;
                        case '0':
                            if (almostLastSymbol == '/')
                                return false;
                        default:
                            return true;
                    }
                case "%":
                    if (lastSymbol == '.')
                    {
                        input = input.substring(0, input.length()-1);
                        input += newSymbol;
                        return false;
                    }
                    if (!Character.isDigit(lastSymbol))
                    {
                        return false;
                    }
                    if (lastSymbol == '0' && almostLastSymbol == '/')
                        return false;
                case "(":
                case ")":
                    if (lastSymbol == '0' && almostLastSymbol == '/')
                        return false;
                default:
                    return true;
            }
        }else
        {   //Если в строке один символ------------------------------------
            switch (newSymbol)
            {
                case "e":
                case "π":
                case "∛":
                case "√":
                    if (Character.isDigit(lastSymbol) || lastSymbol == 'π' || lastSymbol == 'e')
                    {
                        input += "*" + newSymbol;
                        return false;
                    }
                    return true;
                case "sin":
                case "cos":
                case "tg":
                case "ctg":
                case "lg":
                case "ln":
                    if (Character.isDigit(lastSymbol) || lastSymbol == 'e' || lastSymbol == 'π')
                    {
                        input = input + "*" + newSymbol + "(";
                        return false;
                    }

                    switch (lastSymbol)
                    {
                        case '-':
                            input = input + "1*" + newSymbol + "(";
                            return false;
                        case '!':
                            return false;
                        case '√':
                        case '∛':
                            input = input + newSymbol + "(";
                            return false;
                    }
                    return true;
                case ".":
                    if (lastSymbol == 'e' || lastSymbol == 'π' || lastSymbol == '!' || lastSymbol == '-' || lastSymbol == '√' || lastSymbol == '∛' || lastSymbol == '(')
                    {
                        return false;
                    }
                    return true;
                case "!":
                    if (Character.isDigit(input.charAt(0)))
                    {
                        input = input + "*" + newSymbol;
                        return false;
                    }
                    switch (lastSymbol)
                    {
                        case '!':
                            return false;
                        case '-':
                            input = input + "1*" + newSymbol;
                            return false;
                        case 'e':
                        case 'π':
                            input = input + "*" + newSymbol;
                            return false;
                    }
                    return true;
                case "-":
                case "+":
                case "/":
                case "*":
                case "%":
                case "^":
                    if (lastSymbol == '-' || lastSymbol == '√' || lastSymbol == '∛' || lastSymbol == '!' || lastSymbol =='(')
                    {
                        return false;
                    }
            }
        }
        return true;
    }
    //----------------------------------------------------------------------------------------------
    //Ввод первого символа
    private void firstCheck(String inputSymbol)
    {
        //Если строка пуста
        //Log.d(TAG, "Первая проверка");
        if (Character.isDigit(inputSymbol.charAt(0)) || inputSymbol.equals("("))
        {
            input += inputSymbol;
            inputTextView.setText(input);
            return;
        }else
        {
            switch (inputSymbol)
            {
                case "sin":
                case "cos":
                case "tg":
                case "ctg":
                case "lg":
                case "ln":
                    input += inputSymbol + "(";
                    inputTextView.setText(input);
                    return;
                case "e":
                case "π":
                case "∛":
                case "√":
                case "!":
                    input += inputSymbol;
                    inputTextView.setText(input);
                    return;
            }
        }
        Log.d(TAG, "Переход к switch");
        switch (inputSymbol.charAt(0))
        {
            case '-':
                input += inputSymbol;
                inputTextView.setText(input);
                return;
            case '+':
            case '.':
            case '*':
            case '/':
            case '^':
                break;
        }
    }
    //----------------------------------------------------------------------------------------------
    //Обработка строки и вычисление результата
    private void calculate()
    {
        try
        {
            //Последнее редактирование перед выводом
            finalInput = Refactoring.finalInputRefactor(input);
        } catch (Exception ex)
        {
            Log.d(TAG, "Ошибка finalInputRefactor " + ex.toString());
            outputTextView.setTextSize(35);
            outputTextView.setText("Неверное выражение");
            input = "";
            output = "";
        }

        try
        {
            //Объявление переменной для ответа и установка точности
            bigDecimal = new Expression(finalInput, new MathContext(8)).eval();
        } catch (Expression.ExpressionException ex)
        {
            Log.d(TAG, "Expression.Exception");
            outputTextView.setTextSize(35);
            outputTextView.setText("Неверное выражение");
            input = "";
            output = "";
            ex.printStackTrace();
            return;
        } catch (Exception ex)
        {
            Log.d(TAG, "Exception");
            outputTextView.setTextSize(35);
            outputTextView.setText("Неверное выражение");
            input = "";
            output = "";
            ex.printStackTrace();
            ex.printStackTrace();
        }

        try
        {
            output = bigDecimal.toString();
        } catch (NullPointerException ex)
        {
            outputTextView.setText("Неверное выражение");
            ex.printStackTrace();
        }

        //Если длинный текст, то уменьшаем размер шрифта
        if (output.length() > 11)
        {
            outputTextView.setTextSize(47);
        } else
        {
            outputTextView.setTextSize(55);
        }
        outputTextView.setText(output);
    }
    //----------------------------------------------------------------------------------------------
}