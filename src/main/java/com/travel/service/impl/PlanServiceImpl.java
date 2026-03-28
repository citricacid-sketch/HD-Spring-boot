package com.travel.service.impl;

import com.travel.dto.TravelPlan;
import com.travel.service.PlanService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PlanServiceImpl implements PlanService {

    private static final Random random = new Random();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public TravelPlan generateTravelPlan(String enhancedPrompt) {
        // Parse enhanced prompt to extract information
        Map<String, String> extractedInfo = parseEnhancedPrompt(enhancedPrompt);

        // Generate a travel plan based on extracted information
        TravelPlan plan = new TravelPlan();
        plan.setId("plan-" + System.currentTimeMillis());
        plan.setTitle(generateTitle(extractedInfo));
        plan.setStartDate(generateStartDate());
        plan.setEndDate(generateEndDate(extractedInfo));
        plan.setStatus("ongoing");
        plan.setSource("ai");
        plan.setDays(generateDays(extractedInfo));
        plan.setBudget(generateBudget(extractedInfo));
        plan.setTransportation(generateTransportation(extractedInfo));
        plan.setDescription(generateDescription(extractedInfo));

        return plan;
    }

    private Map<String, String> parseEnhancedPrompt(String enhancedPrompt) {
        Map<String, String> info = new HashMap<>();

        // Simple parsing logic (in reality would use NLP)
        if (enhancedPrompt.toLowerCase().contains("北京")) {
            info.put("destination", "北京");
        } else if (enhancedPrompt.toLowerCase().contains("上海")) {
            info.put("destination", "上海");
        } else if (enhancedPrompt.toLowerCase().contains("西安")) {
            info.put("destination", "西安");
        } else if (enhancedPrompt.toLowerCase().contains("成都")) {
            info.put("destination", "成都");
        } else if (enhancedPrompt.toLowerCase().contains("杭州")) {
            info.put("destination", "杭州");
        } else {
            info.put("destination", "北京"); // default
        }

        if (enhancedPrompt.toLowerCase().contains("三天")) {
            info.put("duration", "3");
        } else if (enhancedPrompt.toLowerCase().contains("五天")) {
            info.put("duration", "5");
        } else if (enhancedPrompt.toLowerCase().contains("一周")) {
            info.put("duration", "7");
        } else {
            info.put("duration", "3"); // default
        }

        if (enhancedPrompt.toLowerCase().contains("经济") || enhancedPrompt.toLowerCase().contains("省钱")) {
            info.put("budgetLevel", "economy");
        } else if (enhancedPrompt.toLowerCase().contains("豪华")) {
            info.put("budgetLevel", "luxury");
        } else {
            info.put("budgetLevel", "comfort");
        }

        if (enhancedPrompt.toLowerCase().contains("老人")) {
            info.put("group", "elderly");
        } else if (enhancedPrompt.toLowerCase().contains("儿童")) {
            info.put("group", "family");
        } else if (enhancedPrompt.toLowerCase().contains("情侣")) {
            info.put("group", "couple");
        } else {
            info.put("group", "general");
        }

        return info;
    }

    private String generateTitle(Map<String, String> info) {
        String destination = info.getOrDefault("destination", "北京");
        String duration = info.getOrDefault("duration", "3");
        String group = info.getOrDefault("group", "general");

        Map<String, String> groupNames = Map.of(
            "elderly", "长者友好",
            "family", "亲子",
            "couple", "情侣",
            "general", "经典"
        );

        return destination + groupNames.getOrDefault(group, "经典") + duration + "日游";
    }

    private String generateStartDate() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.plusDays(7); // Start in 1 week
        return startDate.format(dateFormatter);
    }

    private String generateEndDate(Map<String, String> info) {
        int duration = Integer.parseInt(info.getOrDefault("duration", "3"));
        LocalDate startDate = LocalDate.parse(generateStartDate(), dateFormatter);
        LocalDate endDate = startDate.plusDays(duration - 1);
        return endDate.format(dateFormatter);
    }

    private List<TravelPlan.Day> generateDays(Map<String, String> info) {
        int duration = Integer.parseInt(info.getOrDefault("duration", "3"));
        String destination = info.getOrDefault("destination", "北京");
        List<TravelPlan.Day> days = new ArrayList<>();

        LocalDate startDate = LocalDate.parse(generateStartDate(), dateFormatter);

        Map<String, List<String>> destinationActivities = Map.of(
            "北京", Arrays.asList(
                "故宫博物院参观",
                "天安门广场升旗仪式",
                "八达岭长城一日游",
                "颐和园漫步",
                "王府井购物"
            ),
            "上海", Arrays.asList(
                "外滩夜景欣赏",
                "东方明珠登高",
                "迪士尼乐园游玩",
                "南京路步行街购物",
                "豫园参观"
            ),
            "西安", Arrays.asList(
                "秦始皇兵马俑博物馆",
                "大雁塔文化广场",
                "回民街美食探索",
                "古城墙骑行",
                "华清宫温泉"
            ),
            "成都", Arrays.asList(
                "熊猫基地参观",
                "宽窄巷子漫步",
                "锦里古街体验",
                "都江堰水利工程",
                "火锅美食体验"
            ),
            "杭州", Arrays.asList(
                "西湖环湖游览",
                "灵隐寺祈福",
                "宋城千古情演出",
                "西溪湿地公园",
                "龙井茶庄品茶"
            )
        );

        List<String> activities = destinationActivities.getOrDefault(destination,
            Arrays.asList("城市观光", "文化体验", "美食探索", "休闲购物", "自然风光"));

        for (int i = 0; i < duration; i++) {
            TravelPlan.Day day = new TravelPlan.Day();
            day.setLabel("第" + (i + 1) + "天");
            day.setDate(startDate.plusDays(i).format(dateFormatter));
            day.setSlots(generateDaySlots(i, activities, info));
            days.add(day);
        }

        return days;
    }

    private List<TravelPlan.Slot> generateDaySlots(int dayIndex, List<String> activities, Map<String, String> info) {
        List<TravelPlan.Slot> slots = new ArrayList<>();

        String group = info.getOrDefault("group", "general");
        boolean isElderly = "elderly".equals(group);
        boolean isFamily = "family".equals(group);

        // Morning slots
        slots.add(new TravelPlan.Slot("08:00", "酒店早餐"));
        if (dayIndex == 0) {
            slots.add(new TravelPlan.Slot("09:30", "抵达目的地，酒店入住"));
        }

        // Activity slots (adjusted for group type)
        if (isElderly) {
            slots.add(new TravelPlan.Slot("10:30", activities.get(dayIndex % activities.size()) + "（轻松版）"));
            slots.add(new TravelPlan.Slot("12:00", "午餐（当地特色餐厅）"));
            slots.add(new TravelPlan.Slot("14:00", "午休时间"));
            slots.add(new TravelPlan.Slot("15:30", "自由活动或简单观光"));
        } else if (isFamily) {
            slots.add(new TravelPlan.Slot("10:00", activities.get(dayIndex % activities.size()) + "（亲子版）"));
            slots.add(new TravelPlan.Slot("12:00", "午餐（儿童友好餐厅）"));
            slots.add(new TravelPlan.Slot("14:00", "亲子活动或游乐场"));
            slots.add(new TravelPlan.Slot("16:00", "小吃体验"));
        } else {
            slots.add(new TravelPlan.Slot("09:30", activities.get(dayIndex % activities.size())));
            slots.add(new TravelPlan.Slot("12:00", "午餐（当地美食）"));
            slots.add(new TravelPlan.Slot("13:30", activities.get((dayIndex + 1) % activities.size())));
            slots.add(new TravelPlan.Slot("16:00", "自由探索"));
        }

        // Evening slots
        slots.add(new TravelPlan.Slot("18:30", "晚餐"));
        if (dayIndex < 2) { // First two days have evening activities
            slots.add(new TravelPlan.Slot("20:00", "夜景观赏或文化表演"));
        }

        // Add end times
        for (TravelPlan.Slot slot : slots) {
            if (slot.getTime().equals("08:00")) slot.setEnd("09:00");
            else if (slot.getTime().equals("09:30")) slot.setEnd("11:30");
            else if (slot.getTime().equals("10:00")) slot.setEnd("12:00");
            else if (slot.getTime().equals("10:30")) slot.setEnd("12:00");
            else if (slot.getTime().equals("12:00")) slot.setEnd("13:30");
            else if (slot.getTime().equals("13:30")) slot.setEnd("15:30");
            else if (slot.getTime().equals("14:00")) slot.setEnd("15:30");
            else if (slot.getTime().equals("15:30")) slot.setEnd("17:00");
            else if (slot.getTime().equals("16:00")) slot.setEnd("18:00");
            else if (slot.getTime().equals("18:30")) slot.setEnd("20:00");
            else if (slot.getTime().equals("20:00")) slot.setEnd("22:00");
        }

        return slots;
    }

    private TravelPlan.Budget generateBudget(Map<String, String> info) {
        String budgetLevel = info.getOrDefault("budgetLevel", "comfort");
        int duration = Integer.parseInt(info.getOrDefault("duration", "3"));
        int people = info.getOrDefault("group", "general").equals("family") ? 4 : 2;

        double basePricePerDay;
        TravelPlan.Budget.Breakdown breakdown = new TravelPlan.Budget.Breakdown();

        switch (budgetLevel) {
            case "economy":
                basePricePerDay = 400 * people;
                breakdown.setAccommodation(150 * people);
                breakdown.setTransportation(50 * people);
                breakdown.setFood(100 * people);
                breakdown.setActivities(50 * people);
                breakdown.setShopping(30 * people);
                breakdown.setOther(20 * people);
                break;
            case "luxury":
                basePricePerDay = 1500 * people;
                breakdown.setAccommodation(700 * people);
                breakdown.setTransportation(200 * people);
                breakdown.setFood(300 * people);
                breakdown.setActivities(200 * people);
                breakdown.setShopping(50 * people);
                breakdown.setOther(50 * people);
                break;
            default: // comfort
                basePricePerDay = 800 * people;
                breakdown.setAccommodation(350 * people);
                breakdown.setTransportation(100 * people);
                breakdown.setFood(200 * people);
                breakdown.setActivities(100 * people);
                breakdown.setShopping(30 * people);
                breakdown.setOther(20 * people);
        }

        TravelPlan.Budget budget = new TravelPlan.Budget();
        budget.setTotal(basePricePerDay * duration);
        budget.setCurrency("CNY");
        budget.setBreakdown(breakdown);

        return budget;
    }

    private TravelPlan.Transportation generateTransportation(Map<String, String> info) {
        String destination = info.getOrDefault("destination", "北京");
        String budgetLevel = info.getOrDefault("budgetLevel", "comfort");

        TravelPlan.Transportation transportation = new TravelPlan.Transportation();

        switch (destination) {
            case "北京":
                transportation.setPrimary("地铁 + 出租车");
                transportation.setSecondary("公共交通卡");
                transportation.setNotes("北京地铁网络发达，建议购买交通卡方便出行");
                break;
            case "上海":
                transportation.setPrimary("地铁 + 网约车");
                transportation.setSecondary("共享单车");
                transportation.setNotes("上海地铁覆盖广泛，高峰时段建议避开拥挤线路");
                break;
            case "西安":
                transportation.setPrimary("旅游巴士 + 出租车");
                transportation.setSecondary("步行");
                transportation.setNotes("西安景点相对集中，部分区域适合步行游览");
                break;
            case "成都":
                transportation.setPrimary("地铁 + 景区直通车");
                transportation.setSecondary("出租车");
                transportation.setNotes("成都地铁可到达主要景点，郊区景点建议乘坐景区直通车");
                break;
            case "杭州":
                transportation.setPrimary("公交车 + 共享单车");
                transportation.setSecondary("游船");
                transportation.setNotes("杭州西湖周边建议骑行或步行，湖区可乘坐游船");
                break;
            default:
                transportation.setPrimary("出租车 + 公共交通");
                transportation.setSecondary("步行");
                transportation.setNotes("根据具体行程安排交通方式");
        }

        if ("luxury".equals(budgetLevel)) {
            transportation.setPrimary("包车服务");
            transportation.setNotes("专车接送，行程更自由舒适");
        }

        return transportation;
    }

    private String generateDescription(Map<String, String> info) {
        String destination = info.getOrDefault("destination", "北京");
        String duration = info.getOrDefault("duration", "3");
        String group = info.getOrDefault("group", "general");
        String budgetLevel = info.getOrDefault("budgetLevel", "comfort");

        Map<String, String> destinationDesc = Map.of(
            "北京", "中国首都，拥有丰富的历史文化遗产和现代都市风貌",
            "上海", "国际化大都市，融合东西方文化，充满现代魅力",
            "西安", "古都长安，丝绸之路起点，历史文化底蕴深厚",
            "成都", "天府之国，以美食、熊猫和悠闲生活闻名",
            "杭州", "人间天堂，西湖美景，茶文化发源地"
        );

        Map<String, String> groupDesc = Map.of(
            "elderly", "行程安排轻松舒缓，注重舒适和安全",
            "family", "包含亲子互动项目，适合全家出游",
            "couple", "浪漫氛围营造，适合情侣度假",
            "general", "经典行程安排，适合大多数游客"
        );

        Map<String, String> budgetDesc = Map.of(
            "economy", "经济型预算，性价比高",
            "comfort", "舒适型预算，体验良好",
            "luxury", "豪华型预算，尊享服务"
        );

        return String.format(
            "本次%s日%s之旅，为您量身定制。%s。行程设计%s，预算属于%s，确保您获得满意的旅行体验。",
            duration, destination,
            destinationDesc.getOrDefault(destination, "特色旅游目的地"),
            groupDesc.getOrDefault(group, "经典行程"),
            budgetDesc.getOrDefault(budgetLevel, "舒适型")
        );
    }
}