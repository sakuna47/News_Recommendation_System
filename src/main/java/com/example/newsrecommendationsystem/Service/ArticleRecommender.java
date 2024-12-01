package com.example.newsrecommendationsystem.Service;

import com.mongodb.client.*;
import org.bson.Document;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ArticleRecommender {

    private static final String DATABASE_NAME = "CwOOD";
    private static final String INTERACTIONS_COLLECTION = "ArticleInteractions";
    private static final String ARTICLES_COLLECTION = "Articles";

    private MongoClient mongoClient;
    private MongoDatabase database;
    private ExecutorService executorService;

    public ArticleRecommender() {
        mongoClient = MongoClients.create("mongodb://localhost:27017"); // Update the connection string if necessary
        database = mongoClient.getDatabase(DATABASE_NAME);
        executorService = Executors.newFixedThreadPool(10); // Using a thread pool with 10 threads
    }

    public List<Document> recommendArticles(String username) throws InterruptedException, ExecutionException {
        MongoCollection<Document> interactionsCollection = database.getCollection(INTERACTIONS_COLLECTION);
        MongoCollection<Document> articlesCollection = database.getCollection(ARTICLES_COLLECTION);

        // Fetch user interactions
        List<Document> userInteractions = interactionsCollection.find(new Document("username", username)).into(new ArrayList<>());

        // Calculate category preferences
        Map<String, Double> categoryScores = calculateCategoryScores(userInteractions);

        // Categorize preferences
        List<String> highPreferenceCategories = new ArrayList<>();
        List<String> mediumPreferenceCategories = new ArrayList<>();
        List<String> lowPreferenceCategories = new ArrayList<>();

        for (Map.Entry<String, Double> entry : categoryScores.entrySet()) {
            if (entry.getValue() > 7) {
                highPreferenceCategories.add(entry.getKey());
            } else if (entry.getValue() >= 4 && entry.getValue() <= 7) {
                mediumPreferenceCategories.add(entry.getKey());
            } else {
                lowPreferenceCategories.add(entry.getKey());
            }
        }

        // Use ExecutorService to fetch articles asynchronously
        List<Future<List<Document>>> futures = new ArrayList<>();
        futures.add(executorService.submit(() -> fetchArticlesByCategory(articlesCollection, highPreferenceCategories, 10)));
        futures.add(executorService.submit(() -> fetchArticlesByCategory(articlesCollection, mediumPreferenceCategories, 5)));

        // Wait for all tasks to complete and combine the results
        List<Document> recommendedArticles = new ArrayList<>();
        for (Future<List<Document>> future : futures) {
            recommendedArticles.addAll(future.get());
        }

        return recommendedArticles;
    }

    private Map<String, Double> calculateCategoryScores(List<Document> interactions) {
        Map<String, List<Integer>> ratingsByCategory = new HashMap<>();

        for (Document interaction : interactions) {
            String category = interaction.getString("category");
            int rating = interaction.getInteger("rating");

            ratingsByCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(rating);
        }

        Map<String, Double> categoryScores = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : ratingsByCategory.entrySet()) {
            double average = entry.getValue().stream().mapToInt(Integer::intValue).average().orElse(0.0);
            categoryScores.put(entry.getKey(), average);
        }

        return categoryScores;
    }

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

    public void close() {
        mongoClient.close();
        executorService.shutdown(); // Shutdown the executor service
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Initialize the session manager and set a test user (for demonstration)
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.setUsername("sakuna@gmail.com");  // This simulates a login

        ArticleRecommender recommender = new ArticleRecommender();
        try {
            // Get the currently logged-in username from the SessionManager
            String username = sessionManager.getUsername();
            if (username != null) {
                List<Document> recommendedArticles = recommender.recommendArticles(username);

                System.out.println("Recommended Articles:");
                for (Document article : recommendedArticles) {
                    System.out.println(article.getString("title") + " - Category: " + article.getString("category"));
                }
            } else {
                System.out.println("No user is currently logged in.");
            }
        } finally {
            recommender.close();
            // Clear the session to simulate logout
            sessionManager.clearSession();
        }
    }
}
