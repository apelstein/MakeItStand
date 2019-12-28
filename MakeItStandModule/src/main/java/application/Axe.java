package application;

import java.util.ArrayList;

public enum Axe {
    X,
    Y,
    Z;

    public static ArrayList<Axe> get2OtherAxes(Axe chosenAxe){
        ArrayList<Axe> array = new ArrayList<>();
        for (Axe axe : Axe.values()){
            if(chosenAxe != axe){
                array.add(axe);
            }
        }
        assert array.size() == 2;
        return array;
    }
}
