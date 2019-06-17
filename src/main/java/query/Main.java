package query;

import Err.FieldExceptionInn;
import Err.FieldExceptionInnLenght;

import java.util.Scanner;

public class Main {
    public static void main(String[] args)throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите ИНН.");
        String inn = scanner.next();

        char[] charInn =  inn.toCharArray();
        for (char i :charInn){ if(i>57||i<48) throw new FieldExceptionInn(); }
        if(inn.length()>12||inn.length()==11||inn.length()<10){ throw new FieldExceptionInnLenght(); }

        System.out.println("Введите КПП, если КПП отсутствует введите \"000000000\"");
        String kpp = scanner.next();

        char[] charKpp =  kpp.toCharArray();
        for (char i :charKpp){ if(i>57||i<48) throw new FieldExceptionInn(); }
        if(kpp.length()!=9) throw new FieldExceptionInnLenght();

        System.out.println("Введите дату для проверки в таком виде число, месяц, год (без ввода будет указана сегодняшняя дата)");
        String date = scanner.next();


        //5247017800
        //524701001
        //03.06.2019


        Contractor contractor = new Contractor(inn,kpp,date);
        Query.singleContractor(contractor);
    }
}
