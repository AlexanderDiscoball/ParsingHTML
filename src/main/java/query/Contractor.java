package query;

import Err.FieldExceptionInn;
import Err.FieldExceptionInnLenght;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Contractor  {
    private static final Logger log = LoggerFactory.getLogger(Contractor.class);

    private final String inn;
    private final String kpp;
    private final String DEFKPP = "000000000";
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final String date;
    public Contractor(String inn){
        this.inn = inn;
        this.kpp = DEFKPP;
        this.date = dateFormat.format(LocalDate.now());
    }
    public Contractor(String inn,String kpp){
        this.inn = inn;
        this.kpp = kpp;
        this.date = dateFormat.format(LocalDate.now());
    }
    public Contractor(String inn,String kpp, String date) {

        this.inn = inn;
        this.kpp = kpp;
        this.date = date;
    }

    public String getDt() {
        return date;
    }

    public String getInn() {
        return inn;
    }

    public String getKpp() {
        return kpp;
    }

    @Override
    public String toString(){
        return
                 inn + " "+
                 kpp + " "+
                 date+'\n';
    }

}
