package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import tiles.Gameboard;

import java.io.File;

public class DataManager {
    
    private String filePath = "./savefile.txt";

    private Gameboard gb;

    public DataManager (Gameboard gb) {
        this.gb = gb;
    }

    public void createSaveFile () {

        try {
            File f = new File(filePath);
            FileWriter fileWriter = new FileWriter(f);
            BufferedWriter bWriter = new BufferedWriter(fileWriter);
            bWriter.write("" + 0);

            bWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadHighScore () {
        
        try {
            File f = new File(filePath);
            
            if (!f.exists()) {
                this.createSaveFile();
            }
            
            FileReader fileReader = new FileReader(f);
            BufferedReader bReader = new BufferedReader(fileReader);

            String data = bReader.readLine();
            int hScore = Integer.parseInt(data);

            gb.setHighScore(hScore);

            bReader.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveHighScore () {

        try {
            File f = new File(filePath);
            FileWriter fileWriter = new FileWriter(f);
            BufferedWriter bWriter = new BufferedWriter(fileWriter);
            
            bWriter.write("" + gb.getHighScore());

            bWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
