package application;

import java.util.ArrayList;
import java.util.List;

public enum Axis {
    X,
    Y,
    Z;

    public static List<Axis> get2OtherAxes(Axis chosenAxis) {
        List<Axis> array = new ArrayList<>();
        for (Axis axis : Axis.values()) {
            if (chosenAxis != axis) {
                array.add(axis);
            }
        }
        assert array.size() == 2;
        return array;
    }
}
