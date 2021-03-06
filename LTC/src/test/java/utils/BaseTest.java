package test.java.utils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;


public class BaseTest extends TestListenerAdapter
{
    protected static InternetExplorerDriver driver;

    @Parameters({ "os","browser" })
    @BeforeTest(alwaysRun = true)
    public void setUp(String os, String browser)
    {
    	
    	System.out.println("Inside before test of base class");
        System.setProperty("os", os);
       // System.setProperty("os_version", os_version);
        System.setProperty("browser", browser);
       // System.setProperty("browser_version", browser_version);
   	
        //driver = new HtmlUnitDriver();
      //  driver = new FirefoxDriver();
        
        //driver = new ChromeDriver();
        
        System.setProperty("webdriver.ie.driver", "C:\\Users\\venkatragavan\\git\\ltc\\LTC\\drivers\\IEDriverServer_Win32_2.44.0\\IEDriverServer.exe");
    	DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
    	capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
    	capabilities.setCapability("ignoreZoomSetting", true);
    	capabilities.setCapability("ignoreProtectedModeSettings" , true);
    	driver = new InternetExplorerDriver(capabilities);
    	driver.manage().window().maximize();
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    driver.get(PropertyUtils.getProperty("app.url"));
	    Actions act = new Actions(driver);
	    new WebDriverWait(driver, 25).until(ExpectedConditions.titleContains("Welcome To"));
    }

    
    
    public static void takeScreenShot(String strTest) {
	
		
		try {
			//File file = new File(sFinalPath + strTest + ".png");
				System.out.println("Inside test base screenshot method..");
			   DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
			   Date date = new Date();
		       String today = dateFormat.format(date);
		   	
		   		File file = new File("C:\\Users\\venkatragavan\\git\\ltc\\LTC\\screenshots\\"+strTest+today+".png");
			//	if (!file.exists()) {
				//	file.mkdir();
			//	}
				
			Thread.sleep(4000);
			File tmpFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(tmpFile, file);
		} catch (IOException ioe ) {
			ioe.printStackTrace();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}

	

    @AfterTest(alwaysRun = true)
    public void tearDown()
    {
    	System.out.println("Inside after test of base class");
        driver.close();
        driver.quit();
    }

    public WebDriver getDriverInstance()
    {
        return driver;
    }

    @Override
    public void onTestSkipped(ITestResult result)
    {
    	Reporter.log("Inside onTestSkipped");
        onTestFailure(result);
    }
    
    @Override
	public void onTestSuccess(ITestResult result) {
		
    	Reporter.log("Inside onTestSuccess");
		String methodName = result.getName().toString().trim();
		
	}
	

    @Override
    public void onTestFailure(ITestResult result)
    {
    	Reporter.log("Inside onTestFailure");
        Object currentClass = result.getInstance();
        WebDriver driver = ((BaseTest) currentClass).getDriverInstance();

        if (driver != null)
        {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            try
            {
                String fileNameToCopy = "target/custom-test-reports/" + result.getTestClass().getName()
                        + "_screenshot.png";
                FileUtils.copyFile(scrFile, new File(fileNameToCopy));
                Reporter.log("[Console Log] Screenshot saved in " + result.getTestClass().getName() + "_screenshot.png");
            } catch (IOException ex)
            {
                // Log error message
            }
        }
    }
}
