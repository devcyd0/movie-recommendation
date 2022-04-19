package nearsoft.academy.bigdata.recommendation;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class MovieRecommender {
    private final HashMap<String, Integer> users = new HashMap<String, Integer>();
    private final HashMap<String, Integer> products = new HashMap<String, Integer>();
    private final HashMap<Integer, String> inverseProducts = new HashMap<Integer, String>();

    private int totalReviews = 0, totalUsers = 0, totalProducts = 0;
    private int productNum, userNum;

    public MovieRecommender(String path) throws IOException, TasteException{
            //Read and convert data to csv
            File rawData        = new File(path);
            BufferedReader br   = new BufferedReader(new FileReader(rawData));
            File csvData        = new File("csvData.csv");
            FileWriter fw       = new FileWriter(csvData);
            BufferedWriter bw   = new BufferedWriter(fw);

            String userId = "", productId = "", scoreReview;
            String line;

            while ((line = br.readLine()) != null) {
                if(line.startsWith("product/productId:")) {
                    productId = splitLine(line);
                    if (products.containsKey(productId) == false) {
                        totalProducts++;
                        products.put(productId,totalProducts);
                        inverseProducts.put(totalProducts, productId);
                        productNum = totalProducts;
                    } else {
                        productNum = products.get(productId);
                    }
                }
                else if (line.startsWith("review/userId:")) {
                    userId = splitLine(line);
                    if(users.containsKey(userId) == false) {
                        totalUsers++;
                        users.put(userId,totalUsers);
                        userNum = totalUsers;
                    } else {
                        userNum = users.get(userId);
                    }
                }
                else if (line.startsWith("review/score:")) {
                    scoreReview = splitLine(line);
                    bw.write(userNum + "," + productNum + "," + scoreReview + "\n");
                    totalReviews++;
                }
            }
            br.close();
            bw.close();
    }

    public List<String> getRecommendationsForUser(String UserId) throws IOException, TasteException {
        //Recommendation process
        DataModel model = new FileDataModel(new File("csvData.csv"));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

        List <RecommendedItem> recommendations = recommender.recommend(users.get(UserId),3);
        List <String> movieRecommendations = new ArrayList <String>();

        for (RecommendedItem recommendation : recommendations) {
            movieRecommendations.add(inverseProducts.get((int)recommendation.getItemID()));
        }

        return movieRecommendations;
    }

    private String splitLine(String line){
        return line.split(" ")[1];
    }

    public int getTotalReviews(){
        return totalReviews;
    }
    public int getTotalProducts(){
        return totalProducts;
    }
    public int getTotalUsers(){
        return totalUsers;
    }
}