package com.undot.triviabackend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.datastore.*;


import javax.inject.Named;
import java.security.PrivateKey;
import java.util.*;

/**
 * Created by 0503337710 on 26/03/2016.
 */

@Api(name = "triviaApi",
        version = "v1"
        )

public class EndPoint {
OAuthService oAuthService = new OAuthService();
    @ApiMethod(name = "register"
            ,path = "register",
            httpMethod = ApiMethod.HttpMethod.GET)
    public User register(@Named("token") String token) {
        RegisterResponse registerResponse = new RegisterResponse();
    User user = oAuthService.getGoogleUser(token);
        if(user.isValidToken()) {
            try {
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                Transaction txn = datastore.beginTransaction();


                try {
                    Key linkKey = KeyFactory.createKey("Users", user.getGoogleId());

                    Entity link = datastore.get(linkKey);
                    registerResponse.setResponse("Success");
                    User user1 = new User();
                    user1.setValidToken(true);
                    return user1;





                } catch (EntityNotFoundException e) {
                    AsymmetricEncryption asy = new AsymmetricEncryption();
                    try {
                        byte[] savePublic = AsymmetricEncryption.savePublicKeyB(asy.getPublicKey());
                        Debug.rsa("Pub:" + savePublic);
                        byte[] savePrivate = AsymmetricEncryption.savePrivateKeyB(asy.getPrivateKey());
                        Debug.rsa("Prib:" + savePrivate);
                    }catch (java.lang.Exception es){
                        Debug.rsa("Error in Register and private public keys byte generation");
                    }
                    String savePublic="";
                    String savePrivate="";
                    try {
                        savePublic = AsymmetricEncryption.savePublicKey1(asy.getPublicKey());
                        Debug.rsa("Pub:"+savePublic);
                        savePrivate = AsymmetricEncryption.savePrivateKey1(asy.getPrivateKey());
                        Debug.rsa("Prib:"+savePrivate);
                    }catch (java.lang.Exception es){
                        Debug.rsa("Error in Register and private public keys string  generation");
                    }

                    user.setsPublic(savePublic);
                    Entity rate = new Entity("Users", user.getGoogleId());
                    rate.setProperty("name", user.getName());
                    rate.setProperty("email", user.getEmail());
                    rate.setProperty("sPrivate",savePrivate);
                    rate.setProperty("sPublic",savePublic);

                    datastore.put(rate);
                }
                txn.commit();
                registerResponse.setResponse("Success");
                return user;
            } catch (Exception e) {
                registerResponse.setResponse("Error");
                return user;

            }
        }
        else
        {
            registerResponse.setResponse("Invalid");
            return user;
        }



    }
    @ApiMethod(name = "setScore"
            ,path = "setScore",
            httpMethod = ApiMethod.HttpMethod.GET)
    public RegisterResponse setScore(Score score) {
        User user = oAuthService.getGoogleUser(score.getUserToken());
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastore.beginTransaction();
        if(user.isValidToken())
        {
            try {
                Key linkKey = KeyFactory.createKey("Users", user.getGoogleId());
                Entity link = datastore.get(linkKey);
                String key = (String)link.getProperty("sPrivate");
                AsymmetricEncryption asy = new AsymmetricEncryption();
                try {
                    PrivateKey pkey = asy.loadPrivateKeyS(key);

                    Long highscore = Long.decode(asy.decrypt(score.getScore(),pkey).trim());
                    java.util.Date date= new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int month = cal.get(Calendar.MONTH);
                    Entity rate = new Entity("Leaderboard_"+score.getCategory(), user.getGoogleId());
                    rate.setProperty("name", user.getName());
                    rate.setProperty("score", highscore);
                    rate.setProperty("time", theMonth(month));
                    datastore.put(rate);
                    txn.commit();
                    RegisterResponse registerResponse = new RegisterResponse();
                    registerResponse.setResponse("Success");
                    return registerResponse;

                }
                catch (Exception e)
                {
                    RegisterResponse registerResponse = new RegisterResponse();
                    registerResponse.setResponse("Encryption Error");
                    return registerResponse;
                }




            }
            catch (EntityNotFoundException e)
            {
                RegisterResponse registerResponse = new RegisterResponse();
                registerResponse.setResponse("User Not Found");
                return registerResponse;
            }
        }
        else
        {
            RegisterResponse registerResponse = new RegisterResponse();
            registerResponse.setResponse("Invalid Token");
            return registerResponse;
        }


    }
    @ApiMethod(name = "getCategories"
            ,path = "getCategories",
            httpMethod = ApiMethod.HttpMethod.GET)
    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query questions = new Query("Categories");
        List<Entity> entities = datastore.prepare(questions).asList(FetchOptions.Builder.withDefaults());
        for(Entity e:entities){
            categories.add((e.getKey().getName()));
        }
        return categories;

    }
    @ApiMethod(name = "addCategory"
            ,path = "addCategory",
            httpMethod = ApiMethod.HttpMethod.GET)
    public RegisterResponse addCategory(@Named("name") String name) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastore.beginTransaction();
        try {
            Entity rate = new Entity("Categories",name);
            datastore.put(rate);
            txn.commit();
            RegisterResponse registerResponse = new RegisterResponse();
            registerResponse.setResponse("Success");
            return registerResponse;
        }catch (Exception e)
        {
            RegisterResponse registerResponse = new RegisterResponse();
            registerResponse.setResponse("Error");
            return registerResponse;
        }



    }
    @ApiMethod(name = "getQuestions"
    ,path = "getquestions",
    httpMethod = ApiMethod.HttpMethod.GET)
    public QuestionList getQuestions() {

            List<Question> questionsList = new ArrayList<>();
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Query questions = new Query("Question");
            Query.Filter rank1Filter =
                    new Query.FilterPredicate("rank",
                            Query.FilterOperator.EQUAL,
                            1);
            Query.Filter rank2Filter =
                    new Query.FilterPredicate("rank",
                            Query.FilterOperator.EQUAL,
                            2);
            Query.Filter rank3Filter =
                    new Query.FilterPredicate("rank",
                            Query.FilterOperator.EQUAL,
                            3);
            List<Entity> rank1 = datastore.prepare(questions.setFilter(rank1Filter)).asList(FetchOptions.Builder.withDefaults());
            List<Entity> rank2 = datastore.prepare(questions.setFilter(rank2Filter)).asList(FetchOptions.Builder.withDefaults());
            List<Entity> rank3 = datastore.prepare(questions.setFilter(rank3Filter)).asList(FetchOptions.Builder.withDefaults());
            System.out.println("rank1 size: "+rank1.size());
            System.out.println("rank2 size: "+rank2.size());
            System.out.println("rank3 size: "+rank3.size());

        Integer[] rank1_numbers = sampleRandomNumbersWithoutRepetition(0,rank1.size(),3);
        Integer[] rank2_numbers = sampleRandomNumbersWithoutRepetition(0,rank2.size(),3);
        Integer[] rank3_numbers = sampleRandomNumbersWithoutRepetition(0,rank3.size(),4);
            for(Integer i : rank1_numbers){
                Entity e = rank1.get(i);
                Question q = new Question();
                q.setmQuestion((String)e.getProperty("question"));
                q.setmOptionToAns_1((String)e.getProperty("optional_answer1"));
                q.setmOptionToAns_2((String)e.getProperty("optional_answer2"));
                q.setmOptionToAns_3((String)e.getProperty("optional_answer3"));
                q.setmOptionToAns_4((String)e.getProperty("optional_answer4"));
                q.settCorrectAns((String)e.getProperty("current_answer"));
                q.setRank(1);
                q.settQuestionKind((Boolean) e.getProperty("qkind"));
                questionsList.add(q);

            }
            for(Integer i : rank2_numbers){
                Entity e = rank2.get(i);
                Question q = new Question();
                q.setmQuestion((String)e.getProperty("question"));
                q.setmOptionToAns_1((String)e.getProperty("optional_answer1"));
                q.setmOptionToAns_2((String)e.getProperty("optional_answer2"));
                q.setmOptionToAns_3((String)e.getProperty("optional_answer3"));
                q.setmOptionToAns_4((String)e.getProperty("optional_answer4"));
                q.settCorrectAns((String)e.getProperty("current_answer"));
                q.setRank(2);
                q.settQuestionKind((Boolean) e.getProperty("qkind"));
                questionsList.add(q);

            }

            for(Integer i : rank3_numbers){
                Entity e = rank3.get(i);
                Question q = new Question();
                q.setmQuestion((String)e.getProperty("question"));
                q.setmOptionToAns_1((String)e.getProperty("optional_answer1"));
                q.setmOptionToAns_2((String)e.getProperty("optional_answer2"));
                q.setmOptionToAns_3((String)e.getProperty("optional_answer3"));
                q.setmOptionToAns_4((String)e.getProperty("optional_answer4"));
                q.settCorrectAns((String)e.getProperty("current_answer"));
                q.setRank(3);
                q.settQuestionKind((Boolean) e.getProperty("qkind"));
                questionsList.add(q);

            }
            QuestionList questionList = new QuestionList();
            questionList.setQuestionList(questionsList);

            return questionList;





    }
    @ApiMethod(name = "test"
            ,path = "test",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Question test(@Named("name") String name , @Named("question") String question, @Named("answer1") String answer1, @Named("answer2") String answer2, @Named("answer3") String answer3, @Named("answer4") String answer4, @Named("answer") String answer, @Named("kind") Boolean kind, @Named("rank") Integer rank)
    {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastore.beginTransaction();

        Question q = new Question();
        q.setmQuestion(question);
        q.setmOptionToAns_1(answer1);
        q.setmOptionToAns_2(answer2);
        q.setmOptionToAns_3(answer3);
        q.setmOptionToAns_4(answer4);
        q.settCorrectAns(answer);
        q.settQuestionKind(kind);
        q.setRank(rank);

        Entity rate = new Entity("Question",name);
        rate.setProperty("question",q.getmQuestion());
        rate.setProperty("current_answer",q.gettCorrectAns());
        rate.setProperty("optional_answer1",q.getmOptionToAns_1());
        rate.setProperty("optional_answer2",q.getmOptionToAns_2());
        rate.setProperty("optional_answer3",q.getmOptionToAns_3());
        rate.setProperty("optional_answer4",q.getmOptionToAns_4());
        rate.setProperty("qkind",q.gettQuestionKind());
        rate.setProperty("rank",q.getRank());






        datastore.put(rate);

    txn.commit();
        return q;
    }

    static Integer[] sampleRandomNumbersWithoutRepetition(Integer start, Integer end, Integer count) {
        Random rng = new Random();

        Integer[] result = new Integer[count];
        Integer cur = 0;
        Integer remaining = end - start;
        for (Integer i = start; i < end && count > 0; i++) {
            Double probability = rng.nextDouble();
            if (probability < (Double.valueOf(count) / Double.valueOf(remaining))) {
                count--;
                result[cur++] = i;
            }
            remaining--;
        }
        return result;
    }
    static String theMonth(Integer month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }
}
