package agregator.model;

import agregator.vo.Vacancy;

import java.util.List;

/**
 * Created by s.sergienko on 13.09.2016.
 */
public class Provider {
    private Strategy strategy;

    public Provider() {
    }

    public Provider(Strategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public List<Vacancy> getJavaVacancies(String searchString)
    {
        return strategy.getVacancies(searchString);
    }
}
