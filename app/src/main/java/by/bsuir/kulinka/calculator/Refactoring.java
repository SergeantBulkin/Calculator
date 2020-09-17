package by.bsuir.kulinka.calculator;

import android.util.Log;

final class Refactoring
{
    //Переменная для логов
    private static final String TAG = "Refactoring";
    //----------------------------------------------------------------------------------------------
    static String finalInputRefactor(String input)
    {
        //------------------------------------------------------------------
        //Удалить знак операции в конце строки
        input = deleteSymbol(input);
        //------------------------------------------------------------------
        //Заменить PI
        input = replacePI(input);
        //------------------------------------------------------------------
        //Заменить %
        input = replacePercent(input);
        //------------------------------------------------------------------
        //Заменить квадратный корень
        input = replaceSqrt(input);
        //------------------------------------------------------------------
        //Заменить факториал
        input = replaceFactorial(input);
        //------------------------------------------------------------------
        //Закрыть скобки
        input = closeBrace(input);
        //------------------------------------------------------------------
        //Заменить ctg на cot
        while (input.contains("ctg"))
        {
            input = input.replaceAll("ctg", "cot");
        }
        Log.d(TAG, "Тангенс заменен");
        //------------------------------------------------------------------
        //Заменить tg на tan
        while (input.contains("tg"))
        {
            input = input.replaceAll("tg", "tan");
        }
        Log.d(TAG, "Котангенс заменен");
        //------------------------------------------------------------------
        //Заменить lg на log10
        while (input.contains("lg"))
        {
            input = input.replaceAll("lg", "log10");
        }
        Log.d(TAG, "lg заменен");
        //------------------------------------------------------------------
        //Заменить ln на log
        while (input.contains("ln"))
        {
            input = input.replaceAll("ln", "log");
        }
        Log.d(TAG, "ln заменен");
        //------------------------------------------------------------------
        Log.d(TAG, "После рефактор " + input);
        return input;
    }
    //----------------------------------------------------------------------------------------------
    //Удалить знак операции в конце строки
    private static String deleteSymbol (String input)
    {
        if (input.charAt(input.length()-1) == '/' || input.charAt(input.length()-1) == '*' ||
                input.charAt(input.length()-1) == '-' || input.charAt(input.length()-1) == '+' ||
                input.charAt(input.length()-1) == '%' || input.charAt(input.length()-1) == '^')
        {
            input = input.substring(0, input.length() - 1);
            Log.d(TAG, "Последний символ - 1 - " + input);
        }
        if (input.charAt(input.length()-1) == '!' || input.charAt(input.length()-1) == '√' ||
                input.charAt(input.length()-1) == '∛')
        {
            input = input.substring(0, input.length() - 2);
            Log.d(TAG, "Последний символ - 2 - " + input);
        }
        return input;
    }
    //----------------------------------------------------------------------------------------------
    //Заменить PI
    private static String replacePI (String input)
    {
        while (input.contains("π"))
        {
            input = input.replaceAll("π", "PI");
        }
        Log.d(TAG, "PI заменено");
        return input;
    }
    //----------------------------------------------------------------------------------------------
    //Заменить факториал
    private static String replacePercent (String input)
    {
        int position; //позиция процента

        String subString; //Подстрока, которую необходимо заменить
        String replacement; //Подстрока, на которую будем менять

        while (input.contains("%"))
        {
            //Ищем первый знак процента
            position = input.indexOf("%");

            //Получаем число слева процента
            subString = searchSubstr(input, position);

            //Вставляем скобки слева и справа
            input = percentBraces(input, position);

            double delit100 = Double.parseDouble(subString);
            delit100 = delit100 / 100; //Делим его на 100

            //Прибавляем поделенное число к строке
            replacement = "" + delit100;

            input = input.replace(subString+"%", replacement+"*");
            Log.d(TAG, "Проход while " + input);
        }
        Log.d(TAG, "% заменен");
        return input;
    }
    //----------------------------------------------------------------------------------------------
    //Заменить квадратный корень
    private static String replaceSqrt (String input)
    {
        int position; //позиция корня
        while (input.contains("√"))
        {
            //Ищем позицию знака корня
            position = input.indexOf("√");
            //Проверяем, если стоит следующим символом скобка
            if (input.charAt(position+1) == '(')
            {
                input = input.replace("√", "sqrt");
            }else
            {
                input = braceOrNot(input, position);
                input = input.replace("√", "sqrt(");
            }
        }
        Log.d(TAG, "Квадратный корень заменен");
        return input;
    }
    //----------------------------------------------------------------------------------------------
    //Заменить факториал
    private static String replaceFactorial (String input)
    {
        int position; //позиция факториала
        while (input.contains("!"))
        {
            //Ищем позицию знака факториал
            position = input.indexOf("!");
            //Проверяем, если стоит следующим символом скобка
            if (input.charAt(position+1) == '(')
            {
                input = input.replace("!", "fact");
            }else
            {
                input = braceOrNot(input, position);
                input = input.replace("!", "fact(");
            }
        }
        Log.d(TAG, "Факториал заменен");
        return input;
    }
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    //Вспомогательный метод для поиска числа перед знаком, стоящим на позиции position
    private static String searchSubstr(String inputStr, int position)
    {
        int index = position - 1;

        while (true)
        {
            if (index != 0 && Character.isDigit(inputStr.charAt(index))) index--;
            else break;

        }
        if (!Character.isDigit(inputStr.charAt(index)))
        {
            index++;
        }
        inputStr = inputStr.substring(index, position);
        Log.d(TAG, "searchSubstr " + inputStr);
        return inputStr;
    }
    //----------------------------------------------------------------------------------------------
    //Закрыть скобки
    static String closeBrace(String inputStr)
    {
        int openBraceAmount = 0;
        int closeBraceAmount = 0;
        for (int i = 0; i < inputStr.length(); i++)
        {
            switch (inputStr.charAt(i))
            {
                case '(':
                    openBraceAmount++;
                    break;
                case ')':
                    closeBraceAmount++;
                    break;
            }
        }
        if(openBraceAmount - closeBraceAmount > 0)
        {
            StringBuilder inputBuilder = new StringBuilder(inputStr);
            for (int i = 0; i < (openBraceAmount-closeBraceAmount); i++)
            {
                inputBuilder.append(")");
            }
            inputStr = inputBuilder.toString();
        }
        Log.d(TAG, "Скобки закрыты");
        return inputStr;
    }
    //----------------------------------------------------------------------------------------------
    private static String braceOrNot(String input, int position)
    {
        position++;
        while (Character.isDigit(input.charAt(position)) || input.charAt(position) == '!' ||
                input.charAt(position) == '√' || input.charAt(position) == '∛')
        {
            if (input.length() - position - 1 == 0)
            {
                input = input.concat(")");
                return input;
            }else
            {
                position++;
            }
        }
        if (input.charAt(position) != ')')
        {
            input = insertBrace(input, position, false);
        }else
        {
            return input;
        }
        return input;
    }
    //----------------------------------------------------------------------------------------------
    static String insertBrace(String inputStr, int position, boolean flag)
    {
        StringBuilder sb = new StringBuilder(inputStr);
        if (flag)
        {
            sb.insert(position, "(");
        } else
        {
            sb.insert(position, ")");
        }
        return sb.toString();
    }
    //----------------------------------------------------------------------------------------------
    private static String percentBraces (String inputStr, int position)
    {
        //Вставить скобку перед числом, стоящим перед процентом
        if (!(inputStr.charAt(position - 1) == ')'))
        {
            position--;
            while (true)
            {
                if (position != 0 && Character.isDigit(inputStr.charAt(position))) position--;
                else break;
            }
            if (position == 0)
            {
                inputStr = insertBrace(inputStr, position, true);
            } else
            {
                inputStr = insertBrace(inputStr, position + 1, true);
            }
            Log.d(TAG, "percentBraces " + inputStr);
        }
        //Вставить скобку после числа, стоящего после процента
        int position2 = inputStr.indexOf('%');
        if (!(inputStr.charAt(position2 + 1) == '('))
        {
            position2++;
            while (true)
            {
                if (position2 != inputStr.length() && Character.isDigit(inputStr.charAt(position2))) position2++;
                else break;
            }
            inputStr = insertBrace(inputStr, position2, false);
            Log.d(TAG, "percentBraces 2 " + inputStr);
        }
        return inputStr;
    }
}