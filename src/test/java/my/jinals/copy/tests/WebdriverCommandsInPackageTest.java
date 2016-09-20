package my.jinals.copy.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;


public class WebdriverCommandsInPackageTest {

    private WebDriver driver;
    private String os;
    private String osVersion;
    private String browser;
    private String browserVersion;
    private String device;
    private String user;
    private String key;
    private String hubUrl;
    private String buildNumber;

    @Rule
    public TestName name = new TestName();

    @Before
    public void setup() throws Exception {
        // JSONParser parser = new JSONParser();
        // try{
        //     Object object = parser.parse(new FileReader("../../common/config.json"));
        //     JSONObject jsonObject = (JSONObject) object;
        //     os = (String)jsonObject.get("os");
        //     osVersion = (String)jsonObject.get("os_version");
        //     browser = (String)jsonObject.get("browser");
        //     browserVersion = (String)jsonObject.get("browser_version");
        //     user = (String)jsonObject.get("browserstack.user");
        //     key = (String)jsonObject.get("browserstack.key");
        //     device = (String)jsonObject.get("device");
        //     hubUrl = (String)jsonObject.get("hub_url");
        //     buildNumber = (String)jsonObject.get("build_number");

        // }catch (FileNotFoundException e) {
        //     System.err.println("Exception in loading config file: "+ e.getMessage());
        //     e.printStackTrace();
        // }
        String username = System.getenv("BROWSERSTACK_USER");
        String accessKey = System.getenv("BROWSERSTACK_ACCESSKEY");
        String browserstackLocal = System.getenv("BROWSERSTACK_LOCAL");
        String browserstackLocalIdentifier = System.getenv("BROWSERSTACK_LOCAL_IDENTIFIER");

        DesiredCapabilities cap = new DesiredCapabilities();

        cap.setCapability("browser", "Chrome");
        // cap.setCapability("browser_version", browserVersion);
        // cap.setCapability("os", os);
        // cap.setCapability("os_version", osVersion);
        // cap.setCapability("device",device);
        // cap.setCapability("browserstack.user",user);
        // cap.setCapability("browserstack.key",key);
        cap.setCapability("browserstack.local", browserstackLocal);
        cap.setCapability("browserstack.localIdentifier", browserstackLocalIdentifier);
        cap.setCapability("browserstack.debug", "true");
        cap.setCapability("build","WebDriver commands test Java " + buildNumber);
        cap.setCapability("browserstack.local", "true");
        // driver = new RemoteWebDriver(new URL(hubUrl),cap);
        driver = new RemoteWebDriver(new URL("https://" + username + ":" + accessKey + "@hub.browserstack.com/wd/hub"), cap);
    }

    @Test
    public void testURL() {
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        Assert.assertEquals(driver.getCurrentUrl(),"http://stormy-beyond-9729.herokuapp.com/" );
    }

    @Test
    public void testTitle(){
        String expectedTitle = "Apache HTTP Server Test Page powered by CentOS";
        driver.get("http://localtesting.browserstack.com/");
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle,expectedTitle);
    }

    @After
    public  void tearDown() {
        if (driver != null)
        {
            driver.quit();
        }
    }
}
