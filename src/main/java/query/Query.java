package query;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Query {
    //private static final Logger log = LoggerFactory.getLogger(Query.class);
    public static void singleContractor(Contractor contractor) throws InterruptedException {
        Logger.getLogger(PhantomJSDriverService.class.getName()).setLevel(Level.OFF);
        String inn;
        String kpp;
        String date;


        DesiredCapabilities caps = new DesiredCapabilities();
        String[] phantomArgs = new  String[] {
                "--webdriver-loglevel=NONE"
        };
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "src/main/resources/drivers/phantomjs-2.1.1/bin/phantomjs.exe");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomArgs);

        WebDriver webDriver = new PhantomJSDriver(caps);




        webDriver.get("http://npchk.nalog.ru/index.html");
        inn = contractor.getInn();
        kpp = contractor.getKpp();
        date = contractor.getDt();

        StringSelection selection = new StringSelection(inn);
        StringSelection selectionkpp = new StringSelection(kpp);
        StringSelection selectiondate = new StringSelection(date);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        clipboard.setContents(selection, selection);
        webDriver.findElement(By.id("inn")).sendKeys(Keys.CONTROL + "v");
        if (!(kpp.equals("000000000"))) {
            clipboard.setContents(selectionkpp, selectionkpp);
            webDriver.findElement(By.id("kpp")).sendKeys(Keys.CONTROL + "v");
        }

        clipboard.setContents(selectiondate, selectiondate);
        webDriver.findElement(By.id("dt")).sendKeys(Keys.CONTROL + "v");

        webDriver.findElement(By.xpath("//*[@id=\"content\"]/form/div[4]/div/div[10]/button")).click();
        WebDriverWait wait = new WebDriverWait(webDriver, 5);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"content\"]/form/div[3]")));
        Thread.sleep(1000);

        String anwser = webDriver.findElement(By.xpath("//*[@id=\"content\"]/form/div[4]/div")).getText();
        if(anwser.contains("и имел статус действуещего в указанную дату")){
            System.out.println("Налогоплательщик зарегистрирован в ЕГРН и имел статус действуещего в указанную дату");
        }
        else if(anwser.contains("но не имел статус действующего в указанную дату")){
            System.out.println("Налогоплательщик зарегистрирован в ЕГРН, но не имел статус действующего в указанную дату");
        }
        else if(anwser.contains("Налогоплательщик зарегистрирован в ЕГРН")){
            System.out.println("Налогоплательщик зарегистрирован в ЕГРН");
        }
        else if(anwser.contains("Несоответствие КПП указанному в запросе ИНН")){
            System.out.println("Несоответствие КПП указанному в запросе ИНН");
        }
        else if(anwser.contains("Налогоплательщик с указанным ИНН зарегистрирован")){
            System.out.println("Налогоплательщик с указанным ИНН зарегистрирован в ЕГРН");
        }
        else if(anwser.contains("Налогоплательщик с указанным ИНН не зарегистрирован")){
            System.out.println("Налогоплательщик с указанным ИНН не зарегистрирован в ЕГРН");
        }
    }
    public static void manyContractors(String nameOfFile) throws InterruptedException{


       String lines = null;
        try {
            lines = ResourceReader.readFromResource(nameOfFile);
        }catch (IOException e) {
            e.printStackTrace();
        }

        System.setProperty("webdriver.chrome.driver","src/main/resources/drivers/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        WebDriver webDriver = new ChromeDriver();


        webDriver.get("http://npchk.nalog.ru/list.html");

        WebDriverWait wait = new WebDriverWait(webDriver, 8);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"content\"]")));

        StringSelection selection = new StringSelection(lines);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        clipboard.setContents(selection, selection);
        webDriver.findElement(By.id("lst")).sendKeys(Keys.CONTROL + "v");

        webDriver.findElement(By.xpath("//*[@id=\"content\"]/form/div[3]/div/div[2]/button")).click();

        URL res = Query.class.getClassLoader().getResource("name.txt");
        File file = null;
        try {
            file = Paths.get(res.toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String absolutePath = file.getAbsolutePath();
        String userName = absolutePath.substring(9);
        userName = (userName.substring(0,userName.indexOf("\\")));
        String path = absolutePath.substring(0,9)+userName+"\\Downloads\\result.txt";
        StringBuilder answer = new StringBuilder();
        try {
          answer = ResourceReader.read(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file2 = new File(path);
        if(file2.delete()) System.out.println( "файл удален");
        if(!(answer.toString().equals("")))System.out.println("0 - Налогоплательщик зарегистрирован в ЕГРН и имел статус действующего в указанную дату\n" +
                "1 - Налогоплательщик зарегистрирован в ЕГРН, но не имел статус действующего в указанную дату\n" +
                "2 - Налогоплательщик зарегистрирован в ЕГРН\n" +
                "3 - Налогоплательщик с указанным ИНН зарегистрирован в ЕГРН, КПП не соответствует ИНН или не указан\n" +
                "4 - Налогоплательщик с указанным ИНН не зарегистрирован в ЕГРН\n" +
                "5 - Некорректный ИНН\n" +
                "6 - Недопустимое количество символов ИНН\n" +
                "7 - Недопустимое количество символов КПП\n" +
                "8 - Недопустимые символы в ИНН\n" +
                "9 - Недопустимые символы в КПП\n" +
                "10 - КПП не должен использоваться при проверке ИП\n" +
                "11 - некорректный формат даты\n" +
                "12 - некорректная дата (ранее 01.01.1991 или позднее текущей даты)\n" +
                "? - ошибка обработки запроса");

        System.out.println();
        System.out.println(answer.toString());



    }

    public static void main(String[] args) throws InterruptedException, URISyntaxException {
        manyContractors("name.txt");


    }

}
