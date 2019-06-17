package Err;

public class FieldExceptionInn extends Exception {

    public FieldExceptionInn() {
        super("FieldException: " + "ИНН - недопустимое количество символов в ИНН");
    }
}
