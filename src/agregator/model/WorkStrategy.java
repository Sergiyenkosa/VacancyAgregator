package agregator.model;

import agregator.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by s.sergienko on 27.09.2016.
 */
public class WorkStrategy implements Strategy {
    private static final String URL_FORMAT = "https://www.work.ua/jobs%s-java+junior/?page=%d";
    private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancies = new ArrayList<>();

        for (int i = 0;; i++) {
            try {
                Document document = getDocument(searchString, i+1);

                String attribute = "class";

                Elements elements = document.getElementsByAttributeValueStarting(attribute, "card card-hover card-visited job-link");
                if (elements.isEmpty()) break;

                for (Element element : elements) {
                    Vacancy vacancy = new Vacancy();
                    String[] vacancyInfo = (element.getElementsByTag("div").last().getElementsByTag("span").text().split("·"));

                    vacancy.setTitle(element.getElementsByTag("a").text());
                    vacancy.setSalary(element.getElementsByAttribute("data-content").text());
                    vacancy.setCity(vacancyInfo[1].replaceFirst(" ", ""));
                    vacancy.setCompanyName(vacancyInfo[0]);
                    vacancy.setSiteName("work.ua");
                    vacancy.setUrl("https://www.work.ua" + element.getElementsByTag("h2").first().getElementsByTag("a").attr("href"));

                    vacancies.add(vacancy);
                }
            }
            catch (IOException ignored) {
            }
        }
        return vacancies;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        return Jsoup.connect(String.format(URL_FORMAT, parseCityNameUA(searchString), page)).userAgent(userAgent).referrer("").get();
    }

    private String parseCityNameUA (String cityNameRU) {
        switch (cityNameRU.toLowerCase()) {
            case "kiev":
                return "-kyiv";
            case "odessa":
                return "-odesa";
            case "harkov":
                return "-kharkiv";
            case "dnepr":
                return "-dnipro";
            case "lvov":
                return "-lviv";
            default:
                return "";
        }
    }
}
