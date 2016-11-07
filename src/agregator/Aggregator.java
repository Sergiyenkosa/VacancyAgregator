package agregator;

import agregator.model.*;
import agregator.view.HtmlView;

/**
 * Created by s.sergienko on 13.09.2016.
 */
public class Aggregator {
    public static void main(String[] args) {
        HtmlView view = new HtmlView();
        view.setController(new Controller(new Model(view, new Provider(new WorkStrategy()),
                new Provider(new RabotaStrategy()), new Provider(new HHStrategy()), new Provider(new MoikrugStrategy()))));
        view.userCitySelectEmulationMethod();
    }
}