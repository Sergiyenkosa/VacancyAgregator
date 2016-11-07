package agregator.model;

import agregator.vo.Vacancy;

import java.util.List;

/**
 * Created by s.sergienko on 13.09.2016.
 */
public interface Strategy {
    List<Vacancy> getVacancies(String searchString);
}