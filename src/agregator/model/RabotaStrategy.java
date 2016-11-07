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
 * Created by s.sergienko on 28.09.2016.
 */
public class RabotaStrategy implements Strategy {
    private static final String URL_FORMAT = "http://rabota.ua/jobsearch/vacancy_list?%skeyWords=java+junior&pg=%d";
    private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancies = new ArrayList<>();

        for (int i = 0;; i++) {
            try {
                Document document = getDocument(searchString, i+1);
                String attribute = "class";

                Elements elements = document.getElementsByAttributeValueMatching(attribute, "^(v$|v )");
                if (elements.isEmpty()) break;

                for (Element element : elements) {
                    Vacancy vacancy = new Vacancy();
                    String[] vacancyInfo = (element.getElementsByAttributeValue(attribute, "s").text().split("•"));

                    vacancy.setTitle(element.getElementsByAttributeValue(attribute, "t").text());
                    vacancy.setSalary(element.getElementsByTag("b").text());
                    vacancy.setCity(vacancyInfo[1]);
                    vacancy.setCompanyName(element.getElementsByAttributeValue(attribute, "rua-p-c-default").text());
                    vacancy.setSiteName("rabota.ua");
                    vacancy.setUrl("http://rabota.ua/" + element.getElementsByTag("a").attr("href"));

                    vacancies.add(vacancy);
                }
            }
            catch (IOException ignored) {
            }
        }
        return vacancies;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        return Jsoup.connect(String.format(URL_FORMAT, parseCityNameUA(searchString), page)).userAgent(userAgent). referrer("").get();
    }

    private String parseCityNameUA (String cityNameRU) {
        switch (cityNameRU.toLowerCase()) {
            case "kiev":
                return "regionId=1&";
            case "odessa":
                return "regionId=3&";
            case "harkov":
                return "regionId=21&";
            case "dnepr":
                return "regionId=4&";
            case "lvov":
                return "regionId=2&";
            default:
                return "";
        }
    }
}
