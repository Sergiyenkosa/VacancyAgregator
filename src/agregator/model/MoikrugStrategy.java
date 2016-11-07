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
 * Created by s.sergienko on 23.09.2016.
 */
public class MoikrugStrategy implements Strategy {
    private static final String URL_FORMAT = "https://moikrug.ru/vacancies?city_id=%s&location=%s&page=%d&q=java+junior&utf8=✓";
    private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancies = new ArrayList<>();

        for (int i = 0;; i++) {
            try {
                Document document = getDocument(searchString, i+1);

                String attribute = "class";

                Elements elements = document.getElementsByAttributeValueStarting("id", "job_");
                if (elements.isEmpty()) break;

                for (Element element : elements) {
                    Vacancy vacancy = new Vacancy();
                    vacancy.setTitle(element.getElementsByAttributeValue(attribute, "title").text());
                    vacancy.setSalary(element.getElementsByAttributeValue(attribute, "count").text());
                    vacancy.setCity(element.getElementsByAttributeValue(attribute, "location").text());
                    vacancy.setCompanyName(element.getElementsByAttributeValue(attribute, "company_name").first().getElementsByTag("a").text());
                    vacancy.setSiteName("moikrug.ru");
                    vacancy.setUrl("https://moikrug.ru" + element.getElementsByAttributeValue(attribute, "title").first().getElementsByTag("a").attr("href"));

                    vacancies.add(vacancy);
                }
            }
            catch (IOException ignored) {
            }
        }
        return vacancies;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        return Jsoup.connect(String.format(URL_FORMAT, parseCityID(searchString), searchString, page)).userAgent(userAgent).referrer("").get();
    }

    private String parseCityID (String cityName) {
        switch (cityName.toLowerCase()) {
            case "kiev":
                return "908";
            case "odessa":
                return "912";
            case "harkov":
                return "742";
            case "dnepr":
            case "dnepropetrovsk":
                return "903";
            case "lvov":
                return "711";
            default:
                return "";
        }
    }
}
