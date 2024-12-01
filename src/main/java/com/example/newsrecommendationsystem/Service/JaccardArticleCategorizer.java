package com.example.newsrecommendationsystem.Service;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.*;

public class JaccardArticleCategorizer {

    private final Map<String, String> categoryExamples;

    public JaccardArticleCategorizer() {
        categoryExamples = new HashMap<>();
        categoryExamples.put("Local News", "community, city, neighborhood, local government, events");
        categoryExamples.put("Business", "finance, stocks, investment, economy, entrepreneurship");
        categoryExamples.put("Sports", "football, cricket, Olympics, team, tournament");
        categoryExamples.put("Health", "fitness, wellness, healthcare, medical, disease");
        categoryExamples.put("Politics", "government, election, policy, parliament, president");
        categoryExamples.put("Weather", "rain, heatwave, cyclone, flood, forecast");
        categoryExamples.put("Entertainment", "movies, music, celebrities, festival, series");
        categoryExamples.put("World News", "global, international, war, diplomacy, conflict");
    }

    public List<Document> getArticlesByCategory(String category) {
        List<Document> categorizedArticles = new ArrayList<>();
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("CwOOD");
            MongoCollection<Document> articlesCollection = database.getCollection("Articles");

            categorizedArticles = articlesCollection.find(new Document("category", category)).into(new ArrayList<>());
        }
        return categorizedArticles;
    }

    private Set<String> tokenize(String text) {
        return new HashSet<>(Arrays.asList(text.toLowerCase().split("\\W+")));
    }

    private double jaccardSimilarity(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        return (double) intersection.size() / union.size();
    }

    public String categorize(String content) {
        Set<String> articleTokens = tokenize(content);
        String bestCategory = "Uncategorized";
        double maxSimilarity = 0.0;

        for (Map.Entry<String, String> entry : categoryExamples.entrySet()) {
            Set<String> categoryTokens = tokenize(entry.getValue());
            double similarity = jaccardSimilarity(articleTokens, categoryTokens);

            if (similarity > maxSimilarity) {
                maxSimilarity = similarity;
                bestCategory = entry.getKey();
            }
        }

        return bestCategory;
    }
}
