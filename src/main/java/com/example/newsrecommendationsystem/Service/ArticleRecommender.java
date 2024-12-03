package com.example.newsrecommendationsystem.Service;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ArticleRecommender {

    private static final String DATABASE_NAME = "CwOOD";
    private static final String INTERACTIONS_COLLECTION = "ArticleInteractions";
    private static final String ARTICLES_COLLECTION = "Articles";

    private MongoClient mongoClient;
    private MongoDatabase database;
    private final ExecutorService executorService;

    public ArticleRecommender() {
        mongoClient = MongoClients.create("mongodb://localhost:27017?maxPoolSize=100\"");
        database = mongoClient.getDatabase(DATABASE_NAME);
        executorService = Executors.newFixedThreadPool(4); // Allow up to 4 concurrent threads
    }

    // Recommend articles for a list of users
    public void recommendArticlesForUsers(List<String> usernames) {
        for (String username : usernames) {
            executorService.submit(() -> {
                try {
                    List<Document> articles = recommendArticles(username);
                    synchronized (System.out) {
                        System.out.println("Recommendations for " + username + ":");
                        articles.forEach(article ->
                                System.out.println(article.getString("title") + " - Category: " + article.getString("category")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // Recommend articles for a single user
    public List<Document> recommendArticles(String username) {
        MongoCollection<Document> interactionsCollection = database.getCollection(INTERACTIONS_COLLECTION);
        MongoCollection<Document> articlesCollection = database.getCollection(ARTICLES_COLLECTION);

        // Fetch user interactions from the database
        List<Document> userInteractions = interactionsCollection.find(new Document("username", username)).into(new ArrayList<>());
        Map<String, Double> categoryScores = calculateCategoryScores(userInteractions);

        // Categorize preferences based on scores
        List<String> highPreferenceCategories = categoryScores.entrySet().stream()
                .filter(entry -> entry.getValue() > 7)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<String> mediumPreferenceCategories = categoryScores.entrySet().stream()
                .filter(entry -> entry.getValue() >= 4 && entry.getValue() <= 7)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<String> lowPreferenceCategories = categoryScores.entrySet().stream()
                .filter(entry -> entry.getValue() < 4)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Fetch articles based on user preferences
        List<Document> recommendedArticles = new ArrayList<>();
        recommendedArticles.addAll(fetchArticlesByCategory(articlesCollection, highPreferenceCategories, 10));
        recommendedArticles.addAll(fetchArticlesByCategory(articlesCollection, mediumPreferenceCategories, 5));
        recommendedArticles.addAll(fetchArticlesByCategory(articlesCollection, lowPreferenceCategories, 2));  // Fetch up to 2 articles for low-preference categories

        return recommendedArticles;
    }

    // Calculate average rating for each category based on user interactions
    private Map<String, Double> calculateCategoryScores(List<Document> interactions) {
        Map<String, List<Integer>> ratingsByCategory = new HashMap<>();
        for (Document interaction : interactions) {
            String category = interaction.getString("category");
            int rating = interaction.getInteger("rating");
            ratingsByCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(rating);
        }

        // Calculate the average rating for each category
        Map<String, Double> categoryScores = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : ratingsByCategory.entrySet()) {
            double average = entry.getValue().stream().mapToInt(Integer::intValue).average().orElse(0.0);
            categoryScores.put(entry.getKey(), average);
        }

        return categoryScores;
    }

    // Fetch articles for specific categories from the database
    private List<Document> fetchArticlesByCategory(MongoCollection<Document> articlesCollection, List<String> categories, int limitPerCategory) {
        List<Document> articles = new ArrayList<>();
        for (String category : categories) {
            List<Document> categoryArticles = articlesCollection.find(new Document("category", category))
                    .limit(limitPerCategory)
                    .into(new ArrayList<>());
            articles.addAll(categoryArticles);
        }
        return articles;
    }

    // Clean up resources
    public void close() {
        executorService.shutdown();
        mongoClient.close();
    }
}
