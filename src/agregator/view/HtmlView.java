package agregator.view;

import agregator.Controller;
import agregator.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by s.sergienko on 20.09.2016.
 */
public class HtmlView implements View {
    private Controller controller;
    private final String filePath = "./src/" + this.getClass().getPackage().getName().replace('.', '/') + "/vacancies.html";

    @Override
    public void update(List<Vacancy> vacancies) {
        updateFile(getUpdatedFileContent(vacancies));
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void userCitySelectEmulationMethod() {
        controller.onCitySelect("Dnepr");
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) {
        String s;

        try {
            Document document = getDocument();

            Element elementVacancy = document.getElementsByClass("template").first();
            Element patternVacancy = elementVacancy.clone();
            patternVacancy.removeClass("template").removeAttr("style");

            document.getElementsByAttributeValue("class", "vacancy").remove();

            for (Vacancy vacancy : vacancies) {
                Element element = patternVacancy.clone();
                element.getElementsByClass("city").first().text(vacancy.getCity());
                element.getElementsByClass("companyName").first().text(vacancy.getCompanyName());
                element.getElementsByClass("salary").first().text(vacancy.getSalary());
                Element link = element.getElementsByTag("a").first();
                link.text(vacancy.getTitle());
                link.attr("href", vacancy.getUrl());

                elementVacancy.before(element.toString());
            }
            s = document.html();
        }
        catch (IOException e) {
            e.printStackTrace();
            s = "Some exception occurred";
        }
        return s;
    }

    private void updateFile(String s) {
        try (FileWriter writer = new FileWriter(filePath)){
            writer.write(s);
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Document getDocument() throws IOException {
        return Jsoup.parse(new File(filePath), "utf-8");
    }
}
