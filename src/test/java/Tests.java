import com.codeborne.selenide.*;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static com.codeborne.selenide.Browsers.CHROME;
import static com.codeborne.selenide.Selenide.*;

public class Tests

{
    @BeforeEach
    void setup(){
        Configuration.browser = CHROME;
        open("https://the-internet.herokuapp.com");
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false));
    }

    @ParameterizedTest
    @ValueSource(strings={"forward", "back"})
    void testCheckBoxes(String order) throws InterruptedException {
        $x("//a[@href='/checkboxes']").click();
        SelenideElement input1 = $x("//input[1]");
        SelenideElement input2 = $x("//input[2]");
        if (order == "back") {
            input1 = $x("//input[2]");
            input2 = $x("//input[1]");
        }
        input1.click();
        input1.should(Condition.checked);
        input2.click();
        input2.shouldNot(Condition.checked);
    }

    @Test
    void testDropdown(){
        $x("//a[@href='/dropdown']").click();
        $x("//select").click();
        $x("//option[@value='']").shouldHave(Condition.attribute("selected"));
        SelenideElement option1 = $x("//option[@value='1']");
        option1.click();
        option1.shouldHave(Condition.attribute("selected"));
        SelenideElement option2 = $x("//option[@value='2']");
        option2.click();
        option2.shouldHave(Condition.attribute("selected"));
    }

    @DisplayName("Disappearing elements test")
    @RepeatedTest(value = 10, name = "{displayName} - repetition {currentRepetition} of {totalRepetitions}")
    void testDisapperaingEls(){
        $x("//a[@href='/disappearing_elements']").click();
        ElementsCollection el = $$x("//li");
        el.shouldHave(CollectionCondition.size(5));
    }

    void cBcInput(String val, String inpVal){
        $x("//input").clear();
        for(int i = 0; i < val.length(); i++){
            $x("//input").sendKeys(String.valueOf(val.charAt(i)));
        }
        $x("//input").should(Condition.value(inpVal), Duration.ofSeconds(1));
    }

    @TestFactory
    List<DynamicTest> testInputs(){
        List<DynamicTest> dTests = new ArrayList<>();
        List<String> vals = List.of("2354", "-765", "842l5", " 12", "421 ", "asdf", " ", "123З", "?", "фыва");
        List<String> inpVals = List.of("2354", "-765", "8425", "12", "421", "", "", "123", "", "");
        $x("//a[@href='/inputs']").click();
        dTests.add(DynamicTest.dynamicTest(
                "Try1",
                () -> {
                    cBcInput(vals.get(0), inpVals.get(0));
                }
        ));
        dTests.add(DynamicTest.dynamicTest(
                "Try2",
                () -> {
                    cBcInput(vals.get(1), inpVals.get(1));
                }
        ));
        dTests.add(DynamicTest.dynamicTest(
                "Try3",
                () -> {
                    cBcInput(vals.get(2), inpVals.get(2));
                }
        ));
        dTests.add(DynamicTest.dynamicTest(
                "Try4",
                () -> {
                    cBcInput(vals.get(3), inpVals.get(3));
                }
        ));
        dTests.add(DynamicTest.dynamicTest(
                "Try5",
                () -> {
                    cBcInput(vals.get(4), inpVals.get(4));
                }
        ));
        dTests.add(DynamicTest.dynamicTest(
                "Try6",
                () -> {
                    cBcInput(vals.get(5), inpVals.get(5));
                }
        ));
        dTests.add(DynamicTest.dynamicTest(
                "Try7",
                () -> {
                    cBcInput(vals.get(6), inpVals.get(6));
                }
        ));
        dTests.add(DynamicTest.dynamicTest(
                "Try8",
                () -> {
                    cBcInput(vals.get(7), inpVals.get(7));
                }
        ));
        dTests.add(DynamicTest.dynamicTest(
                "Try9",
                () -> {
                    cBcInput(vals.get(8), inpVals.get(8));
                }
        ));
        dTests.add(DynamicTest.dynamicTest(
                "Try10",
                () -> {
                    cBcInput(vals.get(9), inpVals.get(9));
                }
        ));
        return dTests;
    }

    @ParameterizedTest()
    @ValueSource(ints = {2, 3, 1})
    public void testHovers(int order) {
        $x("//a[@href='/hovers']").click();
        $x("//div[@class=\"figure\"][" + order + "]").hover();
        $x("//div[@class=\"figure\"][" + order + "]").should(Condition.text("name: user" + order + " View profile"));
    }

    @RepeatedTest(10)
    public void testNotificationMsg() throws InterruptedException {
        $x("//a[@href='/notification_message']").click();
        $x("//p/a").click();
        $x("//div[@id=\"flash\"]").should(Condition.text("Action successful"), Duration.ofSeconds(1));
    }

    void addRemoveEls(int add, int remove) {
        int counter = 0;
        ElementsCollection items = $$x("//div[@id='elements']/button");
        for (int i = 0; i < add; i++) {
            $x("//div[@class='example']/button").click();
            counter++;
            items.should(CollectionCondition.size(counter));
        }
        for (int i = 0; i < remove; i++) {
            items.should(CollectionCondition.sizeGreaterThan(0));
            int delNum = new Random().nextInt(add - i) + 1;
            $x("//div[@id='elements']/button[" + delNum + "]").click();
            counter--;
            items.should(CollectionCondition.size(counter));
        }
    }

    @TestFactory
    List<DynamicTest> testAREls(){
        List<DynamicTest> dTests = new ArrayList<>();
        dTests.add(DynamicTest.dynamicTest( "2:1",
                ()-> {
                    $x("//a[@href='/add_remove_elements/']").click();
                    addRemoveEls(2, 1);
                }
        ));
        dTests.add(DynamicTest.dynamicTest( "5:2",
                ()-> {
                    refresh();
                    addRemoveEls(5, 2);
                }
        ));
        dTests.add(DynamicTest.dynamicTest( "1:3",
                ()-> {
                    refresh();
                    addRemoveEls(1, 3);
                }
        ));
        return dTests;
    }

    void statusCodes(String statusCode){
        $x("//a[@href='/status_codes']").click();
        $x("//li/a[text()=" + statusCode +"]").click();
        $x("//p").should(Condition.text(statusCode));
    }

    @Test
    public void testStatusCodes200(){
        statusCodes("200");
    }

    @Test
    public void testStatusCodes301(){
        statusCodes("301");
    }

    @Test
    public void testStatusCodes404(){
        statusCodes("404");
    }

    @Test
    public void testStatusCodes500(){
        statusCodes("500");
    }

    @AfterEach
    void quit(){
        closeWebDriver();
    }
}
