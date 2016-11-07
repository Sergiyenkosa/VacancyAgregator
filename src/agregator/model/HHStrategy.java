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
 * Created by s.sergienko on 13.09.2016.
 */
public class HHStrategy implements Strategy {
    private static final String URL_FORMAT = "http://hh.ua/search/vacancy?text=java+junior+%s&page=%d";
    private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";
    private static final int timeout = 5 * 1000;

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancies = new ArrayList<>();

        for (int i = 0;; i++) {
            try {
                Document document = getDocument(searchString, i);

                String attribute = "data-qa";

                Elements elements = document.getElementsByAttributeValue(attribute, "vacancy-serp__vacancy vacancy-serp__vacancy_premium");
                elements.addAll(document.getElementsByAttributeValue(attribute, "vacancy-serp__vacancy"));
                if (elements.isEmpty()) break;

                for (Element element : elements) {
                    Vacancy vacancy = new Vacancy();

                    vacancy.setTitle(element.getElementsByAttributeValue(attribute, "vacancy-serp__vacancy-title").text());
                    vacancy.setSalary(element.getElementsByAttributeValue(attribute, "vacancy-serp__vacancy-compensation").text());
                    vacancy.setCity(element.getElementsByAttributeValue(attribute, "vacancy-serp__vacancy-address").text());
                    vacancy.setCompanyName(element.getElementsByAttributeValue(attribute, "vacancy-serp__vacancy-employer").text());
                    vacancy.setSiteName("hh.ua");
                    vacancy.setUrl(element.getElementsByAttributeValue(attribute, "vacancy-serp__vacancy-title").attr("href"));

                    vacancies.add(vacancy);
                }
            }
            catch (IOException ignored) {
            }
        }
        return vacancies;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        return Jsoup.connect(String.format(URL_FORMAT, searchString, page)).userAgent(userAgent).timeout(timeout).referrer("").get();
    }
}
