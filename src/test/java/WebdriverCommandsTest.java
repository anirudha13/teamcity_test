import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.SessionNotFoundException;


public class WebdriverCommandsTest {

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
        JSONParser parser = new JSONParser();
        try{
            Object object = parser.parse(new FileReader("../../common/config.json"));
            JSONObject jsonObject = (JSONObject) object;
            os = (String)jsonObject.get("os");
            osVersion = (String)jsonObject.get("os_version");
            browser = (String)jsonObject.get("browser");
            browserVersion = (String)jsonObject.get("browser_version");
            user = (String)jsonObject.get("browserstack.user");
            key = (String)jsonObject.get("browserstack.key");
            device = (String)jsonObject.get("device");
            hubUrl = (String)jsonObject.get("hub_url");
            buildNumber = (String)jsonObject.get("build_number");

        }catch (FileNotFoundException e) {
            System.err.println("Exception in loading config file: "+ e.getMessage());
            e.printStackTrace();
        }
        String username = System.getenv("BROWSERSTACK_USER");
        String accessKey = System.getenv("BROWSERSTACK_ACCESSKEY");
        String browserstackLocal = System.getenv("BROWSERSTACK_LOCAL");
        String browserstackLocalIdentifier = System.getenv("BROWSERSTACK_LOCAL_IDENTIFIER");

        DesiredCapabilities cap = new DesiredCapabilities();

        cap.setCapability("browser", browser);
        cap.setCapability("browser_version", browserVersion);
        cap.setCapability("os", os);
        cap.setCapability("os_version", osVersion);
        cap.setCapability("device",device);
        // cap.setCapability("browserstack.user",user);
        // cap.setCapability("browserstack.key",key);
        cap.setCapability("browserstack.local", browserstackLocal);
        cap.setCapability("browserstack.localIdentifier", browserstackLocalIdentifier);
        cap.setCapability("browserstack.debug", "true");
        cap.setCapability("build","WebDriver commands test Java " + buildNumber);
        cap.setCapability("name", name.getMethodName());
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
        String expectedTitle = "Selenium playground";
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle,expectedTitle);
    }

    @Test
    public void testBackForward(){
        String expectedUrl = "https://www.facebook.com/";
        driver.get("http://www.browserstack.com");
        driver.get(expectedUrl);
        driver.navigate().back();
        driver.navigate().forward();
        String actualUrl = driver.getCurrentUrl();
        Assert.assertEquals(actualUrl,expectedUrl);
    }

    @Test
    public void testSendKeys(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        String expectedInput = "Testing sendKeys";
        driver.findElement(By.id("q")).sendKeys(expectedInput);
        String actualInput =  driver.findElement(By.id("q")).getAttribute("value");
        Assert.assertEquals(expectedInput,actualInput);
    }

    @Test
    public void testRefresh(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        String inputText = "Testing sendKeys";
        driver.findElement(By.id("q")).sendKeys(inputText);
        driver.navigate().refresh();
        String inputAfterRefresh =  driver.findElement(By.id("q")).getAttribute("value");
        Assert.assertTrue(inputAfterRefresh.isEmpty());
    }

    @Test
    public void testGetElementById(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.id("q"));
        String actualValue = element.getAttribute("name");
        String expectedValue = "post[title]";
        Assert.assertEquals(expectedValue,actualValue);
    }

    @Test
    public void testGetElementByCss(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.cssSelector(".ui-draggable"));
        String actualValue = element.getAttribute("style");
        String expectedValue = "position: relative;";
        Assert.assertEquals(expectedValue,actualValue);
    }

    @Test
    public void testGetElementByClassName(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.className("ui-draggable"));
        String actualValue = element.getAttribute("style");
        String expectedValue = "position: relative;";
        Assert.assertEquals(expectedValue,actualValue);
    }


    @Test
    public void testGetImgSrcAttribute(){
        driver.get("https://www.w3.org");
        WebElement element = driver.findElement(By.cssSelector("div#w3c_mast img"));
        String actualValue = element.getAttribute("src");
        String expectedValue = "https://www.w3.org/2008/site/images/logo-w3c-mobile-lg";
        Assert.assertEquals(expectedValue,actualValue);
    }

    @Test
    public void testGetElementByLinkText(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.linkText("Cheese"));
        Assert.assertTrue(element.isDisplayed());
    }

    @Test
    public void testGetElementByPartialLinkText(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.partialLinkText("Eg"));
        Assert.assertTrue(element.isDisplayed());
    }

    @Test
    public void testGetElementByXpath(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.xpath("//div/p"));
        String actualValue = element.getAttribute("style");
        String expectedValue = "position: relative;";
        Assert.assertEquals(expectedValue,actualValue);
    }

    @Test
    public void testGetElementByName(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.name("post[title]"));
        String actualValue = element.getAttribute("id");
        String expectedValue = "q";
        Assert.assertEquals(expectedValue,actualValue);
    }

    @Test
    public void testGetElementByTagName(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.tagName("link"));
        String actualValue = element.getAttribute("rel");
        String expectedValue = "stylesheet";
        Assert.assertEquals(expectedValue,actualValue);
    }

    @Test
    public void testGetElements(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        List<WebElement> element = driver.findElements(By.cssSelector("#navcontainer ul li"));
        int size = element.size();
        Assert.assertTrue(size==5);
    }

    @Test
    public void testGetElementElements(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        List<WebElement> element = driver.findElement(By.id("navcontainer")).findElements(By.cssSelector("ul li"));
        int size = element.size();
        Assert.assertTrue(size==5);
    }

    @Test
    public void testGetElementElement(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.className("ui-widget-content")).findElement(By.id("draggable"));
        String actualValue = element.getAttribute("style");
        String expectedValue = "position: relative;";
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testClick(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        String someInput = "testing";
        driver.findElement(By.id("q")).sendKeys(someInput);
        WebElement element = driver.findElement(By.cssSelector("input[type='submit']"));
        element.click();
        WebDriverWait wait = new WebDriverWait(driver,5);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("q")));
        String inputAfterClick =  driver.findElement(By.id("q")).getAttribute("value");
        Assert.assertTrue(inputAfterClick.isEmpty());
    }

    @Test
    public void testClear(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        String someInput = "testing";
        driver.findElement(By.id("q")).sendKeys(someInput);
        WebElement element = driver.findElement(By.id("q"));
        element.clear();
        String inputAfterClick =  driver.findElement(By.id("q")).getAttribute("value");
        Assert.assertTrue(inputAfterClick.isEmpty());

    }

    @Test
    public void testSubmit(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        String titleBeforeSubmit = "testing";
        driver.findElement(By.id("q")).sendKeys(titleBeforeSubmit);
        WebElement element = driver.findElement(By.cssSelector("input[type='submit']"));
        element.submit();
        String titleAfterSubmit =  driver.getTitle();
        Assert.assertEquals(titleBeforeSubmit,titleAfterSubmit);

    }

    @Test
    public void testGetText(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        String expectedText = "Drag & Drop";
        String actualTest = driver.findElement(By.cssSelector("h2")).getText();
        Assert.assertEquals(expectedText,actualTest);
    }

    @Test
    public void testGetTagName(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        String expectedTag = "p";
        WebElement element = driver.findElement(By.id("draggable"));
        String actualTag = element.getTagName();
        Assert.assertEquals(expectedTag,actualTag);
    }

    @Test
    public void testGetCssValue(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        String expectedCssValue = "16px";
        String actualCssValue = driver.findElement(By.id("draggable")).getCssValue("font-size");
        Assert.assertEquals(expectedCssValue,actualCssValue);
    }

    @Test
    public void testIsSelected(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.cssSelector("select#multiplecars option[value='mercedes']"));
        element.click();
        Assert.assertTrue(element.isSelected());
    }

    @Test
    public void testIsDisplayed(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.id("q"));
        Assert.assertTrue(element.isDisplayed());

    }

    @Test
    public void testIsEnabled(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/test");
        WebElement element = driver.findElement(By.cssSelector("select.select-box"));
        Assert.assertFalse(element.isEnabled());

    }


    @Test
    public  void testSwitchToAlertText(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/test");
        WebElement element = driver.findElement(By.id("alert"));
        element.click();
        String actualAlertText = driver.switchTo().alert().getText();
        Assert.assertEquals("Welcome to selenium playground",actualAlertText);
    }

    @Test
    public  void testAlertAccept(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/test");
        WebElement element = driver.findElement(By.id("alert"));
        element.click();
        driver.switchTo().alert().accept();
    }

    @Test
    public  void testAlertDismiss(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/test");
        WebElement element = driver.findElement(By.id("alert"));
        element.click();
        driver.switchTo().alert().dismiss();
    }

    @Test
    public void testWindowHandles(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        driver.findElement(By.id("openModal")).click();
        Set<String> winHandles = driver.getWindowHandles();
        driver.switchTo().window((String) winHandles.toArray()[winHandles.size()-1]);
        String actualBodyText = driver.findElement(By.cssSelector("body")).getText();
        Assert.assertEquals("You have opened a new window",actualBodyText);
    }

    @Test
    public void testGetWindowHandle(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        String handle = driver.getWindowHandle();
        Assert.assertFalse(handle.isEmpty());
    }

    @Test
    public void testWindowSizeAndMaximize() {
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        Dimension newSize = new Dimension(100,100);
        driver.manage().window().setSize(newSize);
        Dimension newSizeOfWindow = driver.manage().window().getSize();
        driver.manage().window().maximize();
        Dimension currentSize = driver.manage().window().getSize();
        Assert.assertNotEquals(newSizeOfWindow, currentSize);
    }

    @Test
    public void testWindowPosition() {
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        Point positionBefore = driver.manage().window().getPosition();
        driver.manage().window().setPosition(positionBefore.moveBy(200,200));
        Point positionAfter = driver.manage().window().getPosition();
        Assert.assertNotEquals(positionBefore,positionAfter);
        driver.manage().window().maximize();
    }

    @Test
    public void testExecuteScript(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        String expectedTitle = "Selenium playground";
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        String actualTitle = ((String)js.executeScript("return document.title"));
        Assert.assertEquals(expectedTitle,actualTitle);
    }

    @Test
    public void testExecuteAsyncScript(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        String expectedTitle = "Selenium playground";
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        String actualTitle = ((String)js.executeScript("return document.title"));
        Assert.assertEquals(expectedTitle,actualTitle);
    }

    @Test
    public void  testPageSource(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        String pageSource = driver.getPageSource();
        Assert.assertNotNull(pageSource);
    }

    @Test
    public void testActiveElement(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        String input = "Testing";
        WebElement element = driver.findElement(By.id("q"));
        element.sendKeys(input);
        element = driver.switchTo().activeElement();
        element.clear();
        String finalInput =  driver.findElement(By.id("q")).getAttribute("value");
        Assert.assertTrue(finalInput.isEmpty());
    }

    @Test
    public void testElementLocation(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.id("q"));
        Point location = element.getLocation();
        Dimension dim = element.getSize();
        Assert.assertTrue(location.getX()>0);
        Assert.assertTrue(location.getY()>0);
    }

    @Test
    public void testElementSize(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.id("q"));
        Dimension dim = element.getSize();
        Assert.assertTrue(dim.getHeight()>0);
        Assert.assertTrue(dim.getWidth()>0);
    }

    @Test
    public void testMoveToElement(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/test");
        WebElement element = driver.findElement(By.id("hoverme"));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).build().perform();
        String actualHoverText = driver.findElement(By.id("hovertext")).getText();
        Assert.assertEquals("Hover in",actualHoverText);
    }

    @Test
    public void testClickAndHold(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.cssSelector("input[type='submit']"));
        Actions actions = new Actions(driver);
        actions.clickAndHold(element).build().perform();
        actions.moveToElement(element).clickAndHold().build().perform();
    }

    @Test
    public void testActionClick() {
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.cssSelector("input[type='submit']"));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().build().perform();
        WebDriverWait wait = new WebDriverWait(driver,4);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='submit']")));
        element = driver.findElement(By.cssSelector("input[type='submit']"));
        actions.click(element).build().perform();
    }

    @Test
    public void testDragAndDrop() {
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.id("draggable"));
        Point intialPosition = element.getLocation();
        WebElement destElement = driver.findElement(By.cssSelector("input[type='submit']"));
        Actions actions = new Actions(driver);
        actions.dragAndDrop(element, destElement).build().perform();
        Point finalPosition = driver.findElement(By.id("draggable")).getLocation();
        Assert.assertNotEquals(intialPosition,finalPosition);
        intialPosition = finalPosition;
        actions.dragAndDropBy(destElement,10,10).build().perform();
        finalPosition = driver.findElement(By.id("draggable")).getLocation();
        Assert.assertNotEquals(intialPosition,finalPosition);
    }

    @Test
    public void testContextClickAtElement(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.id("q"));
        Actions action = new Actions(driver);
        action.contextClick(element).build().perform();
    }

    @Test
    public void testContextClick(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.id("q"));
        Actions action = new Actions(driver);
        action.contextClick().build().perform();
    }

    @Test
    public void testKeyDownKeyUp(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        Actions builder = new Actions(driver);
        WebElement element1 = driver.findElement(By.linkText("Milk"));
        WebElement element2 = driver.findElement(By.linkText("Eggs"));
        builder.keyDown(Keys.CONTROL).click(element1).click(element2).keyUp(Keys.CONTROL).build().perform();
    }

    @Test
    public void testHoldAndRelease(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        Actions builder = new Actions(driver);
        WebElement element1 = driver.findElement(By.id("draggable"));
        WebElement element2 = driver.findElement(By.id("q"));
        builder.clickAndHold(element1).moveToElement(element2).release().build().perform();
    }

    @Test
    public void testActionSendKeys(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = driver.findElement(By.id("q"));
        Actions action = new Actions(driver);
        String expectedInput = "Some Keys";
        action.sendKeys(element,expectedInput).build().perform();
        String actualInput = element.getAttribute("value");
        Assert.assertEquals(expectedInput,actualInput);
    }

    @Test
    public void testScreenshot(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);
    }

    @Test
    public void testImplicitWait(){
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        driver.findElement(By.id("q"));
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    }

    @Test
    public void testExplicitWait(){
        WebDriverWait wait = new WebDriverWait(driver,10);
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type='submit']")));
    }

    @Test
    public void testAddCookieAndGetCookie(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        Cookie expectedCookie = new Cookie("cookie","coo");
        driver.manage().addCookie(expectedCookie);
        Cookie actualCookie = driver.manage().getCookieNamed("cookie");
        Assert.assertEquals(expectedCookie,actualCookie);
    }

    @Test
    public void testGetAllCookies(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        Cookie newCookie = new Cookie("cookie","coo");
        driver.manage().addCookie(newCookie);
        Set allCookies = driver.manage().getCookies();
        Assert.assertTrue(allCookies.contains(newCookie));
    }

    @Test
    public void testDeleteCookieNamed(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        Cookie newCookie = new Cookie("cookie","coo");
        driver.manage().addCookie(newCookie);
        driver.manage().deleteCookieNamed("cookie");
        Set allCookies = driver.manage().getCookies();
        Assert.assertFalse(allCookies.contains(newCookie));
    }

    @Test
    public void testDeleteAllCookies(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        Cookie newCookie = new Cookie("cookie","coo");
        driver.manage().addCookie(newCookie);
        driver.manage().deleteAllCookies();
        Set allCookies = driver.manage().getCookies();
        Assert.assertFalse(allCookies.contains(newCookie));
    }

    @Test
    public void testSwitchToFrame(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/frame");
        driver.switchTo().frame("menu");
        WebElement element = driver.findElement(By.cssSelector("a[href='sample.html']"));
        Assert.assertTrue(element.isDisplayed());
    }

    @Test
    public void testSwitchToDefaultContent(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/frame");
        driver.switchTo().frame("menu");
        WebElement element = driver.findElement(By.cssSelector("a[href='sample.html']"));
        Assert.assertTrue(element.isDisplayed());
        driver.switchTo().defaultContent();
        try {
            driver.findElement(By.cssSelector("a[href='sample.html']"));
        }
        catch (NoSuchElementException e)
        {
            String str = e.getMessage();
            Assert.assertTrue(str.contains("no such element"));
        }
    }

    @Test
    public void testCloseCurrentWindow(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        driver.close();
        try {
            driver.get("http://stormy-beyond-9729.herokuapp.com/");
        }
        catch (Exception e)
        {
            String str = e.getMessage();
            Assert.assertTrue(str.contains("no such session"));
        }

    }

    @Test
    public void testDoubleClick(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/test");
        Actions actions = new Actions(driver);
        actions.doubleClick(driver.findElement(By.cssSelector("div#doubleclick"))).build().perform();
        String randomText = driver.findElement(By.cssSelector("div#random1")).getText();
        Assert.assertFalse(randomText.isEmpty());
    }

    @Test
    public void testGetLocalStorageKey(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        String storageValue = js.executeScript("return window.localStorage.getItem('firstname');").toString();
        Assert.assertEquals("Jason", storageValue);
    }

    @Test
    public void testClearLocalStorage(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("window.localStorage.clear();");
        String storageValue = (String)(js.executeScript("return window.localStorage.getItem('firstname');"));
        Assert.assertNull(storageValue);
    }

    @Test
    public void testLocalStorageSize(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("window.localStorage.clear();");
        String storageValueLength = js.executeScript("return window.localStorage.length;").toString();
        Assert.assertTrue(storageValueLength.matches("0"));
    }

    @Test
    public void testWaitForElementVisiblity(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/test");
        WebDriverWait wait = new WebDriverWait(driver, 20);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delaytext")));
        Assert.assertTrue(element.isDisplayed());
    }

    @Test
    public void testControlKeyDown(){
        driver.get("http://stormy-beyond-9729.herokuapp.com/");
        Actions action = new Actions(driver);
        WebElement element1 = driver.findElement(By.cssSelector("#multiplecars option[value='volvo']"));
        WebElement element2 = driver.findElement(By.cssSelector("#multiplecars option[value='mercedes']"));
        action.keyDown(Keys.CONTROL).build().perform();
        element1.click();
        element2.click();
        action = new Actions(driver);
        action.keyUp(Keys.CONTROL).build().perform();
        driver.findElement(By.id("getcars")).click();
        String selected = driver.findElement(By.id("selectedcars")).getText();
        Assert.assertEquals("Volvo\nMercedes",selected);
    }

    @Test
    public void testFileUpload(){
        String text = "Hello World";
        ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
        driver.get("http://stormy-beyond-9729.herokuapp.com/upload");
        File f = null;
        String path = null;
        try{
            f = new File("../../common/file_upload.txt");
            path = f.getAbsolutePath();

        }catch(Exception e){
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector("input[name='myfile']")).sendKeys(path);
        driver.findElement(By.cssSelector("input[type='submit']")).click();
        text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals("Hello World",text);

    }

    @After
    public  void tearDown() {
        if (driver != null)
        {
            driver.quit();
        }
    }
}
