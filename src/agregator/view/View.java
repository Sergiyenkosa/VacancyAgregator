package agregator.view;

import agregator.Controller;
import agregator.vo.Vacancy;

import java.util.List;

/**
 * Created by s.sergienko on 20.09.2016.
 */
public interface View {
    void update(List<Vacancy> vacancies);
    void setController(Controller controller);
}
