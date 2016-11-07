package agregator;

import agregator.model.Model;

/**
 * Created by s.sergienko on 13.09.2016.
 */
public class Controller {
    private Model model;

    public Controller(Model model)
    {
        this.model = model;
        if (model == null) {
            throw new IllegalArgumentException();
        }
    }

    public void onCitySelect(String cityName) {
        model.selectCity(cityName);
    }
}
