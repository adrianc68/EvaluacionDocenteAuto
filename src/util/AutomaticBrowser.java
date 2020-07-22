package util;

import browser.Browser;
import browser.BrowserConstructor;
import browser.ChromeBrowserBuilder;
import browser.FirefoxBrowserBuilder;
import browser.SafariBrowserBuilder;
import evaluation.AnswerElement;
import evaluation.ProfessorElement;
import evaluation.QuestionElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AutomaticBrowser {
    private String url;
    private String usernameElementXpath;
    private String passwordElementXPath;
    private String buttonLoginElementXpath;
    private String buttonNextElementXpath;
    private String questionElementXpath;
    private String questionOptionElementXpath;
    private Browser browser;

    private void navigateInLoginPage(String account, String password) {
        WebDriver driver = browser.getWebDriver();
        driver.get("https://eval.uv.mx/evaluacion/Estudiante/Estudiante");
        driver.findElement(By.xpath("//*[@id='username']")).sendKeys(account);
        driver.findElement(By.xpath("//*[@id='password']")).sendKeys(password);
        driver.findElement(By.xpath("//*[@id='BtnLogin']")).click();
    }

    private List<WebElement> findElements(String xpath) {
        WebDriver driver = browser.getWebDriver();
        List<WebElement> list = driver.findElements(By.xpath(xpath));
        return list;
    }

    private WebElement findElement(String xpath) {
        WebDriver driver = browser.getWebDriver();
        WebElement webElement = driver.findElement(By.xpath(xpath));
        return webElement;
    }


    public Object[] getProfessorAndQuestions(String browserType, String account, String password) {
        List<ProfessorElement> professorElementsList = new ArrayList<>();
        List<QuestionElement> questionElementsList = new ArrayList<>();
        browser = buildConcreteBrowser(browserType);
        WebDriver driver = browser.getWebDriver();
        navigateInLoginPage(account, password);
        List<WebElement> professorElements = findElements("//p[@class = 'title']");
        int professorWebPosition = 1;
        for(WebElement webElement: professorElements){
            ProfessorElement professorElement = new ProfessorElement();
            professorElement.setProfessor( webElement.getText() );
            professorElement.setPositionWebElement(professorWebPosition++);
            professorElementsList.add(professorElement);
        }
        WebDriverWait wait = browser.getWebDriverWait();
        professorElements.get(0).click();
        int count = 1;
        do {
            WebElement questionElement = findElement("//p[@class = 'pregunta']");
            QuestionElement questionWebElement = new QuestionElement();
            questionWebElement.setQuestion( questionElement.getText() );
            List<WebElement> questionElements = findElements("//div[@class = 'opciones']//child::div");
//            System.out.println(count);
//            System.out.println(questionElement.getText() );
//            showTableComponents(driver);
            List<AnswerElement> answerElementsList = new ArrayList<>();
            int positionAnswerWebElement = 1;
            for(WebElement webElement: questionElements) {
                AnswerElement answerElement = new AnswerElement();
                answerElement.setAnswer( webElement.getText() );
                answerElement.setElementPosition(positionAnswerWebElement);
                answerElementsList.add(answerElement);
                positionAnswerWebElement++;
            }
            questionWebElement.setAnswerElements(answerElementsList);
            questionElementsList.add(questionWebElement);
            count++;
            wait.until( ExpectedConditions.elementToBeClickable( By.xpath("//*[@id='btnPregSig']") ) ).click();
        } while ( !existElement(driver) );
        Object[] list = new Object[2];
        list[0] = professorElementsList;
        list[1] = questionElementsList;
        driver.close();
        return list;
    }



    public void showTableComponents(WebDriver driver) {
        List<WebElement> list = driver.findElements(By.xpath("//tbody[@class = 'filas']//child::tr") );
        list.forEach( (value) -> {
            System.out.println(value.getText());
        } );
    }

    public void evaluateOptions(String browserType,HashMap<String, AnswerElement> selectedAnswerHashMap, String account, String password, String professorName) {
        browser = buildConcreteBrowser(browserType);
        WebDriver driver = browser.getWebDriver();
        WebDriverWait wait = browser.getWebDriverWait();
        driver.get("https://eval.uv.mx/evaluacion/Estudiante/Estudiante");
        driver.findElement(By.xpath("//*[@id=\"username\"]")).sendKeys(account);
        driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(password);
        driver.findElement(By.xpath("//*[@id=\"BtnLogin\"]")).click();
        wait.until( ExpectedConditions.elementToBeClickable(By.xpath("//p[@class = 'title' and contains(text(), '" + professorName + "')]"))).click();
        selectedAnswerHashMap.forEach( (key, value) -> System.out.println(key));
        while( !existElement(driver) ) {
            String pregunta = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[@class = 'pregunta']"))).getText();
            if( selectedAnswerHashMap.get(pregunta) != null ) {
                String xpath = "//*[@class = 'radio radio-primary' or @class ='checkbox checkbox-primary'][" + selectedAnswerHashMap.get(pregunta).getElementPosition() + "]//child::label";
                wait.until( ExpectedConditions.elementToBeClickable( By.xpath(xpath) ) ).click();
            }
            wait.until( ExpectedConditions.elementToBeClickable( By.xpath("//*[@id='btnPregSig']") ) ).click();
        }
        driver.close();
    }

    public boolean existElement(WebDriver driver) {
        boolean existElement = true;
        try{
            driver.findElement(By.xpath("//div[@class = 'toast toast-warning']"));
        } catch (NoSuchElementException e) {
            existElement = false;
        }
        return existElement;
    }

    private Browser buildConcreteBrowser(String browserType) {
        BrowserConstructor browserConstructor = new BrowserConstructor();
        if( browserType.equals( BrowserType.CHROME ) ) {
            browserConstructor.setBrowserBuilder( new ChromeBrowserBuilder() );
        } else if( browserType.equals( BrowserType.FIREFOX ) ) {
            browserConstructor.setBrowserBuilder( new FirefoxBrowserBuilder() );
        } else if( browserType.equals( BrowserType.SAFARI ) ) {
            browserConstructor.setBrowserBuilder( new SafariBrowserBuilder() );
        }
        browserConstructor.createBrowserInstance();
        System.setProperty( browserConstructor.getBrowser().getDriver(), browserConstructor.getBrowser().getDriverPath() );
        browserConstructor.initializeWebDriver();
        Browser browser = browserConstructor.getBrowser();
        return browser;
    }

}
