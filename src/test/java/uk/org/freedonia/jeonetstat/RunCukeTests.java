package uk.org.freedonia.jeonetstat;
import org.junit.runner.RunWith;
import cucumber.api.junit.Cucumber;
import cucumber.api.CucumberOptions;




@RunWith(Cucumber.class)
@CucumberOptions( monochrome = true,plugin = {"html:target/cucumber-html-report", "json:target/cucumber-json-report.json" })
public class RunCukeTests {
}