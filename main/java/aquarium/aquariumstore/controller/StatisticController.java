package aquarium.aquariumstore.controller;

import aquarium.aquariumstore.repository.StatisticRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


// Клас StatisticController є контролером в Spring MVC,
// який відповідає за обробку запитів до сторінки статистики
// та взаємодіє з репозиторієм StatisticRepo для отримання даних.

@Controller
public class StatisticController {

    // Репозиторії для отримання даних
    @Autowired
    StatisticRepo statisticRepo;

    // Відображення сторінки зі статистикою
    @GetMapping("/statistics")
    public String statistics(Model model) {

        double thisMonthProfit = statisticRepo.getThisMonthProfit().getFirst().getValue();
        model.addAttribute("thisMonthProfit", thisMonthProfit);

        int thisMonthOrders = (int) Math.round(statisticRepo.getThisMonthOrders().getFirst().getValue());
        model.addAttribute("thisMonthOrders", thisMonthOrders);

        int thisMonthProducts =  (int) Math.round(statisticRepo.getThisMonthProducts().getFirst().getValue());
        model.addAttribute("thisMonthProducts", thisMonthProducts);

        List<StatisticRepo.ChartData> getTopMonthsProfit = statisticRepo.getTopMonthsProfit();
        model.addAttribute("getTopMonthsProfit", getTopMonthsProfit);

        List<StatisticRepo.ChartData> getTopMonthsOrders = statisticRepo.getTopMonthsOrders();
        model.addAttribute("getTopMonthsOrders", getTopMonthsOrders);

        List<StatisticRepo.ChartDataInt> getTopProducts = statisticRepo.getTopProducts();
        model.addAttribute("getTopProducts", getTopProducts);

        List<StatisticRepo.ChartDataInt> getTopCity= statisticRepo.getTopCity();
        model.addAttribute("getTopCity", getTopCity);

        List<StatisticRepo.ChartDataInt> getTopType = statisticRepo.getTopType();
        model.addAttribute("getTopType", getTopType);

        return "statistics";
    }
}
