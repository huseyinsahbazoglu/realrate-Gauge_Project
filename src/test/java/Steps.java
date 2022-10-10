import com.thoughtworks.gauge.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Steps extends Hooks {

    @Step("Open to Canlı Döviz Web Page")
    public void canliDovizWebPage() {
        operaDriver.navigate().to("https://canlidoviz.com/");
        System.out.println("Canlı Döviz Page was opened.");
    }

    @Step("Catch-Up to Instant Rates")
    public void getInstantRates() {
        WebElement goldRate = operaDriver.findElement(By.xpath("//div[@class='col-lg-6']//tr[@data-code='GA' and @data-type='table']/td[contains(@class,'canli')]"));
        WebElement dollarRate = operaDriver.findElement(By.xpath("//div[@class='col-lg-6']//tr[@data-code='USD' and @data-type='table']/td[contains(@class,'canli')]"));
        WebElement euroRate = operaDriver.findElement(By.xpath("//div[@class='col-lg-6']//tr[@data-code='EUR' and @data-type='table']/td[contains(@class,'canli')]"));

        saveRate(goldRate, "Altın");
        saveRate(dollarRate, "Dolar");
        saveRate(euroRate, "Euro");
    }

    @Step("Create a Json File for Rates")
    public void createJSONFile() {
        createJSONFile(getRate("Altın"), getRate("Dolar"), getRate("Euro"));
    }

    @Step("Create a Rate Tracking Web Page")
    public void createRTWebPage() {
        createHTMLFile(getRate("Altın"), getRate("Dolar"), getRate("Euro"));
    }

    @Step("Open to Rate Tracking Web Page")
    public void openToRTWebPage() {
        //HTML File
        //chromeDriver.get("file:///" + System.getProperty("user.dir") + "/html/rates.html");

        //Don't forget to define IDE's debugger localhost port value (63342 for Intellij Idea)
        String debuggerPort = "63342";
        chromeDriver.navigate().to("http://localhost:" + debuggerPort + "/realrate/html/rates.html");
    }

    @Step("Continuous Tracking")
    public void continuousTracking() {
        while (true) {
            if (chromeDriver.getWindowHandles().size() == 0) {
                break;
            }
            getInstantRates();
            createJSONFile();
        }
    }
}
