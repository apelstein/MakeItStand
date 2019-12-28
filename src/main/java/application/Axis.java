package application;

import java.util.ArrayList;

public enum Axis {
    X,
    Y,
    Z;

    public static ArrayList<Axis> get2OtherAxes(Axis chosenAxis){
        ArrayList<Axis> array = new ArrayList<>();
        for (Axis axis : Axis.values()){
            if(chosenAxis != axis){
                array.add(axis);
            }
        }
        assert array.size() == 2;
        return array;
    }
}
