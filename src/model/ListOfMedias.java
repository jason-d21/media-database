package model;

import ui.UpdateNotification;

import java.util.ArrayList;
import java.util.Observable;

public abstract class ListOfMedias {
    public abstract void update(int index, String infoToAdjust, String newInfo);
    public abstract ArrayList<String> getShortForms();
    public abstract void updateGenre(int index, boolean toAdd, ArrayList<String> newGenres);

}
