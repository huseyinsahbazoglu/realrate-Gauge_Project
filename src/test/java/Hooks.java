import com.thoughtworks.gauge.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Hooks extends BaseSteps{
    protected static WebDriver operaDriver;
    protected static WebDriver chromeDriver;
    OperaOptions operaOptions;
    ChromeOptions chromeOptions;
    public WebDriverWait wait;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    JavascriptExecutor js = (JavascriptExecutor) operaDriver;

    @BeforeSuite
    public void beforeSuite() {
        operaDriver = new OperaDriver(operaOptions());
        operaDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        operaDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        chromeDriver = new ChromeDriver(chromeOptions());
        chromeDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        logger.info("************************************  WebCore  BeforeScenario  ************************************");
        System.out.println("Web drivers were started.");
        H2jdbcDelete();
        H2jdbcCreate();
        System.out.println("H2 Database was created.");
    }
    /*
        @BeforeScenario
        public void beforeScenario(){
            System.out.println("in before scenario");
        }

        @BeforeSpec
        public void beforeSpec(){
            System.out.println("in before spec");
        }

        @BeforeStep
        public void beforeStep(){
            System.out.println("in before step");
        }


        @AfterStep
        public void afterStep() {
            System.out.println("in after step");
        }

        @AfterSpec
        public void afterSpec() {
            System.out.println("in after spec");
        }

        @AfterScenario
        public void afterScenario(){
            System.out.println("in after scenario");
        }
     */
    @AfterSuite
    public void afterSuite() {
        operaDriver.quit();
        chromeDriver.quit();
        stopStmtAndConn();
        System.out.println("Browsers were closed.");
    }
    public ChromeOptions chromeOptions() {
        chromeOptions = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);

        chromeOptions.setExperimentalOption("prefs", prefs)
                //.addArguments("--headless")
                .addArguments("--disable-notifications");
                //.addArguments("--start-maximized")
                //.addArguments("--start-fullscreen");
        System.setProperty("webdriver.chrome.driver", System.getenv().getOrDefault("webdriver-chrome-driver", "webdrivers/chromedriver_v104.0.5112.79"));
        chromeOptions.merge(chromeOptions);
        return chromeOptions;
    }
    public OperaOptions operaOptions() {
        operaOptions = new OperaOptions();
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);

        operaOptions.setExperimentalOption("prefs", prefs)
                .addArguments("--headless")
                .addArguments("--disable-notifications")
                .addArguments("--start-maximized")
                .addArguments("--start-fullscreen");
        System.setProperty("webdriver.opera.driver", System.getenv().getOrDefault("webdriver-opera-driver", "webdrivers/operadriver_v105.0.5195.102"));
        operaOptions.merge(operaOptions);
        return operaOptions;
    }
}