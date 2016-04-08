package com.company;

import java.io.*;

public class Main {

    private static String text;
    private static String fileName = "C://output.txt";

    //////////////////////////////write///////////////////
    public static void write(String fileName, String text) {

        //Определяем файл
        File file = new File(fileName);

        try {
            //проверяем, что если файл не существует то создаем его
            if(!file.exists()){
                file.createNewFile();
            }

            //PrintWriter обеспечит возможности записи в файл
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                //Записываем текст в файл
                out.print(text);
            } finally {
                //После чего мы должны закрыть файл
                //Иначе файл не запишется
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    //////////////////////////////read///////////////////

    public static String read(String fileName) throws FileNotFoundException {
        //Create a new file
        File file = new File(fileName);

        //Этот спец. объект для построения строки
        StringBuilder sb = new StringBuilder();

        //exists(fileName);// случай когда файл не создан

        try {
            //Объект для чтения файла в буфер
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {
                //В цикле построчно считываем файл
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                //Также не забываем закрыть файл
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        //Возвращаем полученный текст с файла
        return sb.toString();
    }

    private static void exists(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()){
            throw new FileNotFoundException(file.getName());
        }
    }

    //////////////////factorial/////////////////////////////
    public static int factorial(Integer f){
        int n = f;
        int result = 1;
            for (int i = 2; i <= n; i++){
                result *= i;
            } while (n < 0);

        return result;
    }
    ////////////////////////////////////////////////////////





    public static void main(String[] args) throws FileNotFoundException {

        /*   Условие задачи.
        Две команды (А и B) решают судьбу футбольного матча в серии послематчевых пенальти (по правилам ФИФА).
        Текущий счёт 0:0. Первый пенальти бьет команда A. Необходимо вычислить вероятность победы в серии
        пенальти команды A, если известно что вероятность забить пенальти неизменна у каждой из команд
        (т.е. не зависит от счёта и номера удара).

        Пример оформления:
        Входной файл	: input.txt
        Выходной файл	: output.txt
        Формат файла входных данных	 : В единственной строке входного файла расположено два числа,
        разделенных символом ";" - вероятности забить пенальти командой A и командой B.
        Формат файла выходных данных : В выходной файл вывести вероятность победы в серии пенальти команды А.
        */


        try {
            //Попытка прочитать несуществующий файл
            Main.read("C://input.txt");
        }
        catch(FileNotFoundException e) {
            System.out.println("File input not found");
        }

        //Чтение файла
        String textFromFile = Main.read("C://input.txt");
        System.out.println("Данные файла" + textFromFile);

        // преобразуем значения вероятностей из файла string to double

        textFromFile.trim();
        char []  arr = textFromFile.toCharArray();
           for(int i=0; i<arr.length;i++){
               if(arr[i]==';'){
                   double PA = Double.parseDouble(textFromFile.substring(0,i));
                   double PB = Double.parseDouble(textFromFile.substring(i+1,arr.length));
                   System.out.println("Pa ( гола первой комманды) = " + PA);
                   System.out.println("Pb ( гола второй комманды) = " + PB);

                   /*
                    Обе команды выполняют по пять ударов.
                    Команда, забившая большее количество голов, становится победителем матча.
                    Вероятность победы команды A.
                    Задача на закон распределения Бернулли
                    Пусть X - число забитых голов командой A (принимает значение от 0 .. 5),
                          A - событие забить гол коммандой A, получаем пять событий в каждом из которых может произойти A.
                          A происходит с заданной вероятностью Pa);
                      Тогда значения вероятности случайной велечины X находим по формуле Бернулли
                       P (X = m)= ((Pa)^m )* (1-Pa)^(5-m)    где m = (0 .. 5); n=5
                       распределение X имеет вид :

                        X	0	1	2	3	4	5
                        P	p0	p1	p2	p3	p4	p5

                        где pj=C(j,5) * p^j * (1-p)^(5-j) ,где j=0,1,...,5
                        и С(j,5) = 5! / [ j! * (5-j)! ]

                      Тогда мат. ожидание находится по формуле : M[X]=0*p0+1*p1+2*p2+3*p3+4*p4+5*p5,
                      а дисперсия: D[X]=0*p0+1*p1+4*p2+9*p3+16*p4+25*p5 - ( M[X] )^2 =  n*p*q
                      где n = 5; p = Pa; q = 1-Pa;
                      Среднее кврадратическое отклонение S(x) = SQRT ( n*p*q);
                     */

                   // распределение по бернулли
                   double [] PV = new double[6];
                   double PM=0, M = 0 ,D=0, S=0;
                   for(int m=0; m<6;m++){

                       PV [m] = (120/(factorial(m)*factorial(5-m)))*Math.pow(PA,m)*Math.pow(1-PA,5-m);
                        System.out.println("Факториал от "+m+ "="+factorial(m));
                        System.out.println("Факториал от "+(5-m)+ "="+factorial(5-m));

                       // вероятность того что комманда забъет 5 из 5
                       PM+=PV[m];

                       // Математическое ожидание (т.е. вероятность того что команда A забъет 5 голов)
                       M += m*PV[m];
                   }
                   // Дисперсия
                   D = 5*PA*(1-PA);

                   // СКО
                   S = Math.sqrt(5*PA*(1-PA));

                   System.out.println("Мат ожидание того что первая комманда забъет 5 голов  = " + M
                                        +'\n'+"Дисперсия  = "+D);



                   //String rez = new Double (PM).toString();
                   System.out.println("Вероятность что комманда забъет 5 из 5  = " + PM);
                   //Запись в файл
                   // Main.write(fileName, rez);


                   /*
                   когда серии по 5 ударов закончились вничью,
                   и далее идут микроматчи из 2х пенальти до "мгновенной смерти".
                   Обозначим
                   PA =p(1-q) вероятность 1-й команде (забивающей с вероятностью p) выиграть микроматч,
                   PB =q(1-p) вероятность 2-й команде (забивающей с вероятностью q) выиграть микроматч,
                   и остается еще вероятность , что микроматч вничью и нужен следующий.
                   Получаем условную вероятность выигрыша 1-й команды

                   PA+PA*(1-PA-PB )+PA*(1-PA-PB )^2+... =PA/(PA + PB) (сумма бесконечной геом. прогрессии)

                    */

                   double pa = PA*(1-PB);
                   double pb = PB*(1-PA);
                   double rez2 = pa+pa*(1-pa-pb)+ pa*(Math.pow((1-pa-pb),2))+ pa*(Math.pow((1-pa-pb),3))
                           + pa*(Math.pow((1-pa-pb),4))+ pa*(Math.pow((1-pa-pb),5));
                   String rezstr = new Double (rez2).toString();
                   System.out.println("Вероятность что комманда победит в микроматче  = " + rezstr);
                   //Запись в файл
                   Main.write(fileName, rezstr);

               }
           }

    }


}
