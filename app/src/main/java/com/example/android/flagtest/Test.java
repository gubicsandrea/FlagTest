package com.example.android.flagtest;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import android.content.Context;

import static java.security.AccessController.getContext;
/**
 * Created by Hadiné on 2017.03.19..
 */


/**
 * The Question class builder abstraction
 */
interface QuestionBuilder {
    /**
     * Sets the image field of a Question object
     *
     * @param image the image name for the question
     * @return
     */
    QuestionBuilder addImage(final String image);

    /**
     * Sets the options field of a Question object
     *
     * @param options the 4 possible answers for the question
     * @return
     */
    QuestionBuilder addOptions(final List<String> options);

    /**
     * Sets the answer field of a Question object
     *
     * @param answer the right answer for thw question
     * @return
     */
    QuestionBuilder addAnswer(final String answer);

    /**
     * Checks whether all the fields are set for a question and return the object or logs the error.
     *
     * @return
     */
    Question build() throws Exception;
}

/**
 * Stores one question.
 */
class Question{
    private String image;
    private List<String> options;
    private String answer;

    public Question(){
    }

    /**
     *
     * @return the image to display
     */
    public String getImage() {
        return image;
    }

    /**
     *
     * @param image is the image resource name
     */
    public void setImage(final String image){
        this.image = image;
    }

    /**
     *
     * @return the 4 possible answers for the question
     */
    public List<String> getOptions(){
        return options;
    }

    /**
     *
     * @param options sets the 4 possible answers for the question
     */
    public void setOptions(final List<String> options){
        this.options = options;
    }

    /**
     *
     * @return the right answer to the question
     */
    public String getAnswer(){
        return answer;
    }

    /**
     *
     * @param answer set the right answer to the question
     */
    public void setAnswer(final String answer){
        this.answer = answer;
    }
}

/**
 * The Question class builder
 */
class QuestionBuilderImpl implements QuestionBuilder {
    private Question question;

    public QuestionBuilderImpl(){
        question = new Question();
    }

    @Override
    public QuestionBuilder addImage(final String image){
        question.setImage(image);
        return this;
    }

    @Override
    public QuestionBuilder addOptions(final List<String> options){
        question.setOptions(options);
        return this;
    }

    @Override
    public QuestionBuilder addAnswer(final String answer){
        question.setAnswer(answer);
        return this;
    }

    @Override
    public Question build() throws Exception{
        if(question.getImage() == null || question.getOptions().isEmpty() || question.getAnswer() == null){
            Log.e(this.toString(),"not all the fields are set");
            throw new Exception();
        }
        return question;
    }
}

/**
 * Stores a test that contains n question from a text file. The file must contains 6 string in a row seperated with ';'.
 * The first is the question, next four are the options, the last one is the correct answer
 */
public class Test{
    private List<Question> test;
    private int n;
    private String fileName;
    private Context context;
    private Random rnd = new Random();

    /**
     * Creates a new test object
     * @param n is the number of questions in the test
     * @param fileName is the file name that contains all the possible questions (in the assets folder)
     */
    public Test(Context context, int n, String fileName){
        this.n = n;
        this.fileName = fileName;
        test = new ArrayList<>();
        this.context = context;
    }

    /**
     * Returns the question list with n question
     * @return
     */
    public List<Question> getTest(){
        createNewTest();
        return test;
    }

    /**
     * Returns the number of questions in the list
     * @return
     */
    public int getN(){
        return n;
    }

    /**
     * Randomly choose n questions from the whole possible questions
     */
    private void createNewTest(){
        List<Question> allQuestions = getFromFile();
        Set<Integer> indexes = new TreeSet<>();

        while(indexes.size() != n){
            int num = rnd.nextInt(allQuestions.size());
            Log.i(this.toString(),"Random number: " + num);
            indexes.add(num);
        }
        for (int i = 0; i < allQuestions.size(); i++) {
            if(indexes.contains(i)) {
                test.add(allQuestions.get(i));
            }
        }
        Collections.shuffle(test);
    }

    /**
     * Reads every possible question from a file in the assets folder and returns a list of the questions
     * @return the list of all the possible questions
     */
    private ArrayList<Question> getFromFile(){
        BufferedReader br = null;
        ArrayList<Question> list = new ArrayList<>();
        try {
            br = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));
            String line;
            while((line = br.readLine()) != null){
                List<String> fragments = Arrays.asList(line.split(";"));
                List<String> opts = fragments.subList(1,5);
                Collections.shuffle(opts);
                list.add(new QuestionBuilderImpl().addImage(fragments.get(0)).addOptions(opts).addAnswer(fragments.get(5)).build());
            }
        } catch (FileNotFoundException ex) {
            Log.e(this.toString(),ex.getMessage());
        } catch (IOException ex) {
            Log.e(this.toString(), ex.getMessage());
        } catch (Exception ex){
            Log.e(this.toString(), ex.getMessage());
        } finally{
            try{
                br.close();
            } catch(Exception e){
                Log.e(this.toString(),e.getMessage());
            }
        }
        return list;
    }

    /**
     * Checks whether the answers list is match the test answers in order
     * @param answers contains the users answers for the test in order
     * @return the number of the correct answers
     */
    public int checkAnswers(List<String> answers){
        int point = 0;
        for (int i = 0; i < n; i++){
            if(answers.get(i) != null && answers.get(i).compareTo(test.get(i).getAnswer()) == 0){
                point++;
            }
        }
        return point;
    }
}

