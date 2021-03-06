import java.util.*;

public class RamblersState extends SearchState {

    private Coords coords;
    private int height;

    // constructor
    public RamblersState(Coords c, int h, int lc, int rc) {
        coords = c;
        height = h;
        localCost = lc;
        estRemCost = rc;
    }

    // accessors
    public Coords getCoords() {
        return coords;
    }

    public int getHeight() {
        return height;
    }


    // goalPredicate
    public boolean goalPredicate(Search searcher) {
        RamblersSearch msearcher = (RamblersSearch) searcher;
        Coords goalCoords = msearcher.getGoalCoords();
        return (coords.getx() == goalCoords.getx() && coords.gety() == goalCoords.gety());
    }

    // method for heuristic calculation
    public static int calculateEstimatedRemainingCost(Search searcher, Coords coords) {
        RamblersSearch msearcher = (RamblersSearch) searcher;
        TerrainMap map = msearcher.getMap();

        String estimationMethod = msearcher.getEstimationMethod();

        Coords goalCoords = msearcher.getGoalCoords();

        int estimatedRemainingCost = 0;

        // all estimates will always be underestimates
        if (estimationMethod.equals("euclidean")) {
            estimatedRemainingCost = (int) Math.round(Math.sqrt((goalCoords.getx() - coords.getx()) * (goalCoords.getx() - coords.getx())
                    + (goalCoords.gety() - coords.gety()) * (goalCoords.gety() - coords.gety())));
        } else if (estimationMethod.equals("heightDiff")) {
            estimatedRemainingCost = map.getTmap()[goalCoords.gety()][goalCoords.getx()] - map.getTmap()[coords.gety()][coords.getx()];
        } else if (estimationMethod.equals("superAdvanced")) {
            int heightDiff = map.getTmap()[goalCoords.gety()][goalCoords.getx()] - map.getTmap()[coords.gety()][coords.getx()];
            int manhattanDistance = Math.abs(goalCoords.getx() - coords.getx()) + Math.abs(goalCoords.gety() - coords.gety());
            int heightDiffELU = heightDiff >= 0 ? heightDiff : (int) Math.round(Math.exp(heightDiff) - 1);

            estimatedRemainingCost = manhattanDistance + heightDiffELU;
        } else {
            // manhattan
            estimatedRemainingCost = Math.abs(goalCoords.getx() - coords.getx()) + Math.abs(goalCoords.gety() - coords.gety());
        }
        return estimatedRemainingCost;
    }

    // getSuccessors
    public ArrayList<SearchState> getSuccessors(Search searcher) {
        RamblersSearch msearcher = (RamblersSearch) searcher;
        TerrainMap map = msearcher.getMap();

        ArrayList<SearchState> succs = new ArrayList();

        int maxX = map.getWidth();
        int maxY = map.getDepth();

        if (coords.gety() > 0) {
            Coords successorCoords = new Coords(coords.gety() - 1, coords.getx());
            int successorCoordsHeight = map.getTmap()[successorCoords.gety()][successorCoords.getx()];
            int successorLocalCost = successorCoordsHeight <= height ? 1 : 1 + successorCoordsHeight - height;
            int successorEstimatedRemainingCost = calculateEstimatedRemainingCost(searcher, successorCoords);
            succs.add(new RamblersState(successorCoords, successorCoordsHeight, successorLocalCost, successorEstimatedRemainingCost));
        }

        if (coords.gety() + 1 < maxY) {
            Coords successorCoords = new Coords(coords.gety() + 1, coords.getx());
            int successorCoordsHeight = map.getTmap()[successorCoords.gety()][successorCoords.getx()];
            int successorLocalCost = successorCoordsHeight <= height ? 1 : 1 + successorCoordsHeight - height;
            int successorEstimatedRemainingCost = calculateEstimatedRemainingCost(searcher, successorCoords);
            succs.add(new RamblersState(successorCoords, successorCoordsHeight, successorLocalCost, successorEstimatedRemainingCost));
        }

        if (coords.getx() > 0) {
            Coords successorCoords = new Coords(coords.gety(), coords.getx() - 1);
            int successorCoordsHeight = map.getTmap()[successorCoords.gety()][successorCoords.getx()];
            int successorLocalCost = successorCoordsHeight <= height ? 1 : 1 + successorCoordsHeight - height;
            int successorEstimatedRemainingCost = calculateEstimatedRemainingCost(searcher, successorCoords);
            succs.add(new RamblersState(successorCoords, successorCoordsHeight, successorLocalCost, successorEstimatedRemainingCost));
        }

        if (coords.getx() + 1 < maxX) {
            Coords successorCoords = new Coords(coords.gety(), coords.getx() + 1);
            int successorCoordsHeight = map.getTmap()[successorCoords.gety()][successorCoords.getx()];
            int successorLocalCost = successorCoordsHeight <= height ? 1 : 1 + successorCoordsHeight - height;
            int successorEstimatedRemainingCost = calculateEstimatedRemainingCost(searcher, successorCoords);
            succs.add(new RamblersState(successorCoords, successorCoordsHeight, successorLocalCost, successorEstimatedRemainingCost));
        }

        return succs;
    }

    // sameState
    public boolean sameState(SearchState state) {
        RamblersState ramblersState = (RamblersState) state;
        Coords stateCoords = ramblersState.getCoords();
        return (coords.getx() == stateCoords.getx() && coords.gety() == stateCoords.gety());
    }

    // toString
    public String toString() {
        return ("\nCoordinates: (" + coords.gety() + ","
                + coords.getx() + ")\nHeight: " + height + "\n");
    }
}