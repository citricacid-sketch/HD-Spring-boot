package com.travel.service.impl;

import com.travel.dto.RagProcessResponse;
import com.travel.service.RagService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RagServiceMockImpl implements RagService {

    // Mock travel knowledge base (in a real system, this would be a vector database)
    private static final List<String> TRAVEL_KNOWLEDGE = Arrays.asList(
            "北京故宫需要提前预约门票，建议游玩3-4小时",
            "上海外滩夜景最佳观赏时间为晚上7-9点",
            "西安兵马俑建议请导游讲解，游玩时间2-3小时",
            "成都宽窄巷子有很多特色小吃，适合傍晚游览",
            "杭州西湖春季和秋季是最佳游览季节",
            "桂林山水游船行程大约4小时，建议选择上午出发",
            "三亚海滩度假建议准备防晒霜和游泳装备",
            "云南丽江古城适合慢游，建议安排2天时间",
            "西藏布达拉宫需要提前办理边防证",
            "新疆喀纳斯秋季景色最美，但昼夜温差大"
    );

    // Common travel aspects to ask for refinement
    private static final List<String> REFINEMENT_QUESTIONS = Arrays.asList(
            "请问出行天数是多少？",
            "您的预算是多少？",
            "同行是否有老人或儿童？",
            "您对住宿有什么要求？",
            "您更喜欢自然风光还是人文历史？",
            "您对交通方式有什么偏好？",
            "您是否有特殊的饮食要求？",
            "您希望行程节奏是紧凑还是休闲？"
    );

    // Suggestion templates based on keywords
    private static final Map<String, List<String>> KEYWORD_SUGGESTIONS = Map.of(
            "北京", Arrays.asList("故宫", "天安门", "长城", "颐和园"),
            "上海", Arrays.asList("外滩", "东方明珠", "迪士尼", "南京路"),
            "西安", Arrays.asList("兵马俑", "大雁塔", "回民街", "华清宫"),
            "成都", Arrays.asList("熊猫基地", "宽窄巷子", "锦里", "都江堰"),
            "杭州", Arrays.asList("西湖", "灵隐寺", "宋城", "西溪湿地"),
            "三天", Arrays.asList("紧凑行程", "精选景点", "城市游", "短途旅行"),
            "五天", Arrays.asList("深度游", "多城市", "休闲度假", "主题游"),
            "预算", Arrays.asList("经济型", "舒适型", "豪华型", "定制游")
    );

    @Override
    public RagProcessResponse processPrompt(String prompt) {
        // Simple mock RAG processing
        // In reality, this would involve embedding the prompt, querying a vector database,
        // and using an LLM to generate enhanced prompt and questions

        // Extract keywords from prompt
        Set<String> keywords = extractKeywords(prompt);

        // Generate enhanced prompt by adding context
        String enhancedPrompt = generateEnhancedPrompt(prompt, keywords);

        // Select relevant refinement questions
        List<String> refinedQuestions = selectRefinementQuestions(keywords);

        // Calculate confidence score based on prompt detail
        double confidenceScore = calculateConfidenceScore(prompt, keywords);

        // Find relevant context from knowledge base
        List<String> relevantContext = findRelevantContext(keywords);

        return new RagProcessResponse(enhancedPrompt, refinedQuestions, confidenceScore, relevantContext);
    }

    @Override
    public List<String> getSuggestions(String partialPrompt) {
        if (partialPrompt == null || partialPrompt.trim().isEmpty()) {
            return Collections.emptyList();
        }

        List<String> suggestions = new ArrayList<>();
        String lowerPrompt = partialPrompt.toLowerCase();

        // Check for keyword matches
        for (Map.Entry<String, List<String>> entry : KEYWORD_SUGGESTIONS.entrySet()) {
            if (lowerPrompt.contains(entry.getKey().toLowerCase())) {
                suggestions.addAll(entry.getValue());
            }
        }

        // Add generic suggestions if no keyword matches
        if (suggestions.isEmpty()) {
            suggestions.addAll(Arrays.asList(
                "可以补充出行天数",
                "可以说明预算范围",
                "可以描述旅行偏好",
                "可以指定目的地"
            ));
        }

        // Limit to 5 suggestions
        return suggestions.subList(0, Math.min(suggestions.size(), 5));
    }

    private Set<String> extractKeywords(String prompt) {
        Set<String> keywords = new HashSet<>();
        if (prompt == null) return keywords;

        // Simple keyword extraction (in reality would use NLP)
        String[] words = prompt.toLowerCase().split("\\s+");
        List<String> commonKeywords = Arrays.asList(
            "北京", "上海", "西安", "成都", "杭州", "广州", "深圳", "三亚", "丽江", "西藏",
            "三天", "五天", "一周", "周末", "假期",
            "预算", "经济", "豪华", "省钱",
            "老人", "儿童", "家庭", "情侣", "独自",
            "美食", "购物", "摄影", "徒步", "休闲", "冒险"
        );

        for (String word : words) {
            for (String keyword : commonKeywords) {
                if (word.contains(keyword.toLowerCase())) {
                    keywords.add(keyword);
                }
            }
        }

        return keywords;
    }

    private String generateEnhancedPrompt(String originalPrompt, Set<String> keywords) {
        StringBuilder enhanced = new StringBuilder(originalPrompt);

        if (!keywords.isEmpty()) {
            enhanced.append("\n\n基于您的输入，我识别到以下关键信息：");
            for (String keyword : keywords) {
                enhanced.append("\n- ").append(keyword);
            }
            enhanced.append("\n\n我将为您规划一个包含以下要素的行程：");
            for (String keyword : keywords) {
                enhanced.append("\n- ").append(getTravelAspect(keyword));
            }
        }

        enhanced.append("\n\n请确认以上信息准确，或补充更多细节以便我为您优化行程。");
        return enhanced.toString();
    }

    private String getTravelAspect(String keyword) {
        Map<String, String> aspectMap = Map.of(
            "北京", "北京经典景点游览",
            "上海", "上海现代都市体验",
            "三天", "三天紧凑行程安排",
            "五天", "五天深度旅行体验",
            "预算", "合理预算规划",
            "老人", "适合长者的轻松行程",
            "儿童", "亲子友好活动安排",
            "美食", "当地美食探索",
            "购物", "购物机会安排"
        );
        return aspectMap.getOrDefault(keyword, keyword + "相关安排");
    }

    private List<String> selectRefinementQuestions(Set<String> keywords) {
        List<String> questions = new ArrayList<>();

        // Always include basic questions
        questions.add("请问出行天数是多少？");
        questions.add("您的预算是多少？");

        // Add keyword-specific questions
        if (keywords.contains("老人")) {
            questions.add("同行老人是否有行动不便的情况？");
        }
        if (keywords.contains("儿童")) {
            questions.add("儿童的年龄是多少？");
        }
        if (keywords.contains("预算")) {
            questions.add("预算是否包含交通和住宿？");
        }

        // Add random questions if needed
        Random random = new Random();
        while (questions.size() < 4 && REFINEMENT_QUESTIONS.size() > questions.size()) {
            String question = REFINEMENT_QUESTIONS.get(random.nextInt(REFINEMENT_QUESTIONS.size()));
            if (!questions.contains(question)) {
                questions.add(question);
            }
        }

        return questions.subList(0, Math.min(questions.size(), 4));
    }

    private double calculateConfidenceScore(String prompt, Set<String> keywords) {
        // Simple confidence calculation based on prompt length and keywords
        double score = 0.0;

        // Length factor
        int length = prompt.length();
        if (length > 50) score += 0.3;
        else if (length > 20) score += 0.2;
        else score += 0.1;

        // Keyword factor
        score += keywords.size() * 0.1;

        // Cap at 0.9 (never 1.0 to indicate it's an estimate)
        return Math.min(0.9, score);
    }

    private List<String> findRelevantContext(Set<String> keywords) {
        List<String> context = new ArrayList<>();

        for (String knowledge : TRAVEL_KNOWLEDGE) {
            for (String keyword : keywords) {
                if (knowledge.contains(keyword)) {
                    context.add(knowledge);
                    break;
                }
            }

            if (context.size() >= 3) {
                break;
            }
        }

        return context;
    }
}